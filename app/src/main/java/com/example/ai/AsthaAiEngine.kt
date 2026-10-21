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
            appendLine("SCHEDULE: School ${prefs.schoolStartTime}-${prefs.schoolEndTime}, Drona ${prefs.dronaSchedule}, Wind-down ${prefs.windDownTime}")
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

            "question" in q || "quiz" in q || "recall" in q -> {
                val ch = topChapter
                "ACTIVE RECALL COACH (${ch?.subject ?: "Physics"} — ${ch?.chapterName ?: "Current Chapter"}):\n" +
                    "Question 1 (Answer in your own words before checking notes):\n" +
                    "• State the core principle of ${ch?.currentTopic ?: "Rotational Dynamics"} and explain when '${ch?.capturedFormula ?: "τ = Iα"}' applies vs when it breaks down.\n" +
                    "• Common trap to avoid: ${topError?.whyIMadeIt ?: ch?.weakAreas ?: "Sign conventions and axis choice"}."
            }

            "stuck" in q || "hint" in q -> {
                "I'M STUCK COACH:\n" +
                    "Your job isn't to solve it instantly. Your job is to find the next step.\n\n" +
                    "• Step 1 (2 min): Write down ONLY (a) What is given with units, and (b) What exact quantity is asked.\n" +
                    "• Step 2 (Hint 1): Which conservation law or definition connects those two quantities?\n" +
                    "• Tap 'I'M STUCK' inside Study or Focus Mode for the 5-level progressive hint ladder."
            }

            "20 min" in q || "15 min" in q || "minutes" in q -> {
                if (topError != null) {
                    "20-MINUTE HIGH-VALUE MISSION:\n" +
                        "• Review ${errors.count { !it.isResolved }.coerceAtMost(3)} unresolved mistakes in your Error Log (starting with ${topError.subject}: ${topError.questionSummary}).\n" +
                        "• Re-solve them on blank paper without looking at the correction."
                } else {
                    "20-MINUTE HIGH-VALUE MISSION:\n" +
                        "• Solve 3 targeted medium questions in ${topChapter?.subject ?: "Physics"} (${topChapter?.chapterName ?: "Current Chapter"})."
                }
            }

            "missed" in q || "behind" in q || "late" in q || "reset" in q -> {
                "Let's reset from here. A missed task does not mean the day is ruined.\n\n" +
                    "• MUST DO: ${topAcademic?.title ?: "1 core 25-min study block"} + Evening wind-down.\n" +
                    "• SHOULD DO: ${topProject?.name ?: "Krow"} 15-min next action.\n" +
                    "• CAN POSTPONE: Move lower-priority sets to tomorrow automatically via the Recovery button on TODAY."
            }

            "scholarship" in q || "profile" in q -> {
                val weakEvidenceProj = projects.minByOrNull { it.evidenceCheckedCount }
                "SCHOLARSHIP & PORTFOLIO NEXT STEP:\n" +
                    "• Don't try to 'improve everything at once.'\n" +
                    "• Concrete action: Add 1 piece of documented evidence to '${weakEvidenceProj?.name ?: "Krow"}' (Current evidence strength: ${weakEvidenceProj?.evidenceStrengthLabel ?: "BUILDING"}).\n" +
                    "• Next step: ${weakEvidenceProj?.nextConcreteAction ?: "Document user feedback or testing results."}"
            }

            "project" in q || "krow" in q -> {
                val p = projects.find { it.name.lowercase() in q } ?: topProject
                if (p != null) {
                    "PROJECT NEXT ACTION — ${p.name} (Stage: ${p.pipelineStage}):\n" +
                        "• Concrete action: ${p.nextConcreteAction}\n" +
                        "• Evidence strength: ${p.evidenceStrengthLabel} (${p.evidenceCheckedCount}/9 items verified)."
                } else {
                    "Open BUILD to define your next concrete action for Krow or Smart EcoDrain Shield."
                }
            }

            "ncsc" in q || "ecodrain" in q -> {
                "NCSC PREPARATION MISSION (${prefs.ncscProjectTitle}):\n" +
                    "• Target Date: ${prefs.ncscTargetDate}\n" +
                    "• Today's 10-minute mission: Practice explaining the problem statement and core filtration/sensor innovation aloud in 90 seconds, then answer 1 likely judge question on sustainability."
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
