package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.AsthaAiEngine
import com.example.data.AiChatMessage
import com.example.data.BrainDumpEntity
import com.example.data.CalendarEventEntity
import com.example.data.ConfidenceEntryEntity
import com.example.data.DailyHabitEntity
import com.example.data.DailyProgressEntity
import com.example.data.DeadlinePreparationSuggestion
import com.example.data.DefaultWellnessData
import com.example.data.DistractionLogEntity
import com.example.data.EndOfDayReflectionEntity
import com.example.data.ErrorLogEntity
import com.example.data.EventPrepMilestoneEntity
import com.example.data.AcademicTopicEntity
import com.example.data.AngerOverwhelmPauseEntity
import com.example.data.FinancialExperimentEntity
import com.example.data.FinancialLesson
import com.example.data.GeneratedRoutineBlock
import com.example.data.GlowUpRepository
import com.example.data.HobbyEntity
import com.example.data.HydrationReminderEntity
import com.example.data.LongTermGoalEntity
import com.example.data.MissionJeetLectureEntity
import com.example.data.MonthlyGoalEntity
import com.example.data.MorningDecisionPlan
import com.example.data.MorningStudyPriority
import com.example.data.MovementRoutine
import com.example.data.NowRecommendation
import com.example.data.OpportunityEntity
import com.example.data.OsTaskEntity
import com.example.data.PracticeQuestionItem
import com.example.data.ProfileAchievementEntity
import com.example.data.ProfileEvidenceLedgerEntity
import com.example.data.ProjectEntity
import com.example.data.RecentWinEntity
import com.example.data.ScenarioPlanEntity
import com.example.data.ScholarshipStrategyEntity
import com.example.data.ScienceExhibitionScorecardEntity
import com.example.data.SetbackRecoveryEntity
import com.example.data.SkincareStepEntity
import com.example.data.SpecialDayPlanEntity
import com.example.data.StudyChapterEntity
import com.example.data.TimelineBlockEntity
import com.example.data.UserPreferencesEntity
import com.example.notifications.GlowUpNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class BottomNavTab(val route: String, val label: String) {
    TODAY("today", "TODAY"),
    STUDY("study", "STUDY"),
    BUILD("build", "BUILD"),
    WELLNESS("wellness", "WELLNESS"),
    PROFILE("profile", "PROFILE")
}

sealed class ActiveSubScreen {
    data object None : ActiveSubScreen()
    data class FocusMode(
        val taskTitle: String,
        val purpose: String,
        val durationMinutes: Int,
        val currentStepText: String,
        val totalSteps: Int = 5,
        val linkedTask: OsTaskEntity? = null
    ) : ActiveSubScreen()
    data class CalendarHorizonsSheet(val initialTab: String = "CALENDAR") : ActiveSubScreen()
    data object CloseTodayGentlySheet : ActiveSubScreen()
    data object ImStuckSheet : ActiveSubScreen()
    data object AsthaAiSheet : ActiveSubScreen()
    data object BrainDumpSheet : ActiveSubScreen()
    data object AfterSchoolResetSheet : ActiveSubScreen()
    data object EntertainmentWalletSheet : ActiveSubScreen()
    data object MorningModeSheet : ActiveSubScreen()
    data object SchoolModeSheet : ActiveSubScreen()
    data object YoutubeStudySheet : ActiveSubScreen()
    data class OneShotSheet(val chapter: StudyChapterEntity) : ActiveSubScreen()
    data class PracticeQuestionsSheet(val initialSubject: String = "All") : ActiveSubScreen()
    data object ErrorLogSheet : ActiveSubScreen()
    data object WeeklyReviewSheet : ActiveSubScreen()
    data object RecoveryResetSheet : ActiveSubScreen()
    data object SettingsSheet : ActiveSubScreen()
    data object HydrationSheet : ActiveSubScreen()
    data object SleepSheet : ActiveSubScreen()
    data object ConfidenceCheckInSheet : ActiveSubScreen()
    data object ConfidenceLabSheet : ActiveSubScreen()
    data object SetbackRecoverySheet : ActiveSubScreen()
    data object FinancialLiteracySheet : ActiveSubScreen()
    data object StudyResetSheet : ActiveSubScreen()
    data object DailyRoutineGeneratorSheet : ActiveSubScreen()
    data class ActiveWorkoutTimer(val routine: MovementRoutine) : ActiveSubScreen()
    data object ChemistryMissionSheet : ActiveSubScreen()
    data object AcademicExcellenceSheet : ActiveSubScreen()
    data object ScienceExhibitionSheet : ActiveSubScreen()
    data object ScholarshipStrategySheet : ActiveSubScreen()
    data object ScenarioPlanningSheet : ActiveSubScreen()
    data object ProfileEvidenceSheet : ActiveSubScreen()
    data object AngerOverwhelmPauseSheet : ActiveSubScreen()
    data object MissionJeetSheet : ActiveSubScreen()
    data object HobbiesAndJoySheet : ActiveSubScreen()
    data object DistractionRecoverySheet : ActiveSubScreen()
    data class ChapterMaterialFolderSheet(val chapter: StudyChapterEntity) : ActiveSubScreen()
    data class StartupIdeaIncubatorSheet(val project: ProjectEntity? = null) : ActiveSubScreen()
}


data class WeeklyStatsUi(
    val movementCompletedDays: Int = 4,
    val movementTargetDays: Int = 5,
    val hydrationCompletedCount: Int = 7,
    val hydrationTargetCount: Int = 10,
    val skincareCompletedCount: Int = 10,
    val skincareTargetCount: Int = 10,
    val sleepCompletedDays: Int = 6,
    val sleepTargetDays: Int = 7,
    val confidenceCompletedDays: Int = 4,
    val confidenceTargetDays: Int = 7,
    val strongestHabitTitle: String = "✨ Self-Care & Grooming",
    val latestReflectionText: String = "Showed up consistently for Physics numericals and Krow prototype."
)

