package com.example.ai

import com.example.BuildConfig
import com.example.data.ErrorLogEntity
import com.example.data.OpportunityEntity
import com.example.data.OsTaskEntity
import com.example.data.ProjectEntity
import com.example.data.StudyChapterEntity
import com.example.data.UserPreferencesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object AsthaAiEngine {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun buildContextSnapshot(
        prefs: UserPreferencesEntity,
        tasks: List<OsTaskEntity>,
        chapters: List<StudyChapterEntity>,
        errors: List<ErrorLogEntity>,
        projects: List<ProjectEntity>,
        opportunities: List<OpportunityEntity>
    ): String {
        val unfinishedTop3 = tasks.filter { it.isTopThree && !it.isCompleted }
        val unresolvedErrors = errors.filter { !it.isResolved }
        return buildString {
            appendLine("STUDENT: ${prefs.userName} (${prefs.studentClass}, ${prefs.schoolName})")
            appendLine("ENERGY: ${prefs.currentEnergyLevel} | MODE: ${prefs.currentDayMode}")
            appendLine("SCHEDULE: Morning study ${prefs.morningStudyStartTime}-${prefs.morningStudyEndTime}, School ${prefs.schoolStartTime}-${prefs.schoolEndTime}, After-school reset ${prefs.afterSchoolResetStartTime}-${prefs.afterSchoolResetEndTime}, Evening study ${prefs.eveningStudyStartTime}-${prefs.eveningStudyEndTime} (Mode: ${prefs.eveningPlanMode}), Wind-down ${prefs.windDownTime}, Bedtime ${prefs.targetBedtime}. NOTE: User does not attend coaching; user uses self-paced Mission Jeet batch.")
            appendLine("TOP 3 UNFINISHED TASKS: ${unfinishedTop3.joinToString(" | ") { "${it.title} (${it.durationMinutes}m)" }}")
            appendLine("CHAPTERS: ${chapters.joinToString(" | ") { "${it.subject}: ${it.chapterName} [Status=${it.understandingStatus}, Weak=${it.weakAreas}]" }}")
            appendLine("UNRESOLVED MISTAKES (${unresolvedErrors.size}): ${unresolvedErrors.take(3).joinToString(" | ") { "${it.subject} (${it.mistakeType}): ${it.questionSummary}" }}")
            appendLine("PROJECTS: ${projects.joinToString(" | ") { "${it.name} [Stage=${it.pipelineStage}, Evidence=${it.evidenceStrengthLabel}, Next=${it.nextConcreteAction}]" }}")
            appendLine("NCSC: ${prefs.ncscProjectTitle} on ${prefs.ncscTargetDate}")
            appendLine("OPPORTUNITIES: ${opportunities.joinToString(" | ") { "${it.name} (${it.status})" }}")
        }
    }

    suspend fun askAsthaAi(
        userPrompt: String,
        prefs: UserPreferencesEntity,
        tasks: List<OsTaskEntity>,
        chapters: List<StudyChapterEntity>,
        errors: List<ErrorLogEntity>,
        projects: List<ProjectEntity>,
        opportunities: List<OpportunityEntity>
    ): String = withContext(Dispatchers.IO) {
        val localSmartResponse = buildLocalGroundedAnswer(
            userPrompt = userPrompt,
            prefs = prefs,
            tasks = tasks,
            chapters = chapters,
            errors = errors,
            projects = projects,
            opportunities = opportunities
        )

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // If key is placeholder or empty, immediately return the grounded local intelligence response
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext localSmartResponse
        }

        val systemText = """
            You are ASTHA AI, the calm, intelligent personal operating system and study coach for Astha (Class 11 PCM + CS student).
            Core philosophy: "Know what matters. Do the next right thing."
            Rules:
            - Be concise, warm, calm, and ultra-concrete.
            - Never invent fake achievements, fake user numbers, or fake scholarship deadlines.
            - For study questions or when Astha is stuck, use stepwise hints (Hint 1 -> Hint 2 -> First Step) rather than dumping full solutions immediately.
            - Ground your answer strictly in her stored ASTHA OS state below:
            ${buildContextSnapshot(prefs, tasks, chapters, errors, projects, opportunities)}
        """.trimIndent()

        try {
            val payload = JSONObject().apply {
                put(
                    "systemInstruction",
                    JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().put("text", systemText)))
                    }
                )
                put(
                    "contents",
                    JSONArray().put(
                        JSONObject().apply {
                            put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
                        }
                    )
                )
                put(
                    "generationConfig",
                    JSONObject().apply {
                        put("temperature", 0.5)
                    }
                )
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(payload.toString().toRequestBody("application/json".toMediaType()))
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext localSmartResponse
                }
                val bodyStr = response.body?.string().orEmpty()
                val json = JSONObject(bodyStr)
                val candidates = json.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text").orEmpty().trim()
                if (text.isNotBlank()) text else localSmartResponse
            }
        } catch (_: Exception) {
            localSmartResponse
        }
    }

    fun buildLocalGroundedAnswer(
        userPrompt: String,
        prefs: UserPreferencesEntity,
        tasks: List<OsTaskEntity>,
        chapters: List<StudyChapterEntity>,
        errors: List<ErrorLogEntity>,
        projects: List<ProjectEntity>,
        opportunities: List<OpportunityEntity>
    ): String {
        val q = userPrompt.lowercase()
        val topAcademic = tasks.firstOrNull { it.domain == "ACADEMIC" && !it.isCompleted }
        val topProject = projects.firstOrNull()
        val topChapter = chapters.firstOrNull()
        val topError = errors.firstOrNull { !it.isResolved }

        return when {
            "what should i do now" in q || "next" in q && "project" !in q -> {
                val chosen = topAcademic ?: tasks.firstOrNull { !it.isCompleted }
                if (chosen != null) {
                    "NEXT RIGHT THING (${chosen.durationMinutes} min • ${prefs.currentEnergyLevel} Energy):\n" +
                        "• ${chosen.title}\n" +
                        "• How to do it: ${chosen.subtitle}\n" +
                        "• Why now: ${chosen.whyItMatters}"
                } else {
                    "Your priority tasks for today are complete! Take a 10-minute movement reset or enjoy your ${prefs.entertainmentTitle} wallet guilt-free."
                }
            }

            "what should i study" in q || "study" in q && "one-shot" !in q -> {
                val ch = topChapter
                if (ch != null) {
                    "RECOMMENDED STUDY MISSION:\n" +
                        "• ${ch.subject} — ${ch.chapterName} (${ch.currentTopic})\n" +
                        "• One-shot status: ${if (ch.oneShotCompleted) "Completed" else "Not done yet"}\n" +
                        "• Concrete mission: Start with 5 min active recall of '${ch.capturedFormula}', solve 2 easy questions, then 3 medium questions focusing on ${ch.weakAreas}.\n" +
                        (if (topError != null) "• Mistake to watch: ${topError.correctIdea}" else "")
                } else {
                    "Open STUDY and select your current Physics or Maths chapter to start a 45-minute structured block."
                }
            }

            "one-shot" in q -> {
                val ch = topChapter
                "AFTER YOUR ONE-SHOT WORKFLOW:\n" +
                    "1. Close YouTube/video now — do NOT watch another full lecture.\n" +
                    "2. Active Recall (5 min): Explain ${ch?.currentTopic ?: "the main concept"} and write its key formula from memory.\n" +
                    "3. Mark Understanding Check (Green / Yellow / Red).\n" +
                    "4. Solve 2 Easy + 3 Medium questions immediately.\n" +
                    "5. Log any formula or setup slip into your Error Log."
            }

            "halfyearly" in q || "half-yearly" in q || "16 nov" in q || "exam" in q -> {
                "HALF-YEARLY EXAM STRATEGY (16 NOV COUNTDOWN):\n" +
                    "• Physics: Rotational dynamics derivations (Parallel/Perp axes, rolling without slipping, torque equilibrium).\n" +
                    "• Chemistry Physical: Chemical Thermodynamics (First Law, isothermal vs adiabatic w, ΔG spontaneity).\n" +
                    "• Chemistry Organic: GOC (Carbocation/carbanion stability, resonance vs inductive, ortho effect).\n" +
                    "• Mathematics: Complex numbers (Modulus-argument, Argand plane locus, AP/GP sums).\n" +
                    "• Computer Science: Python functions & mutable data structures.\n" +
                    "• Secret to High Marks: 20-min active recall of your Special Chapter Vault on blank paper before each mock numerical."
            }

            "anki" in q || "flashcard" in q || "recall" in q || "question" in q || "quiz" in q -> {
                val ch = topChapter
                "ACTIVE RECALL COACH (BETTER THAN ANKI):\n" +
                    "Unlike standard Anki decks that disconnect from your syllabus, your OS integrates recall directly into your Chapter Vault:\n" +
                    "• Question 1 (State Aloud): What is the core governing law of ${ch?.currentTopic ?: "this topic"} and its defining condition?\n" +
                    "• Formula Test: Write '${ch?.keyFormulasText?.lines()?.firstOrNull() ?: ch?.capturedFormula ?: "τ = Iα"}' from memory without checking.\n" +
                    "• Exam Trap Alert: ${ch?.examTrapsAndTricks?.lines()?.firstOrNull() ?: "Watch sign conventions and units!"}"
            }

            "feynman" in q -> {
                val ch = topChapter
                "FEYNMAN TECHNIQUE CHECK:\n" +
                    "• Topic: ${ch?.currentTopic ?: "Core Concept"}\n" +
                    "• Challenge: Explain this concept in 2 plain sentences without technical jargon.\n" +
                    "• Self-Diagnostic: If you struggle to explain why a formula contains a minus sign or squared factor, that is your high-yield focus area for today!"
            }

            "startup" in q || "idea" in q || "app" in q && "project" in q -> {
                "HIGH-GROWTH STARTUP INCUBATOR:\n" +
                    "• Vision: Building a valuable working app that students or users love daily.\n" +
                    "• The 3 Pillars of Valuable Apps:\n" +
                    "  1. Pain Killer: Solves a daily recurring frustration (e.g. scattered study materials, decision paralysis).\n" +
                    "  2. Zero Friction: Time to value under 30 seconds (like Morning Launch).\n" +
                    "  3. Retention Loop: Delivers compounding benefits the more it's used.\n" +
                    "• Next Step: Open BUILD -> Startup Studio and define your 45-minute MVP sprint."
            }

            "ncsc" in q || "ecodrain" in q -> {
                "NCSC STATUS: OFFICIALLY COMPLETED & ARCHIVED!\n" +
                    "• Congratulations Astha! Your Smart EcoDrain Shield was fully presented and documented.\n" +
                    "• All primary focus now transitions to your Half-Yearly Exams (starting 16 Nov) across all subjects (Physics, Chemistry, Maths, CS) and your new high-growth startup app."
            }

            else -> {
                "ASTHA AI (Grounded in your OS):\n" +
                    "• Priority Now: ${topAcademic?.title ?: "Physics 45-min focus block"}\n" +
                    "• Next Project Step (${topProject?.name ?: "Krow"}): ${topProject?.nextConcreteAction ?: "Collect 3 pieces of feedback"}\n" +
                    "• Unresolved Mistakes to Review: ${errors.count { !it.isResolved }}\n\n" +
                    "Know what matters. Do the next right thing."
            }
        }
    }
}