class GlowUpViewModel(
    private val repository: GlowUpRepository,
    private val appContext: Context
) : ViewModel() {

    val preferences: StateFlow<UserPreferencesEntity> = repository.userPreferences
        .combine(MutableStateFlow(UserPreferencesEntity())) { dbPrefs, fallback ->
            dbPrefs ?: fallback
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferencesEntity())

    val osTasks: StateFlow<List<OsTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timelineBlocks: StateFlow<List<TimelineBlockEntity>> = repository.timelineBlocks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyChapters: StateFlow<List<StudyChapterEntity>> = repository.studyChapters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val errorLogs: StateFlow<List<ErrorLogEntity>> = repository.errorLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projects: StateFlow<List<ProjectEntity>> = repository.projects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profileAchievements: StateFlow<List<ProfileAchievementEntity>> = repository.profileAchievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val opportunities: StateFlow<List<OpportunityEntity>> = repository.opportunities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val brainDumps: StateFlow<List<BrainDumpEntity>> = repository.brainDumps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val distractionLogs: StateFlow<List<DistractionLogEntity>> = repository.distractionLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<DailyHabitEntity>> = repository.activeHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val skincareSteps: StateFlow<List<SkincareStepEntity>> = repository.skincareSteps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hydrationReminders: StateFlow<List<HydrationReminderEntity>> = repository.hydrationReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val confidenceEntries: StateFlow<List<ConfidenceEntryEntity>> = repository.confidenceEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressHistory: StateFlow<List<DailyProgressEntity>> = repository.recentProgressHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val calendarEvents: StateFlow<List<CalendarEventEntity>> = repository.calendarEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthlyGoals: StateFlow<List<MonthlyGoalEntity>> = repository.monthlyGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val longTermGoals: StateFlow<List<LongTermGoalEntity>> = repository.longTermGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentWins: StateFlow<List<RecentWinEntity>> = repository.recentWins
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val specialDayPlans: StateFlow<List<SpecialDayPlanEntity>> = repository.specialDayPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventPrepMilestones: StateFlow<List<EventPrepMilestoneEntity>> = repository.eventPrepMilestones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val endOfDayReflections: StateFlow<List<EndOfDayReflectionEntity>> = repository.endOfDayReflections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val setbackRecoveries: StateFlow<List<SetbackRecoveryEntity>> = repository.setbackRecoveries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val financialExperiments: StateFlow<List<FinancialExperimentEntity>> = repository.financialExperiments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val academicTopics: StateFlow<List<AcademicTopicEntity>> = repository.academicTopics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scienceExhibitionScorecards: StateFlow<List<ScienceExhibitionScorecardEntity>> = repository.scienceExhibitionScorecards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scholarshipStrategies: StateFlow<List<ScholarshipStrategyEntity>> = repository.scholarshipStrategies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scenarioPlans: StateFlow<List<ScenarioPlanEntity>> = repository.scenarioPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profileEvidenceLedger: StateFlow<List<ProfileEvidenceLedgerEntity>> = repository.profileEvidenceLedger
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val angerOverwhelmPauses: StateFlow<List<AngerOverwhelmPauseEntity>> = repository.angerOverwhelmPauses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val missionJeetLectures: StateFlow<List<MissionJeetLectureEntity>> = repository.missionJeetLectures
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hobbies: StateFlow<List<HobbyEntity>> = repository.hobbies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _morningDecisionPlan = MutableStateFlow<MorningDecisionPlan?>(null)
    val morningDecisionPlan: StateFlow<MorningDecisionPlan?> = _morningDecisionPlan.asStateFlow()


    val deadlineSuggestions: StateFlow<List<DeadlinePreparationSuggestion>> = calendarEvents
        .combine(preferences) { events, _ ->
            val todayStr = repository.todayDateString()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val todayDate = try { sdf.parse(todayStr) } catch (_: Exception) { null }
            events.mapNotNull { ev ->
                val targetDate = try { sdf.parse(ev.dateString) } catch (_: Exception) { null }
                val daysDiff = if (todayDate != null && targetDate != null) {
                    ((targetDate.time - todayDate.time) / (1000L * 60 * 60 * 24)).toInt()
                } else 5
                val dismissedSet = ev.dismissedMilestoneDaysCsv.split(",").map { it.trim() }.toSet()
                if (daysDiff in 0..7 && daysDiff.toString() !in dismissedSet) {
                    val (taskTitle, taskSub) = when {
                        ev.category == "COMPETITION" && daysDiff >= 4 ->
                            "Review problem statement & innovation (${ev.title})" to "Refine 90-second problem & innovation script"
                        ev.category == "COMPETITION" && daysDiff in 2..3 ->
                            "Check prototype / evidence (${ev.title})" to "Verify model test logs and charts"
                        ev.category == "COMPETITION" ->
                            "Final pitch & Q&A check (${ev.title})" to "Practice 90-second pitch + 3 judge questions aloud"
                        ev.category == "ACADEMICS" ->
                            "Targeted revision for ${ev.title}" to "Solve 4 mixed questions + review Error Log mistakes"
                        else ->
                            "Advance preparation for ${ev.title}" to (ev.locationOrNotes.ifBlank { "Complete 1 concrete 20-minute prep block" })
                    }
                    val calmText = when (daysDiff) {
                        0 -> "Today • ${ev.startTime}"
                        1 -> "Tomorrow • ${ev.startTime}"
                        else -> "In $daysDiff days"
                    }
                    DeadlinePreparationSuggestion(
                        eventId = ev.id,
                        eventTitle = ev.title,
                        category = ev.category,
                        daysRemaining = daysDiff,
                        countdownCalmText = calmText,
                        suggestedTaskTitle = taskTitle,
                        suggestedTaskSubtitle = taskSub,
                        durationMinutes = 20
                    )
                } else null
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTab = MutableStateFlow(BottomNavTab.TODAY)
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()

    private val _activeSubScreen = MutableStateFlow<ActiveSubScreen>(ActiveSubScreen.None)
    val activeSubScreen: StateFlow<ActiveSubScreen> = _activeSubScreen.asStateFlow()

    private val _exportSummaryDialogText = MutableStateFlow<String?>(null)
    val exportSummaryDialogText: StateFlow<String?> = _exportSummaryDialogText.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    private val _skipOrEscapeCount = MutableStateFlow(0)
    val skipOrEscapeCount: StateFlow<Int> = _skipOrEscapeCount.asStateFlow()

    private val _aiMessages = MutableStateFlow(
        listOf(
            AiChatMessage(
                isUser = false,
                text = "Hi Astha! I'm ASTHA AI — your personal operating system & study coach.\n\n" +
                    "I know your Class 11 PCM chapters, Error Log, Krow & Smart EcoDrain Shield projects, NCSC prep, Calendar deadlines, Monthly Goals, and energy level.\n\n" +
                    "Ask me anything or tap a quick prompt below!"
            )
        )
    )
    val aiMessages: StateFlow<List<AiChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    val nowRecommendation: StateFlow<NowRecommendation> = combine(
        preferences,
        osTasks,
        studyChapters,
        errorLogs,
        projects
    ) { prefs, tasks, chapters, errors, projs ->
        val timeFmt = SimpleDateFormat("h:mm a", Locale.US).format(Date())
        val nowMs = System.currentTimeMillis()
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val energy = prefs.currentEnergyLevel
        val rotation = prefs.recommendationRotationCounter
        val lastSubj = prefs.lastRecommendedSubject
        val lastTaskId = prefs.lastRecommendedTaskId

        val contextualLine = when {
            energy == "LOW" -> "Take it gently — one small step is enough."
            hour in 5..11 -> "Start calm. One focused block sets the rhythm."
            hour in 12..16 -> "Welcome back. Reset first, then do one clear thing."
            hour in 17..20 -> "Your evening is still yours."
            else -> "Your day is still yours."
        }

        val unfinished = tasks.filter { !it.isCompleted && !it.isPostponed }
        val unresolvedErrors = errors.filter { !it.isResolved }

        // Build diverse candidate pool with Weighted Scoring + Recency Penalty
        // Score = Urgency + Importance + Goal Alignment + Energy Match + Time Fit + Subject Balance - Recency Penalty
        data class ScoredCandidate(val score: Int, val rec: NowRecommendation)
        val scoredCandidates = mutableListOf<ScoredCandidate>()

        val alternateLabels = listOf(
            "Chemistry • 15 min formula & numerical recall",
            "Krow • 20 min user feedback step",
            "NCSC • 10 min speaking rehearsal",
            "Error Log • 15 min spaced mistake check",
            "Wellness • 10 min mobility reset"
        )

        // 1. Project / Krow / Smart EcoDrain Shield Candidates (High Goal Alignment & Variety)
        projs.forEachIndexed { idx, proj ->
            val wasJustRecommended = proj.name.equals(lastSubj, ignoreCase = true)
            val recencyPenalty = if (wasJustRecommended) 45 else 0
            val energyBonus = if (energy != "LOW") 18 else 8
            val baseScore = 88 - (idx * 6) + energyBonus - recencyPenalty
            val humanLine = if (proj.name.contains("Krow", ignoreCase = true)) {
                "Your Krow prototype is moving forward — 1 user note unlocks validation."
            } else {
                "Your ${proj.name} model is taking shape for NCSC."
            }
            scoredCandidates.add(
                ScoredCandidate(
                    score = baseScore,
                    rec = NowRecommendation(
                        title = proj.name,
                        subtitleShort = "Get one piece of user feedback.",
                        humanHeadline = humanLine,
                        concreteAction = proj.nextConcreteAction,
                        whyReason = "Advances your monthly project goal and builds real portfolio evidence (${proj.evidenceCheckedCount}/9).",
                        durationMinutes = 20,
                        currentTimeFormatted = timeFmt,
                        contextualLine = contextualLine,
                        categoryBadge = "YOUR NEXT MOVE",
                        energyLevel = energy,
                        availableMinutes = 20,
                        priorityDomain = "PROJECT",
                        subjectOrProject = proj.name,
                        alternateOptions = alternateLabels.filterNot { it.startsWith(proj.name) }.take(3),
                        projectToBuild = proj
                    )
                )
            )
        }

        // 2. Unfinished Tasks with Recency Penalty (completed within 4h or recommended within 2h penalized)
        unfinished.forEach { task ->
            val minsSinceRecommended = if (task.lastRecommendedAt > 0L) ((nowMs - task.lastRecommendedAt) / 60000L) else 999L
            val minsSinceCompleted = if (task.lastCompletedAt > 0L) ((nowMs - task.lastCompletedAt) / 60000L) else 999L
            val sameAsLastSubj = task.subject.isNotBlank() && task.subject.equals(lastSubj, ignoreCase = true)
            val sameAsLastId = task.id == lastTaskId

            var recencyPenalty = 0
            if (sameAsLastId) recencyPenalty += 55
            if (sameAsLastSubj) recencyPenalty += 40
            if (minsSinceRecommended < 120) recencyPenalty += 35
            if (minsSinceCompleted < 240) recencyPenalty += 50
            recencyPenalty += (task.skipCount * 20)

            val urgencyScore = if (task.priorityBucket == "MUST DO") 30 else 18
            val topThreeBonus = if (task.isTopThree) 20 else 8
            val energyFit = if (energy == "LOW" && task.energyRequired != "LOW") -20 else 15
            val subjectBalanceBonus = if (task.subject.equals("Physics", ignoreCase = true)) 0 else 18

            val totalScore = 50 + urgencyScore + topThreeBonus + energyFit + subjectBalanceBonus - recencyPenalty
            val adaptedMins = when {
                energy == "LOW" -> minOf(15, task.durationMinutes)
                task.durationMinutes > 30 -> 25
                else -> task.durationMinutes
            }
            val displayTitle = task.subject.ifBlank { task.title }
            val shortSub = when {
                task.subject.equals("Physics", ignoreCase = true) -> "Solve 4 mixed rotational questions."
                task.subject.equals("Mathematics", ignoreCase = true) -> "Solve 5 targeted problems without notes."
                task.domain == "PROJECT" -> "Get one piece of user feedback."
                task.domain == "WELLNESS" -> "10-minute mobility & hydration reset."
                else -> task.subtitle.take(48)
            }
            val humanLine = when {
                task.domain == "PROJECT" -> "Your Krow prototype is moving forward, and 1 user note unlocks validation."
                task.domain == "WELLNESS" -> "A 10-minute reset keeps your posture, focus, and energy fresh."
                else -> "You're building steady momentum in $displayTitle — one focused set locks it in."
            }
            scoredCandidates.add(
                ScoredCandidate(
                    score = totalScore,
                    rec = NowRecommendation(
                        title = displayTitle,
                        subtitleShort = shortSub,
                        humanHeadline = humanLine,
                        concreteAction = task.subtitle,
                        whyReason = task.whyItMatters,
                        durationMinutes = adaptedMins,
                        currentTimeFormatted = timeFmt,
                        contextualLine = contextualLine,
                        categoryBadge = if (energy == "LOW") "GENTLE NEXT MOVE" else "YOUR NEXT MOVE",
                        energyLevel = energy,
                        availableMinutes = adaptedMins,
                        priorityDomain = task.domain,
                        subjectOrProject = displayTitle,
                        alternateOptions = alternateLabels.filterNot { it.startsWith(displayTitle) }.take(3),
                        taskToStart = task
                    )
                )
            )
        }

        // 3. Spaced Error Log Review Candidate
        if (unresolvedErrors.isNotEmpty()) {
            val topErr = unresolvedErrors.first()
            val errPenalty = if (lastSubj.contains("Error", ignoreCase = true)) 45 else 0
            scoredCandidates.add(
                ScoredCandidate(
                    score = 84 - errPenalty,
                    rec = NowRecommendation(
                        title = "${topErr.subject} Error Log",
                        subtitleShort = "Review ${unresolvedErrors.size.coerceAtMost(3)} mistakes due for spaced recall.",
                        humanHeadline = "You logged ${unresolvedErrors.size} key insights — reviewing them now turns mistakes into marks.",
                        concreteAction = "Re-solve '${topErr.questionSummary.take(65)}...' on blank paper and verify: ${topErr.correctIdea}",
                        whyReason = "24h / 3d / 7d spaced repetition prevents repeating the same formula mistake.",
                        durationMinutes = 15,
                        currentTimeFormatted = timeFmt,
                        contextualLine = contextualLine,
                        categoryBadge = "YOUR NEXT MOVE",
                        energyLevel = energy,
                        availableMinutes = 15,
                        priorityDomain = "ACADEMIC",
                        subjectOrProject = "Error Log",
                        alternateOptions = alternateLabels.filterNot { it.startsWith("Error") }.take(3)
                    )
                )
            )
        }

        // 4. Least-Recently Studied Subject Balance Candidate (Chemistry / Maths / CS / Physics)
        chapters
            .sortedBy { it.lastStudiedTimestamp }
            .forEachIndexed { index, ch ->
                val sameSubjPenalty = if (ch.subject.equals(lastSubj, ignoreCase = true)) 45 else 0
                val nonPhysicsBonus = if (!ch.subject.equals("Physics", ignoreCase = true)) 14 else 0
                val score = 82 - (index * 5) + nonPhysicsBonus - sameSubjPenalty
                scoredCandidates.add(
                    ScoredCandidate(
                        score = score,
                        rec = NowRecommendation(
                            title = ch.subject,
                            subtitleShort = "4 mixed questions • ${ch.chapterName}",
                            humanHeadline = "Keep ${ch.subject} (${ch.chapterName}) fresh with a short active-recall set.",
                            concreteAction = "Active recall '${ch.capturedFormula.ifBlank { ch.currentTopic }}' (5m) → Solve 4 mixed questions on ${ch.weakAreas}.",
                            whyReason = "Subject-balance rotation ensures Chemistry, Maths, CS, and Physics progress together.",
                            durationMinutes = if (energy == "LOW") 15 else 25,
                            currentTimeFormatted = timeFmt,
                            contextualLine = contextualLine,
                            categoryBadge = "YOUR NEXT MOVE",
                            energyLevel = energy,
                            availableMinutes = if (energy == "LOW") 15 else 25,
                            priorityDomain = "ACADEMIC",
                            subjectOrProject = ch.subject,
                            alternateOptions = alternateLabels.filterNot { it.startsWith(ch.subject.take(6)) }.take(3),
                            chapterToStudy = ch
                        )
                    )
                )
            }

        // 5. Night Brain Dump if late evening
        if (hour >= 21) {
            scoredCandidates.add(
                ScoredCandidate(
                    score = 120,
                    rec = NowRecommendation(
                        title = "Night Brain Dump",
                        subtitleShort = "Empty your head & prepare tomorrow in 5 min.",
                        humanHeadline = "Nothing else needs to be solved tonight — clear your head for restful sleep.",
                        concreteAction = "Write down any lingering thoughts, pack tomorrow's bag, and start your wind-down.",
                        whyReason = "It's past 9:00 PM — protecting your sleep makes tomorrow effortless.",
                        durationMinutes = 10,
                        currentTimeFormatted = timeFmt,
                        contextualLine = "Nothing else needs to be solved tonight.",
                        categoryBadge = "TONIGHT • WIND-DOWN",
                        energyLevel = energy,
                        availableMinutes = 10,
                        priorityDomain = "WELLNESS",
                        subjectOrProject = "Wind-Down",
                        alternateOptions = listOf("Evening Skincare • 5 min", "Tomorrow Top 3 • 5 min")
                    )
                )
            )
        }

        val orderedPool = scoredCandidates.sortedByDescending { it.score }.map { it.rec }
        val todayStr = repository.todayDateString()
        val todaySpecialPlan = specialDayPlans.value.find { it.dateString == todayStr }
        val activeDayType = todaySpecialPlan?.dayType ?: when (prefs.currentDayMode) {
            "Competition preparation" -> "COMPETITION PREPARATION DAY"
            "Exam preparation" -> "REVISION DAY"
            "Weekend" -> "PROJECT DAY"
            else -> "BALANCED DAY"
        }
        val upcomingCriticalEvents = calendarEvents.value.filter {
            it.priority == "HIGH" && (it.dateString == todayStr || it.isMultiDay)
        }
        val isLightOrSocialDay = activeDayType in listOf(
            "REST / RESET DAY",
            "FAMILY TIME DAY",
            "FRIENDS / SOCIAL DAY",
            "OUTDOOR / EXPERIENCE DAY"
        )
        val conflictMessage = if (isLightOrSocialDay && upcomingCriticalEvents.isNotEmpty()) {
            val evTitle = upcomingCriticalEvents.first().title
            "Heads up: You have '$evTitle' coming up while today is planned as a $activeDayType. Want 1 light 15-min prep step early so the rest of your day stays 100% free?"
        } else null

        if (isLightOrSocialDay && todaySpecialPlan != null && conflictMessage == null) {
            // Respect REST / FAMILY / SOCIAL day: recommend only 1 light optional step or guilt-free rest
            val lightAction = todaySpecialPlan.customFocusNote.ifBlank {
                "Finish 1 small 15-min revision early so the rest of your $activeDayType stays completely free."
            }
            NowRecommendation(
                title = activeDayType,
                subtitleShort = "Light rhythm • Protect your time & energy today.",
                humanHeadline = "Today is planned as a $activeDayType — keep your workload gentle and guilt-free.",
                concreteAction = lightAction,
                whyReason = "Respecting your planned $activeDayType prevents burnout and keeps you present.",
                durationMinutes = 15,
                currentTimeFormatted = timeFmt,
                contextualLine = "Today is a $activeDayType. Be present.",
                categoryBadge = "MY DAY, MY WAY",
                energyLevel = energy,
                availableMinutes = 15,
                priorityDomain = "WELLNESS",
                subjectOrProject = activeDayType,
                alternateOptions = listOf(
                    "15-min light formula review",
                    "10-min mobility & stretch",
                    "Close Today Gently reflection"
                ),
                selectedDayType = activeDayType,
                conflictWarningMessage = null
            )
        } else if (orderedPool.isEmpty()) {
            NowRecommendation(
                title = "Krow",
                subtitleShort = "Get one piece of user feedback.",
                humanHeadline = "Your Krow prototype is moving forward, and 1 user note unlocks validation.",
                concreteAction = "Send 1 short feedback question or observe 1 user testing the Krow flow.",
                whyReason = "High-impact project validation step.",
                durationMinutes = 20,
                currentTimeFormatted = timeFmt,
                contextualLine = contextualLine,
                categoryBadge = "YOUR NEXT MOVE",
                energyLevel = energy,
                availableMinutes = 20,
                priorityDomain = "PROJECT",
                subjectOrProject = "Krow",
                selectedDayType = activeDayType,
                conflictWarningMessage = conflictMessage
            )
        } else {
            // Boost candidates matching today's Special Day Type if set
            val prioritizedPool = when (activeDayType) {
                "PROJECT DAY" -> orderedPool.sortedByDescending { it.priorityDomain == "PROJECT" }
                "COMPETITION PREPARATION DAY" -> orderedPool.sortedByDescending {
                    it.title.contains("NCSC", true) || it.title.contains("EcoDrain", true)
                }
                "REVISION DAY" -> orderedPool.sortedByDescending {
                    it.title.contains("Error", true) || it.priorityDomain == "ACADEMIC"
                }
                else -> orderedPool
            }
            val selectedIndex = (rotation.coerceAtLeast(0)) % prioritizedPool.size
            val chosen = prioritizedPool[selectedIndex]
            val dynamicAlternates = prioritizedPool
                .filter { it.title != chosen.title }
                .take(3)
                .map { "${it.title} • ${it.durationMinutes} min" }
                .ifEmpty { chosen.alternateOptions }
            chosen.copy(
                alternateOptions = dynamicAlternates,
                selectedDayType = activeDayType,
                conflictWarningMessage = conflictMessage
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NowRecommendation(
            title = "Krow",
            subtitleShort = "Get one piece of user feedback.",
            humanHeadline = "Your Krow prototype is moving forward, and 1 user note will unlock validation.",
            concreteAction = "Ask 1 student or shopkeeper to walk through your Krow screen and note 1 friction point.",
            whyReason = "Directly advances your October Monthly Goal for Krow validation.",
            durationMinutes = 20,
            currentTimeFormatted = "3:30 PM",
            contextualLine = "Your day is still yours.",
            categoryBadge = "YOUR NEXT MOVE",
            energyLevel = "NORMAL",
            availableMinutes = 20,
            priorityDomain = "PROJECT",
            subjectOrProject = "Krow"
        )
    )

    val weeklyStats: StateFlow<WeeklyStatsUi> = combine(
        progressHistory,
        confidenceEntries,
        habits
    ) { history, entries, currentHabits ->
        if (history.isEmpty()) {
            WeeklyStatsUi()
        } else {
            val moveDays = history.count { it.movementCompleted }.coerceIn(0, 5)
            val hydrationCount = (history.sumOf { it.hydrationCompletedCount } +
                if (currentHabits.any { it.category == "HYDRATE" && it.completedToday }) 1 else 0)
                .coerceIn(0, 10)
            val skincareCount = history.sumOf {
                (if (it.morningSkincareCompleted) 1 else 0) + (if (it.eveningSkincareCompleted) 1 else 0)
            }.coerceIn(0, 10)
            val sleepDays = history.count { it.sleepWindDownCompleted }.coerceIn(0, 7)
            val confidenceDays = maxOf(
                history.count { it.confidenceCheckInCompleted },
                entries.size
            ).coerceIn(0, 7)

            val ratios = listOf(
                "✨ Self-Care & Grooming" to (skincareCount / 10f),
                "🧘 Movement & Mobility" to (moveDays / 5f),
                "😴 Sleep & Wind-Down" to (sleepDays / 7f),
                "💧 Hydration" to (hydrationCount / 10f),
                "🌸 Confidence" to (confidenceDays / 7f)
            )
            val strongest = ratios.maxByOrNull { it.second }?.first ?: "✨ Self-Care & Grooming"
            val latestProud = entries.firstOrNull { it.proudOfText.isNotBlank() }?.proudOfText
                ?: "Stayed consistent with Physics numericals, Krow prototype, and evening wind-down."

            WeeklyStatsUi(
                movementCompletedDays = moveDays,
                movementTargetDays = 5,
                hydrationCompletedCount = hydrationCount,
                hydrationTargetCount = 10,
                skincareCompletedCount = skincareCount,
                skincareTargetCount = 10,
                sleepCompletedDays = sleepDays,
                sleepTargetDays = 7,
                confidenceCompletedDays = confidenceDays,
                confidenceTargetDays = 7,
                strongestHabitTitle = strongest,
                latestReflectionText = latestProud
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeeklyStatsUi())

    init {
        GlowUpNotificationHelper.createNotificationChannel(appContext)
        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
            refreshMorningDecisionPlan()
            resyncAllReminders()
        }
    }

    fun selectTab(tab: BottomNavTab) {
        _activeSubScreen.value = ActiveSubScreen.None
        _selectedTab.value = tab
    }

    fun openSubScreen(subScreen: ActiveSubScreen) {
        _activeSubScreen.value = subScreen
    }

    fun closeSubScreen() {
        _activeSubScreen.value = ActiveSubScreen.None
    }

    fun clearBannerMessage() {
        _statusBannerMessage.value = null
    }

    fun showGentleMessage(msg: String) {
        _statusBannerMessage.value = msg
    }

    fun getDynamicGreeting(prefs: UserPreferencesEntity): String {
        if (prefs.customGreeting.isNotBlank()) {
            return prefs.customGreeting
        }
        val name = prefs.userName.ifBlank { "Astha" }
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 4..11 -> "Good morning, $name"
            in 12..16 -> "Good afternoon, $name"
            else -> "Good evening, $name"
        }
    }

    fun getFormattedTodayDate(): String {
        val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.US)
        return sdf.format(Date())
    }

    fun setEnergyLevel(level: String) {
        updatePreferences { it.copy(currentEnergyLevel = level) }
        _statusBannerMessage.value = "Energy set to $level — recommendations recalculated."
    }

    fun setDayMode(mode: String) {
        updatePreferences { it.copy(currentDayMode = mode) }
    }

    // "I HAVE X MINUTES" Universal Calculator
    fun calculateActionForMinutes(minutes: Int): String {
        val unresolvedErrors = errorLogs.value.filter { !it.isResolved }
        val topChapter = studyChapters.value.firstOrNull()
        val topProject = projects.value.firstOrNull()
        val topAcademic = osTasks.value.firstOrNull { it.domain == "ACADEMIC" && !it.isCompleted }

        return when {
            minutes <= 5 -> "5-Minute High-Value Mission:\n• Active recall 3 key formulas from ${topChapter?.chapterName ?: "Rotational Motion"} or do a 5-min Quick Posture Reset."
            minutes <= 10 -> "10-Minute High-Value Mission:\n• Solve 2 easy warm-up questions in ${topChapter?.subject ?: "Physics"} or complete a 10-minute normal mobility reset."
            minutes <= 15 -> if (unresolvedErrors.isNotEmpty()) {
                "15-Minute High-Value Mission:\n• Review ${unresolvedErrors.size.coerceAtMost(3)} Physics/Chemistry mistakes in your Error Log (starting with: ${unresolvedErrors.first().questionSummary.take(55)}...)."
            } else {
                "15-Minute High-Value Mission:\n• Practice your 90-second NCSC explanation for ${preferences.value.ncscProjectTitle}."
            }
            minutes <= 20 -> "20-Minute High-Value Mission:\n• Advance ${topProject?.name ?: "Krow"}: ${topProject?.nextConcreteAction ?: "Collect 3 pieces of user feedback"}."
            minutes <= 30 -> "30-Minute High-Value Mission:\n• Solve 5 targeted medium questions in ${topChapter?.subject ?: "Mathematics"} (${topChapter?.chapterName ?: "Current Chapter"})."
            minutes <= 45 -> "45-Minute High-Value Mission:\n• Complete your core study block: ${topAcademic?.title ?: "Physics 45-min Core Study Engine (Recall → Easy → Medium → Mistake Check)"}."
            else -> "${minutes}-Minute Deep Focus Mission:\n• Complete a full 45-min ${topChapter?.subject ?: "Physics"} practice block + 15-min Error Log review + 15-min ${topProject?.name ?: "Krow"} build step."
        }
    }

    // "I'M CONFUSED" Universal Converter -> ONE concrete next action
    fun resolveConfusionToSingleAction(confusionOption: String): Pair<String, ActiveSubScreen?> {
        val topChapter = studyChapters.value.firstOrNull()
        val topTask = osTasks.value.firstOrNull { !it.isCompleted }
        val topProj = projects.value.firstOrNull()

        return when (confusionOption) {
            "I don't know what to study" -> {
                val title = "${topChapter?.subject ?: "Physics"} — ${topChapter?.chapterName ?: "Rotational Motion"}"
                "ONE Concrete Next Action: Solve 2 easy + 3 medium questions in $title (25 min)." to
                    ActiveSubScreen.FocusMode(
                        taskTitle = title,
                        purpose = "Solve 2 easy warm-up questions, then 3 medium questions.",
                        durationMinutes = 25,
                        currentStepText = "Step 1: Write down key formulas from memory (3 min)"
                    )
            }
            "I don't know how to study" -> {
                "ONE Concrete Next Action: Follow the 5-step Core Study Loop — close notes, write 2 formulas from memory, and solve 1 worked example." to
                    ActiveSubScreen.FocusMode(
                        taskTitle = "${topChapter?.subject ?: "Physics"} Guided Study Loop",
                        purpose = "0-5m Active Recall → 5-15m Easy Set → 15-25m Medium Set",
                        durationMinutes = 25,
                        currentStepText = "0–5 min: Active recall on blank paper"
                    )
            }
            "I have too many tasks" -> {
                "ONE Concrete Next Action: Ignore everything except #1 on Today's Top 3 (${topTask?.title ?: "Physics practice"}). Postpone the rest using Recovery." to
                    ActiveSubScreen.RecoveryResetSheet
            }
            "I'm too tired" -> {
                setEnergyLevel("LOW")
                "ONE Concrete Next Action: Switched to LOW ENERGY mode. Take a 10-minute controlled power reset or do a 5-min gentle stretch." to
                    ActiveSubScreen.AfterSchoolResetSheet
            }
            "I'm distracted" -> {
                "ONE Concrete Next Action: Define a single 15-minute sprint on '${topTask?.title ?: "Review 2 Error Log mistakes"}' with your phone aside." to
                    ActiveSubScreen.FocusMode(
                        taskTitle = topTask?.title ?: "15-Min Focus Sprint",
                        purpose = "Just 15 minutes on one clear step — no multitasking.",
                        durationMinutes = 15,
                        currentStepText = "Find what is given and what is asked on Question 1"
                    )
            }
            "I don't understand the topic" -> {
                val ch = topChapter ?: StudyChapterEntity(
                    subject = "Physics",
                    chapterName = "Rotational Motion",
                    currentTopic = "Torque & Angular Momentum",
                    weakAreas = "Conservation of angular momentum"
                )
                "ONE Concrete Next Action: Use the I'm Stuck 5-level hint coach or capture 1 specific doubt in One-Shot Mode." to
                    ActiveSubScreen.OneShotSheet(ch)
            }
            else -> {
                "ONE Concrete Next Action: Start with a 5-minute setup for '${topProj?.name ?: "Physics practice"}' — just open your notebook and write the heading." to
                    ActiveSubScreen.FocusMode(
                        taskTitle = topTask?.title ?: "5-Minute Starter Step",
                        purpose = "Just start for 5 minutes. Momentum comes after starting.",
                        durationMinutes = 10,
                        currentStepText = "Open notebook and complete Step 1"
                    )
            }
        }
    }

    // Start Focus Mode
    fun startFocusForRecommendation(rec: NowRecommendation) {
        _activeSubScreen.value = ActiveSubScreen.FocusMode(
            taskTitle = "${rec.title} (${rec.durationMinutes} min)",
            purpose = rec.concreteAction,
            durationMinutes = rec.durationMinutes,
            currentStepText = rec.subtitleShort.ifBlank { "Step 1: Recall key ideas → Core execution → Quick check" },
            totalSteps = 4,
            linkedTask = rec.taskToStart
        )
    }

    fun skipCurrentRecommendation(rec: NowRecommendation) {
        viewModelScope.launch {
            repository.skipTaskForNow(rec.taskToStart)
            _statusBannerMessage.value = "Switched to another smart recommendation for right now."
        }
    }

    fun startFocusForTask(task: OsTaskEntity) {
        _activeSubScreen.value = ActiveSubScreen.FocusMode(
            taskTitle = task.title,
            purpose = task.subtitle,
            durationMinutes = task.durationMinutes,
            currentStepText = "Focus on one concrete step at a time",
            totalSteps = 4,
            linkedTask = task
        )
    }

    fun completeFocusSession(
        linkedTask: OsTaskEntity?,
        taskTitle: String,
        durationMinutes: Int,
        focusRating: String,
        distractionSource: String
    ) {
        viewModelScope.launch {
            repository.markRecommendationCompleted(linkedTask, taskTitle.take(15))
            repository.logFocusSessionResult(
                taskTitle = taskTitle,
                focusRating = focusRating,
                distractionSource = distractionSource,
                durationMinutes = durationMinutes
            )
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "Focus block complete! Next recommendation updated."
        }
    }

    // Tasks
    fun addOsTask(
        title: String,
        subtitle: String,
        whyItMatters: String,
        domain: String,
        priorityBucket: String,
        durationMinutes: Int,
        energyRequired: String,
        scheduledTimeSlot: String,
        isTopThree: Boolean = false,
        topThreeSlot: Int = 0
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addTask(
                title = title,
                subtitle = subtitle,
                whyItMatters = whyItMatters,
                domain = domain,
                priorityBucket = priorityBucket,
                durationMinutes = durationMinutes,
                energyRequired = energyRequired,
                scheduledTimeSlot = scheduledTimeSlot,
                isTopThree = isTopThree,
                topThreeSlot = topThreeSlot
            )
            _statusBannerMessage.value = "Task added to your plan."
        }
    }

    fun toggleOsTask(task: OsTaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(task)
        }
    }

    fun postponeOsTask(task: OsTaskEntity) {
        viewModelScope.launch {
            repository.postponeTask(task)
            _statusBannerMessage.value = "Postponed '${task.title}' to tomorrow without guilt."
        }
    }

    fun rescheduleOsTask(task: OsTaskEntity, newSlot: String, newPriority: String) {
        viewModelScope.launch {
            repository.rescheduleTask(task, newSlot, newPriority)
            _statusBannerMessage.value = "Updated '${task.title}' ($newPriority • $newSlot)."
        }
    }

    fun deleteOsTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // Timeline & Recovery
    fun toggleTimelineBlock(block: TimelineBlockEntity) {
        viewModelScope.launch {
            repository.toggleTimelineBlockCompleted(block)
        }
    }

    fun addTimelineBlock(
        startTime: String,
        endTime: String,
        title: String,
        subtitle: String,
        blockType: String,
        recoveryCategory: String,
        durationMinutes: Int
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addTimelineBlock(
                startTime = startTime,
                endTime = endTime,
                title = title,
                subtitle = subtitle,
                blockType = blockType,
                recoveryCategory = recoveryCategory,
                durationMinutes = durationMinutes
            )
            _statusBannerMessage.value = "Added '$title' to today's timeline."
        }
    }

    fun triggerIntelligentRecovery() {
        viewModelScope.launch {
            repository.resetAndRecalculateRemainingDay(preferences.value.currentEnergyLevel)
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "Let's reset from here. Your remaining day has been recalculated."
        }
    }

    // Study Engine, One-Shot, Practice & Error Log
    fun saveOneShotCompletion(
        chapterId: Int,
        videoResource: String,
        purpose: String,
        capturedConcept: String,
        capturedFormula: String,
        capturedDoubt: String,
        understandingStatus: String
    ) {
        viewModelScope.launch {
            repository.saveOneShotCapture(
                chapterId = chapterId,
                videoResource = videoResource,
                purpose = purpose,
                capturedConcept = capturedConcept,
                capturedFormula = capturedFormula,
                capturedDoubt = capturedDoubt,
                understandingStatus = understandingStatus
            )
            _activeSubScreen.value = ActiveSubScreen.None
            val nextAdvice = when (understandingStatus) {
                "GREEN" -> "GREEN: Great! Move straight to 5 medium practice questions."
                "YELLOW" -> "YELLOW: Start with 2 worked examples + 3 easy questions to build confidence."
                else -> "RED: Let's clarify '${capturedDoubt.ifBlank { "the core concept" }}' with ASTHA AI (no need to re-watch a full lecture)."
            }
            _statusBannerMessage.value = nextAdvice
        }
    }

    fun addStudyChapter(subject: String, chapterName: String, currentTopic: String, weakAreas: String) {
        if (chapterName.isBlank()) return
        viewModelScope.launch {
            repository.addStudyChapter(subject, chapterName, currentTopic, weakAreas)
            _statusBannerMessage.value = "Added chapter '$chapterName' ($subject)."
        }
    }

    fun updateChapter(chapter: StudyChapterEntity) {
        viewModelScope.launch {
            repository.updateStudyChapter(chapter)
            _statusBannerMessage.value = "Saved study material for ${chapter.chapterName}."
        }
    }

    fun recordPracticeQuestionOutcome(
        question: PracticeQuestionItem,
        outcome: String, // SOLVED, PARTIAL, WRONG, SKIPPED
        logToErrorBook: Boolean = false,
        whyMistake: String = ""
    ) {
        viewModelScope.launch {
            if (outcome == "SKIPPED") {
                _skipOrEscapeCount.value += 1
            } else if (outcome == "SOLVED") {
                _skipOrEscapeCount.value = 0
            }
            repository.recordQuestionOutcome(
                subject = question.subject,
                outcome = outcome,
                weaknessDetected = if (outcome == "WRONG" || outcome == "PARTIAL") question.focusSkill else null
            )
            if (logToErrorBook || outcome == "WRONG") {
                repository.addErrorLog(
                    subject = question.subject,
                    chapter = question.chapter,
                    questionSummary = question.questionText,
                    mistakeType = question.focusSkill,
                    whyIMadeIt = whyMistake.ifBlank { "Needed hint on ${question.focusSkill.lowercase()}" },
                    correctIdea = question.hint2Formula,
                    source = question.source
                )
            }
        }
    }

    fun addErrorLogEntry(
        subject: String,
        chapter: String,
        questionSummary: String,
        mistakeType: String,
        whyIMadeIt: String,
        correctIdea: String,
        source: String
    ) {
        if (questionSummary.isBlank()) return
        viewModelScope.launch {
            repository.addErrorLog(
                subject = subject,
                chapter = chapter,
                questionSummary = questionSummary,
                mistakeType = mistakeType,
                whyIMadeIt = whyIMadeIt,
                correctIdea = correctIdea,
                source = source
            )
            _statusBannerMessage.value = "Mistake logged! Scheduled for 24h → 3d → 7d spaced review."
        }
    }

    fun reviewErrorLogEntry(entry: ErrorLogEntity) {
        viewModelScope.launch {
            repository.advanceErrorLogReview(entry)
            _statusBannerMessage.value = "Reviewed! Moved to next spaced-repetition stage."
        }
    }

    // BUILD: Projects
    fun updateProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.updateProject(project)
            _statusBannerMessage.value = "Updated ${project.name} (Evidence: ${project.evidenceStrengthLabel})."
        }
    }

    fun addProject(
        name: String,
        tagline: String,
        problemStatement: String,
        targetUser: String,
        solutionSummary: String,
        technologyStack: String,
        nextConcreteAction: String
    ) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addProject(
                name = name,
                tagline = tagline,
                problemStatement = problemStatement,
                targetUser = targetUser,
                solutionSummary = solutionSummary,
                technologyStack = technologyStack,
                nextConcreteAction = nextConcreteAction
            )
            _statusBannerMessage.value = "Project '$name' created in BUILD."
        }
    }

    // PROFILE: Achievements & Opportunities
    fun addProfileAchievement(
        category: String,
        title: String,
        dateString: String,
        organization: String,
        role: String,
        whatIDid: String,
        resultSummary: String,
        impactSummary: String,
        evidenceNote: String,
        linkUrl: String
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addProfileAchievement(
                category = category,
                title = title,
                dateString = dateString,
                organization = organization,
                role = role,
                whatIDid = whatIDid,
                resultSummary = resultSummary,
                impactSummary = impactSummary,
                evidenceNote = evidenceNote,
                linkUrl = linkUrl
            )
            _statusBannerMessage.value = "Added '$title' to your Profile portfolio."
        }
    }

    fun deleteProfileAchievement(id: Int) {
        viewModelScope.launch {
            repository.deleteProfileAchievement(id)
        }
    }

    fun addOpportunity(
        name: String,
        category: String,
        country: String,
        organization: String,
        eligibility: String,
        deadline: String,
        funding: String,
        applicationRequirements: String,
        officialSource: String,
        status: String,
        notes: String,
        isVerified: Boolean
    ) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addOpportunity(
                name = name,
                category = category,
                country = country,
                organization = organization,
                eligibility = eligibility,
                deadline = deadline,
                funding = funding,
                applicationRequirements = applicationRequirements,
                officialSource = officialSource,
                status = status,
                notes = notes,
                isVerified = isVerified
            )
            _statusBannerMessage.value = "Saved opportunity '$name' ($status)."
        }
    }

    fun cycleOpportunityStatus(opportunity: OpportunityEntity) {
        val statuses = listOf("Saved", "Researching", "Eligible", "Preparing", "Submitted", "Result")
        val nextIdx = (statuses.indexOf(opportunity.status) + 1) % statuses.size
        val nextStatus = statuses[nextIdx]
        viewModelScope.launch {
            repository.updateOpportunity(opportunity.copy(status = nextStatus))
            _statusBannerMessage.value = "${opportunity.name} status → $nextStatus"
        }
    }

    fun verifyOpportunity(opportunity: OpportunityEntity) {
        viewModelScope.launch {
            repository.updateOpportunity(
                opportunity.copy(
                    isVerified = true,
                    lastVerifiedDate = "Verified on ${repository.todayDateString()}"
                )
            )
            _statusBannerMessage.value = "Marked '${opportunity.name}' as verified."
        }
    }

    // Night Brain Dump
    fun saveBrainDumpThought(content: String, category: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.saveBrainDump(content, category)
            _statusBannerMessage.value = "Nothing else needs to be solved tonight. Thought organized into $category."
        }
    }

    // ASTHA AI Assistant
    fun sendPromptToAsthaAi(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = AiChatMessage(id = System.currentTimeMillis(), isUser = true, text = prompt.trim())
        _aiMessages.value = _aiMessages.value + userMsg
        _isAiLoading.value = true

        viewModelScope.launch {
            val reply = AsthaAiEngine.askAsthaAi(
                userPrompt = prompt,
                prefs = preferences.value,
                tasks = osTasks.value,
                chapters = studyChapters.value,
                errors = errorLogs.value,
                projects = projects.value,
                opportunities = opportunities.value
            )
            _aiMessages.value = _aiMessages.value + AiChatMessage(
                id = System.currentTimeMillis() + 1,
                isUser = false,
                text = reply
            )
            _isAiLoading.value = false
        }
    }

    fun generateDailyRoutine(prefs: UserPreferencesEntity): List<GeneratedRoutineBlock> {
        val morningItems = listOf(
            "${prefs.morningStudyStartTime}–${prefs.morningStudyEndTime}: Morning Focus Block (75 min)",
            "• Choose 1 specific objective (active recall, fresh numericals, or mistake review)",
            "• 10-minute fallback available if waking late—never turn a missed morning into failure",
            "• Drink water, light stretch, and leave for school at ${prefs.schoolStartTime}"
        )
        val schoolItems = listOf(
            "${prefs.schoolStartTime}–${prefs.schoolEndTime}: School Hours",
            "• Attend classes and labs; use short 5–15m free intervals for lightweight formula recall",
            "• Socialize with friends and keep hydration steady; no coaching scheduled"
        )
        val resetItems = listOf(
            "${prefs.afterSchoolResetStartTime}–${prefs.afterSchoolResetEndTime}: After-School Reset (No intense study immediately)",
            "• Eat lunch & freshen up",
            "• 20-min power rest or gentle nap with timer",
            "• Deliberate anime episode or light movement without guilt"
        )
        val eveningItems = when (prefs.eveningPlanMode) {
            "SHORT" -> listOf(
                "${prefs.eveningStudyStartTime}–${prefs.eveningStudyEndTime}: Short Focused Evening Plan (Core Essentials)",
                "• 4:00–5:15 PM: Mission Jeet #1 Priority Lesson (active watching & notes)",
                "• 5:15–5:30 PM: Posture & hydration pause",
                "• 5:30–6:30 PM: 5–8 targeted questions & mistake log",
                "• 6:30–7:15 PM: Science project (Krow / EcoDrain) or second subject",
                "• 8:15–9:00 PM: Family dinner & relaxed conversation"
            )
            "LOW_ENERGY" -> listOf(
                "${prefs.eveningStudyStartTime}–${prefs.eveningStudyEndTime}: Low-Energy Recovery Evening Plan (Gentle & Sustainable)",
                "• 4:00–4:45 PM: Light concept review or one-shot summary (no heavy derivations)",
                "• 4:45–5:30 PM: Walk outside or creative sketching/drawing",
                "• 5:30–6:15 PM: Error Log review (read past mistakes without solving from scratch)",
                "• 6:15–7:00 PM: Casual prototype reading or creative exploration",
                "• 8:00–9:00 PM: Nourishing dinner, family time, and early wind-down"
            )
            else -> listOf(
                "${prefs.eveningStudyStartTime}–${prefs.eveningStudyEndTime}: Standard Evening Plan (Balanced Academic & Project Progress)",
                "• 4:00–5:15 PM: Mission Jeet Lecture & Core Concepts (active chunking)",
                "• 5:15–5:35 PM: Movement, water & desk reset",
                "• 5:35–6:45 PM: Independent question practice & mistake logging",
                "• 6:45–7:35 PM: Project / Build focus (Krow / Smart EcoDrain Shield)",
                "• 7:35–8:15 PM: School subjects revision (Chemistry NCERT / CS)",
                "• 8:15–9:00 PM: Dinner, family connection & relaxed transition"
            )
        }
        val nightItems = listOf(
            "9:00–9:40 PM: Hobbies & Joy (Drawing, anime, or personal reading)",
            "${prefs.windDownTime}: 2-Minute Guilt-Free Closure & Tomorrow's First Step",
            "${prefs.targetBedtime}: Sleep for full recovery"
        )

        return listOf(
            GeneratedRoutineBlock("Morning Study (6:00–7:15 AM)", "🌅", morningItems),
            GeneratedRoutineBlock("School (7:15 AM–2:30 PM)", "🏫", schoolItems),
            GeneratedRoutineBlock("After-School Reset (2:30–4:00 PM)", "🍃", resetItems),
            GeneratedRoutineBlock("Evening Study & Goals (4:00–9:00 PM)", "📚", eveningItems),
            GeneratedRoutineBlock("Night & Closure (9:00–10:15 PM)", "🌙", nightItems)
        )
    }

    fun setEveningPlanMode(mode: String) {
        viewModelScope.launch {
            val current = preferences.value
            repository.saveUserPreferences(current.copy(eveningPlanMode = mode))
            _statusBannerMessage.value = "Evening plan updated to: $mode Plan"
        }
    }

    fun recalculateEvening(mode: String, availableMinutes: Int) {
        viewModelScope.launch {
            val current = preferences.value
            repository.saveUserPreferences(current.copy(eveningPlanMode = mode))
            // Adjust timeline blocks for today if needed
            val blocks = repository.dao.getTimelineBlocksOnce()
            if (blocks.isNotEmpty()) {
                val updated = blocks.map { b ->
                    if (b.title.contains("Evening", ignoreCase = true) || b.blockType == "FOCUS") {
                        b.copy(subtitle = "Adjusted for $mode plan ($availableMinutes min available)")
                    } else b
                }
                repository.dao.insertTimelineBlocks(updated)
            }
            _statusBannerMessage.value = "Recalculated evening calmly: $mode Plan ($availableMinutes min available)"
        }
    }

    // Mission Jeet Management
    fun saveMissionJeetLecture(lecture: MissionJeetLectureEntity) {
        viewModelScope.launch {
            repository.saveMissionJeetLecture(lecture)
            _statusBannerMessage.value = "Saved Mission Jeet topic: ${lecture.topic}"
        }
    }

    fun updateMissionJeetLecture(lecture: MissionJeetLectureEntity) {
        viewModelScope.launch {
            repository.updateMissionJeetLecture(lecture.copy(lastUpdatedTimestamp = System.currentTimeMillis()))
            _statusBannerMessage.value = "Updated progress for: ${lecture.topic}"
        }
    }

    fun deleteMissionJeetLecture(id: Int) {
        viewModelScope.launch {
            repository.deleteMissionJeetLecture(id)
            _statusBannerMessage.value = "Removed Mission Jeet lecture."
        }
    }

    // Hobbies & Joy Management
    fun saveHobby(hobby: HobbyEntity) {
        viewModelScope.launch {
            repository.saveHobby(hobby)
            _statusBannerMessage.value = "Added hobby: ${hobby.name}"
        }
    }

    fun updateHobby(hobby: HobbyEntity) {
        viewModelScope.launch {
            repository.updateHobby(hobby)
        }
    }

    fun deleteHobby(id: Int) {
        viewModelScope.launch {
            repository.deleteHobby(id)
            _statusBannerMessage.value = "Removed hobby."
        }
    }

    fun scheduleHobbyOnCalendar(hobby: HobbyEntity) {
        viewModelScope.launch {
            val todayStr = repository.todayDateString()
            val event = CalendarEventEntity(
                title = hobby.name,
                dateString = todayStr,
                endDateString = todayStr,
                startTime = "9:00 PM",
                endTime = "9:35 PM",
                category = when (hobby.category) {
                    "SOCIAL" -> "FAMILY TIME"
                    "PHYSICAL" -> "WELLNESS"
                    else -> "PERSONAL"
                },
                priority = "MEDIUM",
                location = "Relaxation Space",
                locationOrNotes = "${hobby.description} • Guilt-free recharge.",
                reminderPreference = "On event day",
                attendanceOrCompletionStatus = "PLANNED"
            )
            repository.dao.insertCalendarEvent(event)
            repository.updateHobby(hobby.copy(isScheduledOnCalendar = true, lastEnjoyedDate = "Today"))
            _statusBannerMessage.value = "Scheduled \"${hobby.name}\" on your calendar for tonight at 9:00 PM ✨"
        }
    }

    // Existing Wellness methods preserved seamlessly
    fun completeOnboarding(
        userName: String,
        focusAreas: Set<String>,
        freeTime: String,
        durationMinutes: Int
    ) {
        viewModelScope.launch {
            repository.completeOnboarding(userName, focusAreas, freeTime, durationMinutes)
            scheduleConfiguredReminders()
            _statusBannerMessage.value = "ASTHA OS is ready, ${userName.ifBlank { "Astha" }} ✨"
        }
    }

    fun toggleHabit(habit: DailyHabitEntity) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habit)
        }
    }

    fun addCustomHabit(title: String, subtitle: String, emoji: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addCustomHabit(title, subtitle, emoji)
            _statusBannerMessage.value = "Added \"${title.trim()}\" to your wellness habits."
        }
    }

    fun deleteHabit(id: Int) {
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
    }

    fun startWorkoutRoutine(routine: MovementRoutine) {
        _activeSubScreen.value = ActiveSubScreen.ActiveWorkoutTimer(routine)
    }

    fun finishWorkoutRoutine(routine: MovementRoutine) {
        viewModelScope.launch {
            repository.markMovementRoutineCompleted(routine.title, routine.durationMinutes)
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "${routine.title} completed! 🧘"
        }
    }

    fun toggleSkincareStep(step: SkincareStepEntity) {
        viewModelScope.launch {
            repository.toggleSkincareStep(step)
        }
    }

    fun addSkincareStep(period: String, title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addSkincareStep(period, title)
            _statusBannerMessage.value = "Added step to your ${period.lowercase()} self-care ✨"
        }
    }

    fun deleteSkincareStep(id: Int) {
        viewModelScope.launch {
            repository.deleteSkincareStep(id)
        }
    }

    fun toggleHydrationReminderEnabled(reminder: HydrationReminderEntity) {
        viewModelScope.launch {
            repository.toggleHydrationReminderEnabled(reminder)
            scheduleConfiguredReminders()
        }
    }

    fun toggleHydrationReminderCompleted(reminder: HydrationReminderEntity) {
        viewModelScope.launch {
            repository.toggleHydrationReminderCompleted(reminder)
        }
    }

    fun addHydrationReminder(timeLabel: String, hour24: Int, minute: Int) {
        viewModelScope.launch {
            repository.addHydrationReminder(timeLabel, hour24, minute)
            scheduleConfiguredReminders()
            _statusBannerMessage.value = "Hydration reminder added for $timeLabel 💧"
        }
    }

    fun deleteHydrationReminder(id: Int) {
        viewModelScope.launch {
            repository.deleteHydrationReminder(id)
        }
    }

    fun updatePreferences(transform: (UserPreferencesEntity) -> UserPreferencesEntity) {
        viewModelScope.launch {
            val updated = transform(preferences.value)
            repository.savePreferences(updated)
            scheduleConfiguredReminders()
        }
    }

    fun saveConfidenceCheckIn(
        moodEmoji: String,
        moodLabel: String,
        proudOfText: String,
        helpedFeelGoodText: String
    ) {
        viewModelScope.launch {
            repository.saveConfidenceEntry(moodEmoji, moodLabel, proudOfText, helpedFeelGoodText)
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "Confidence check-in saved 🌸"
        }
    }

    fun markSleepWindDownDone() {
        viewModelScope.launch {
            repository.markSleepWindDownCompleted()
            _statusBannerMessage.value = "Tomorrow is prepared. You can wind down 🌙"
        }
    }

    fun acknowledgeMissedBedtimeSupport() {
        viewModelScope.launch {
            repository.acknowledgeMissedBedtime()
            _statusBannerMessage.value = "Tonight didn't go as planned. That's okay. Tomorrow is another chance. 🌙"
        }
    }

    fun triggerSampleNotification(sample: GlowUpNotificationHelper.GentleNotificationSample) {
        GlowUpNotificationHelper.showNotification(
            context = appContext,
            notificationId = sample.id,
            title = sample.title,
            message = sample.body
        )
        _statusBannerMessage.value = "${sample.title} — ${sample.body}"
    }

    private suspend fun scheduleConfiguredReminders() {
        val prefs = preferences.value
        if (!prefs.notificationsEnabled || !prefs.hydrationRemindersEnabled || prefs.hydrationPaused) {
            return
        }
        hydrationReminders.value.filter { it.isEnabled }.forEach { reminder ->
            GlowUpNotificationHelper.scheduleDailyReminder(
                context = appContext,
                requestCode = 3000 + reminder.id,
                hour24 = reminder.hour24,
                minute = reminder.minute,
                title = "💧 Hydration break",
                message = "Take a moment for some water."
            )
        }
    }

    fun resetTodaysHabits() {
        viewModelScope.launch {
            repository.resetTodaysHabits()
            _statusBannerMessage.value = "Let's reset from here. Today's habits have been refreshed."
        }
    }

    fun exportUserData() {
        viewModelScope.launch {
            _exportSummaryDialogText.value = repository.buildExportDataSummary()
        }
    }

    fun dismissExportDialog() {
        _exportSummaryDialogText.value = null
    }

    fun deleteAllUserData() {
        viewModelScope.launch {
            repository.deleteAllUserData()
            _selectedTab.value = BottomNavTab.TODAY
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "All personal data has been reset to fresh defaults."
        }
    }

    // Calendar & Time Horizons actions
    fun addCalendarEvent(
        title: String,
        dateString: String,
        endDateString: String = "",
        startTime: String,
        endTime: String,
        isAllDay: Boolean = false,
        category: String,
        priority: String,
        location: String = "",
        notes: String,
        repeatRule: String = "None",
        reminderEnabled: Boolean = true,
        reminderPreference: String = "Day before & 1 hour before",
        linkedProjectOrGoal: String = "",
        beforeEventChecklistCsv: String = ""
    ) {
        viewModelScope.launch {
            repository.addCalendarEvent(
                title = title,
                dateString = dateString,
                endDateString = endDateString,
                startTime = startTime,
                endTime = endTime,
                isAllDay = isAllDay,
                category = category,
                priority = priority,
                location = location,
                locationOrNotes = notes,
                repeatRule = repeatRule,
                reminderEnabled = reminderEnabled,
                reminderPreference = reminderPreference,
                linkedProjectOrGoal = linkedProjectOrGoal,
                beforeEventChecklistCsv = beforeEventChecklistCsv
            )
            if (reminderEnabled && preferences.value.notificationsEnabled) {
                val (hour24, min) = parseTimeHourAndMinute(startTime)
                GlowUpNotificationHelper.scheduleCalendarEventReminder(
                    context = appContext,
                    eventId = title.hashCode() and 0x7FFF,
                    eventTitle = title,
                    dateString = dateString,
                    hour24 = hour24,
                    minute = min,
                    preference = reminderPreference
                )
            }
            val spanInfo = if (endDateString.isNotBlank() && endDateString != dateString) {
                " ($dateString → $endDateString)"
            } else ""
            _statusBannerMessage.value = "Added '$title'$spanInfo to your Life Calendar."
        }
    }

    fun updateCalendarEvent(event: CalendarEventEntity) {
        viewModelScope.launch {
            repository.updateCalendarEvent(event)
            val eventId = event.id.takeIf { it > 0 } ?: (event.title.hashCode() and 0x7FFF)
            if (event.reminderEnabled && preferences.value.notificationsEnabled) {
                val (hour24, min) = parseTimeHourAndMinute(event.startTime)
                GlowUpNotificationHelper.scheduleCalendarEventReminder(
                    context = appContext,
                    eventId = eventId,
                    eventTitle = event.title,
                    dateString = event.dateString,
                    hour24 = hour24,
                    minute = min,
                    preference = event.reminderPreference
                )
            } else {
                GlowUpNotificationHelper.cancelReminder(appContext, 40000 + (eventId and 0x7FFF))
                GlowUpNotificationHelper.cancelReminder(appContext, 45000 + (eventId and 0x7FFF))
            }
            _statusBannerMessage.value = "Updated '${event.title}'."
        }
    }

    fun saveSpecialDayPlan(
        dateString: String,
        dayType: String,
        customFocusNote: String,
        customStructureItemsCsv: String,
        conflictOverrideApproved: Boolean = false
    ) {
        viewModelScope.launch {
            repository.saveSpecialDayPlan(
                dateString = dateString,
                dayType = dayType,
                customFocusNote = customFocusNote,
                customStructureItemsCsv = customStructureItemsCsv,
                conflictOverrideApproved = conflictOverrideApproved
            )
            // Bump rotation so recommendation engine immediately reflects the new day plan
            val curr = preferences.value
            repository.savePreferences(
                curr.copy(recommendationRotationCounter = curr.recommendationRotationCounter + 1)
            )
            _statusBannerMessage.value = "Planned $dateString as $dayType (My Day, My Way)."
        }
    }

    fun deleteSpecialDayPlan(dateString: String) {
        viewModelScope.launch {
            repository.deleteSpecialDayPlan(dateString)
            _statusBannerMessage.value = "Reset special day plan for $dateString."
        }
    }

    fun handleEventPrepMilestoneAction(
        milestone: EventPrepMilestoneEntity,
        action: String
    ) {
        viewModelScope.launch {
            repository.handleEventPrepMilestoneAction(milestone, action)
            val msg = when (action) {
                "ACCEPT" -> "Added '${milestone.stepTitle}' to Today's Tasks (no duplicates)."
                "POSTPONE" -> "Postponed '${milestone.stepTitle}'."
                "COMPLETE" -> "Completed prep step: ${milestone.stepTitle}!"
                else -> "Dismissed suggestion."
            }
            _statusBannerMessage.value = msg
        }
    }

    fun saveEndOfDayReflection(
        dateString: String = "",
        meaningfulToday: String,
        learnedOrExperienced: String,
        gladMadeTimeFor: String,
        carryIntoTomorrow: String,
        letGoOfForNow: String,
        quickTagSelected: String,
        tomorrowFirstStep: String
    ) {
        viewModelScope.launch {
            repository.saveEndOfDayReflection(
                dateString = dateString,
                meaningfulToday = meaningfulToday,
                learnedOrExperienced = learnedOrExperienced,
                gladMadeTimeFor = gladMadeTimeFor,
                carryIntoTomorrow = carryIntoTomorrow,
                letGoOfForNow = letGoOfForNow,
                quickTagSelected = quickTagSelected,
                tomorrowFirstStep = tomorrowFirstStep
            )
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "Today is closed gently. Rest well, Astha."
        }
    }

    fun handleUnfinishedTaskEndOfDay(task: OsTaskEntity, decision: String) {
        viewModelScope.launch {
            repository.handleUnfinishedTaskEndOfDay(task, decision)
            _statusBannerMessage.value = "'${task.title}' → $decision"
        }
    }

    fun saveWeeklyPlan(
        mon: String,
        tue: String,
        wed: String,
        thu: String,
        fri: String,
        sat: String,
        sun: String
    ) {
        updatePreferences {
            it.copy(
                weeklyPlanMon = mon,
                weeklyPlanTue = tue,
                weeklyPlanWed = wed,
                weeklyPlanThu = thu,
                weeklyPlanFri = fri,
                weeklyPlanSat = sat,
                weeklyPlanSun = sun
            )
        }
        _statusBannerMessage.value = "Weekly balance plan saved!"
    }

    fun deleteCalendarEvent(id: Int) {
        viewModelScope.launch {
            GlowUpNotificationHelper.cancelReminder(appContext, 40000 + (id and 0x7FFF))
            GlowUpNotificationHelper.cancelReminder(appContext, 45000 + (id and 0x7FFF))
            repository.deleteCalendarEvent(id)
            _statusBannerMessage.value = "Event deleted."
        }
    }

    fun parseTimeHourAndMinute(timeStr: String): Pair<Int, Int> {
        val clean = timeStr.trim()
        if (clean.isBlank()) return 9 to 0
        return try {
            if (clean.contains("AM", ignoreCase = true) || clean.contains("PM", ignoreCase = true)) {
                val isPm = clean.contains("PM", ignoreCase = true)
                val withoutAmPm = clean.replace("AM", "", ignoreCase = true).replace("PM", "", ignoreCase = true).trim()
                val parts = withoutAmPm.split(":")
                var hour = parts[0].trim().toInt()
                val min = if (parts.size > 1) parts[1].trim().toInt() else 0
                if (isPm && hour < 12) hour += 12
                if (!isPm && hour == 12) hour = 0
                hour to min
            } else {
                val parts = clean.split(":")
                val hour = parts[0].trim().toInt()
                val min = if (parts.size > 1) parts[1].trim().toInt() else 0
                hour to min
            }
        } catch (_: Exception) {
            9 to 0
        }
    }

    fun resyncAllReminders() {
        viewModelScope.launch {
            val prefs = repository.userPreferences.firstOrNull() ?: UserPreferencesEntity()
            if (!prefs.notificationsEnabled) return@launch

            // Schedule daily morning (6:00 AM) and evening check-ins (9:30 PM) with AlarmClock to pierce Doze mode
            GlowUpNotificationHelper.scheduleDailyReminder(
                context = appContext,
                requestCode = 9101,
                hour24 = 6,
                minute = 0,
                title = "Good morning, Astha",
                message = "Know what matters today. Start with one gentle step."
            )
            GlowUpNotificationHelper.scheduleDailyReminder(
                context = appContext,
                requestCode = 9102,
                hour24 = 21,
                minute = 30,
                title = "Peaceful Evening Close",
                message = "Reflect gently on what you completed and rest well tonight."
            )

            // Resync all scheduled calendar events
            val events = repository.calendarEvents.firstOrNull() ?: emptyList()
            events.filter { it.reminderEnabled }.forEach { ev ->
                val eventId = ev.id.takeIf { it > 0 } ?: (ev.title.hashCode() and 0x7FFF)
                val (hour, min) = parseTimeHourAndMinute(ev.startTime)
                GlowUpNotificationHelper.scheduleCalendarEventReminder(
                    context = appContext,
                    eventId = eventId,
                    eventTitle = ev.title,
                    dateString = ev.dateString,
                    hour24 = hour,
                    minute = min,
                    preference = ev.reminderPreference
                )
            }
        }
    }

    fun triggerTestReminder(delaySeconds: Int = 5) {
        GlowUpNotificationHelper.scheduleInstantTestReminder(appContext, delaySeconds)
        _statusBannerMessage.value = "Test alarm scheduled for in $delaySeconds seconds."
    }

    fun getReminderDiagnostics(): GlowUpNotificationHelper.ReminderDiagnostics {
        return GlowUpNotificationHelper.getDiagnostics(appContext)
    }

    fun acceptDeadlineSuggestion(suggestion: DeadlinePreparationSuggestion) {
        viewModelScope.launch {
            repository.acceptDeadlineSuggestion(suggestion)
            _statusBannerMessage.value = "Added '${suggestion.suggestedTaskTitle}' to Today's Plan."
        }
    }

    fun dismissDeadlineSuggestion(suggestion: DeadlinePreparationSuggestion) {
        viewModelScope.launch {
            repository.dismissDeadlineSuggestion(suggestion)
        }
    }

    fun addMonthlyGoal(domain: String, title: String, weeklyTarget: String, smallestStep: String) {
        viewModelScope.launch {
            repository.addMonthlyGoal(domain, title, weeklyTarget, smallestStep)
            _statusBannerMessage.value = "Monthly goal added for October."
        }
    }

    fun advanceMonthlyGoal(goal: MonthlyGoalEntity) {
        viewModelScope.launch {
            repository.advanceMonthlyGoalStep(goal)
            _statusBannerMessage.value = "Milestone recorded! '${goal.title}' moved forward."
        }
    }

    fun updateMonthlyGoal(goal: MonthlyGoalEntity) {
        viewModelScope.launch {
            repository.updateMonthlyGoal(goal)
        }
    }

    fun deleteMonthlyGoal(id: Int) {
        viewModelScope.launch {
            repository.deleteMonthlyGoal(id)
        }
    }

    fun updateLongTermGoal(goal: LongTermGoalEntity) {
        viewModelScope.launch {
            repository.updateLongTermGoal(goal)
            _statusBannerMessage.value = "Updated ${goal.area} goal hierarchy."
        }
    }

    fun saveMonthlyReview(
        whatBuilt: String,
        whatLearned: String,
        whatEvidence: String,
        whatContinue: String,
        whatStop: String
    ) {
        updatePreferences {
            it.copy(
                monthReviewWhatBuilt = whatBuilt,
                monthReviewWhatLearned = whatLearned,
                monthReviewWhatEvidence = whatEvidence,
                monthReviewContinue = whatContinue,
                monthReviewStop = whatStop
            )
        }
        _statusBannerMessage.value = "Monthly review & goal adjustments saved!"
    }

    // Setback Recovery ("Learn Without Losing Self-Respect")
    fun recordSetbackRecovery(
        eventTitle: String,
        category: String,
        whatActuallyHappened: String,
        howAmIFeeling: String,
        whatIsStillTrueAboutMe: String,
        whatWasOutOfMyControl: String,
        whatWasInMyControl: String,
        actionableLesson: String,
        gentleNextStep: String
    ) {
        viewModelScope.launch {
            repository.addSetbackRecovery(
                eventTitle = eventTitle,
                category = category,
                whatActuallyHappened = whatActuallyHappened,
                howAmIFeeling = howAmIFeeling,
                whatIsStillTrueAboutMe = whatIsStillTrueAboutMe,
                whatWasOutOfMyControl = whatWasOutOfMyControl,
                whatWasInMyControl = whatWasInMyControl,
                actionableLesson = actionableLesson,
                gentleNextStep = gentleNextStep
            )
            _statusBannerMessage.value = "Setback reframed with self-respect. Gentle next step recorded."
        }
    }

    fun toggleSetbackResolved(entry: SetbackRecoveryEntity) {
        viewModelScope.launch {
            repository.updateSetbackRecovery(entry.copy(isResolved = !entry.isResolved))
            _statusBannerMessage.value = if (!entry.isResolved) "Setback marked as integrated & resolved!" else "Setback reopened."
        }
    }

    fun deleteSetbackRecovery(id: Int) {
        viewModelScope.launch {
            repository.deleteSetbackRecovery(id)
        }
    }

    // Financial Experiments & Student Value Creation
    fun addFinancialExperiment(
        title: String,
        skillUsed: String,
        experimentStage: String,
        estimatedEarningPotential: String,
        actualEarned: Double,
        clientOrPlatform: String,
        whatLearnedAboutValue: String
    ) {
        viewModelScope.launch {
            repository.addFinancialExperiment(
                title = title,
                skillUsed = skillUsed,
                experimentStage = experimentStage,
                estimatedEarningPotential = estimatedEarningPotential,
                actualEarned = actualEarned,
                clientOrPlatform = clientOrPlatform,
                whatLearnedAboutValue = whatLearnedAboutValue
            )
            _statusBannerMessage.value = "Financial experiment logged! Keep building value."
        }
    }

    fun updateFinancialExperiment(entry: FinancialExperimentEntity) {
        viewModelScope.launch {
            repository.updateFinancialExperiment(entry)
            _statusBannerMessage.value = "Experiment updated."
        }
    }

    fun deleteFinancialExperiment(id: Int) {
        viewModelScope.launch {
            repository.deleteFinancialExperiment(id)
        }
    }

    // THE MORNING DECISION ENGINE
    fun refreshMorningDecisionPlan(minutesOverride: Int? = null, energyOverride: String? = null) {
        viewModelScope.launch {
            val plan = repository.generateMorningDecisionPlan(minutesOverride, energyOverride)
            _morningDecisionPlan.value = plan
        }
    }

    // Academic Topics & Chemistry Mission
    fun addAcademicTopic(topic: AcademicTopicEntity) {
        viewModelScope.launch {
            repository.addAcademicTopic(topic)
            _statusBannerMessage.value = "Added '${topic.topicName}' to ${topic.subject}."
            refreshMorningDecisionPlan()
        }
    }

    fun updateAcademicTopic(topic: AcademicTopicEntity) {
        viewModelScope.launch {
            repository.updateAcademicTopic(topic)
            _statusBannerMessage.value = "Updated topic '${topic.topicName}'."
            refreshMorningDecisionPlan()
        }
    }

    fun deleteAcademicTopic(id: Int) {
        viewModelScope.launch {
            repository.deleteAcademicTopic(id)
            refreshMorningDecisionPlan()
        }
    }

    // Science Exhibition Scorecards
    fun addScienceExhibitionScorecard(card: ScienceExhibitionScorecardEntity) {
        viewModelScope.launch {
            repository.addScienceExhibitionScorecard(card)
            _statusBannerMessage.value = "Exhibition idea '${card.ideaTitle}' saved to scorecard."
        }
    }

    fun updateScienceExhibitionScorecard(card: ScienceExhibitionScorecardEntity) {
        viewModelScope.launch {
            repository.updateScienceExhibitionScorecard(card)
        }
    }

    fun deleteScienceExhibitionScorecard(id: Int) {
        viewModelScope.launch {
            repository.deleteScienceExhibitionScorecard(id)
        }
    }

    fun selectProjectForExecution(id: Int) {
        viewModelScope.launch {
            repository.selectProjectForExecution(id)
            _statusBannerMessage.value = "Project selected for exhibition execution!"
        }
    }

    fun advanceExecutionMilestone(id: Int) {
        viewModelScope.launch {
            repository.advanceExecutionMilestone(id)
            _statusBannerMessage.value = "Advanced milestone progress."
        }
    }

    // Scholarship Strategies
    fun addScholarshipStrategy(strategy: ScholarshipStrategyEntity) {
        viewModelScope.launch {
            repository.addScholarshipStrategy(strategy)
            _statusBannerMessage.value = "Opportunity '${strategy.name}' logged."
        }
    }

    fun updateScholarshipStrategy(strategy: ScholarshipStrategyEntity) {
        viewModelScope.launch {
            repository.updateScholarshipStrategy(strategy)
        }
    }

    fun deleteScholarshipStrategy(id: Int) {
        viewModelScope.launch {
            repository.deleteScholarshipStrategy(id)
        }
    }

    // Scenario Plans
    fun addScenarioPlan(plan: ScenarioPlanEntity) {
        viewModelScope.launch {
            repository.addScenarioPlan(plan)
            _statusBannerMessage.value = "Scenario plan for '${plan.title}' saved."
        }
    }

    fun updateScenarioPlan(plan: ScenarioPlanEntity) {
        viewModelScope.launch {
            repository.updateScenarioPlan(plan)
        }
    }

    fun deleteScenarioPlan(id: Int) {
        viewModelScope.launch {
            repository.deleteScenarioPlan(id)
        }
    }

    // Profile Evidence Ledger
    fun addProfileEvidence(item: ProfileEvidenceLedgerEntity) {
        viewModelScope.launch {
            repository.addProfileEvidence(item)
            _statusBannerMessage.value = "Added '${item.title}' to Evidence Ledger."
        }
    }

    fun updateProfileEvidence(item: ProfileEvidenceLedgerEntity) {
        viewModelScope.launch {
            repository.updateProfileEvidence(item)
        }
    }

    fun deleteProfileEvidence(id: Int) {
        viewModelScope.launch {
            repository.deleteProfileEvidence(id)
        }
    }

    // Anger & Overwhelm Pause
    fun recordAngerOverwhelmPause(
        trigger: String,
        impulse: String,
        reality: String,
        alt1: String,
        alt2: String,
        response: String
    ) {
        viewModelScope.launch {
            repository.addAngerOverwhelmPause(trigger, impulse, reality, alt1, alt2, response)
            _statusBannerMessage.value = "Calm pause recorded. Response grounded in dignity."
        }
    }

    fun deleteAngerOverwhelmPause(id: Int) {
        viewModelScope.launch {
            repository.deleteAngerOverwhelmPause(id)
        }
    }

    companion object {
        fun provideFactory(
            repository: GlowUpRepository,
            appContext: Context
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GlowUpViewModel(repository, appContext) as T
            }
        }
    }
}
