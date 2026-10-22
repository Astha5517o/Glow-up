package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.GlowUpDatabase
import com.example.data.GlowUpRepository
import com.example.notifications.GlowUpNotificationHelper
import com.example.ui.components.BottomNavigation
import com.example.ui.screens.AfterSchoolResetSubScreen
import com.example.ui.screens.AsthaAiSubScreen
import com.example.ui.screens.BrainDumpSubScreen
import com.example.ui.screens.BuildScreen
import com.example.ui.screens.CalendarHorizonsSubScreen
import com.example.ui.screens.CloseTodayGentlySubScreen
import com.example.ui.screens.ConfidenceCheckInSubScreen
import com.example.ui.screens.DailyRoutineGeneratorSubScreen
import com.example.ui.screens.EntertainmentWalletSubScreen
import com.example.ui.screens.ErrorLogSubScreen
import com.example.ui.screens.FocusModeSubScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HydrationSubScreen
import com.example.ui.screens.ImStuckSubScreen
import com.example.ui.screens.MorningModeSubScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OneShotLearningSubScreen
import com.example.ui.screens.PracticeQuestionsSubScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecoveryResetSubScreen
import com.example.ui.screens.SchoolModeSubScreen
import com.example.ui.screens.SettingsSubScreen
import com.example.ui.screens.SleepSubScreen
import com.example.ui.screens.StudyResetSubScreen
import com.example.ui.screens.StudyScreen
import com.example.ui.screens.TimerScreen
import com.example.ui.screens.WeeklyReviewSubScreen
import com.example.ui.screens.WellnessScreen
import com.example.ui.screens.YoutubeStudySubScreen
import com.example.ui.screens.ConfidenceLabSubScreen
import com.example.ui.screens.SetbackRecoverySubScreen
import com.example.ui.screens.FinancialLiteracySubScreen
import com.example.ui.screens.AcademicExcellenceSubScreen
import com.example.ui.screens.AngerOverwhelmPauseSubScreen
import com.example.ui.screens.ChemistryMissionSubScreen
import com.example.ui.screens.ProfileEvidenceLedgerSubScreen
import com.example.ui.screens.ScenarioPlanningSubScreen
import com.example.ui.screens.ScholarshipStrategySubScreen
import com.example.ui.screens.ScienceExhibitionSubScreen
import com.example.ui.theme.GlowUpTheme
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.BottomNavTab
import com.example.viewmodel.GlowUpViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = GlowUpDatabase.getDatabase(applicationContext)
        val repository = GlowUpRepository(database.glowUpDao())
        val viewModelFactory = GlowUpViewModel.provideFactory(repository, applicationContext)

        setContent {
            val glowUpViewModel: GlowUpViewModel = viewModel(factory = viewModelFactory)
            GlowUpApp(viewModel = glowUpViewModel)
        }
    }
}

@Composable
fun GlowUpApp(viewModel: GlowUpViewModel) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val nowRecommendation by viewModel.nowRecommendation.collectAsStateWithLifecycle()
    val osTasks by viewModel.osTasks.collectAsStateWithLifecycle()
    val timelineBlocks by viewModel.timelineBlocks.collectAsStateWithLifecycle()
    val studyChapters by viewModel.studyChapters.collectAsStateWithLifecycle()
    val errorLogs by viewModel.errorLogs.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val profileAchievements by viewModel.profileAchievements.collectAsStateWithLifecycle()
    val opportunities by viewModel.opportunities.collectAsStateWithLifecycle()
    val brainDumps by viewModel.brainDumps.collectAsStateWithLifecycle()
    val distractionLogs by viewModel.distractionLogs.collectAsStateWithLifecycle()
    val skipOrEscapeCount by viewModel.skipOrEscapeCount.collectAsStateWithLifecycle()
    val aiMessages by viewModel.aiMessages.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()

    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val skincareSteps by viewModel.skincareSteps.collectAsStateWithLifecycle()
    val hydrationReminders by viewModel.hydrationReminders.collectAsStateWithLifecycle()
    val confidenceEntries by viewModel.confidenceEntries.collectAsStateWithLifecycle()
    val weeklyStats by viewModel.weeklyStats.collectAsStateWithLifecycle()
    val calendarEvents by viewModel.calendarEvents.collectAsStateWithLifecycle()
    val monthlyGoals by viewModel.monthlyGoals.collectAsStateWithLifecycle()
    val longTermGoals by viewModel.longTermGoals.collectAsStateWithLifecycle()
    val recentWins by viewModel.recentWins.collectAsStateWithLifecycle()
    val deadlineSuggestions by viewModel.deadlineSuggestions.collectAsStateWithLifecycle()
    val specialDayPlans by viewModel.specialDayPlans.collectAsStateWithLifecycle()
    val eventPrepMilestones by viewModel.eventPrepMilestones.collectAsStateWithLifecycle()
    val endOfDayReflections by viewModel.endOfDayReflections.collectAsStateWithLifecycle()
    val setbackRecoveries by viewModel.setbackRecoveries.collectAsStateWithLifecycle()
    val financialExperiments by viewModel.financialExperiments.collectAsStateWithLifecycle()
    val academicTopics by viewModel.academicTopics.collectAsStateWithLifecycle()
    val scienceExhibitionScorecards by viewModel.scienceExhibitionScorecards.collectAsStateWithLifecycle()
    val scholarshipStrategies by viewModel.scholarshipStrategies.collectAsStateWithLifecycle()
    val scenarioPlans by viewModel.scenarioPlans.collectAsStateWithLifecycle()
    val profileEvidenceLedger by viewModel.profileEvidenceLedger.collectAsStateWithLifecycle()
    val angerOverwhelmPauses by viewModel.angerOverwhelmPauses.collectAsStateWithLifecycle()
    val missionJeetLectures by viewModel.missionJeetLectures.collectAsStateWithLifecycle()
    val hobbies by viewModel.hobbies.collectAsStateWithLifecycle()
    val morningDecisionPlan by viewModel.morningDecisionPlan.collectAsStateWithLifecycle()

    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val activeSubScreen by viewModel.activeSubScreen.collectAsStateWithLifecycle()
    val statusBannerMessage by viewModel.statusBannerMessage.collectAsStateWithLifecycle()
    val exportSummaryDialogText by viewModel.exportSummaryDialogText.collectAsStateWithLifecycle()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Handled gently */ }
    )

    LaunchedEffect(preferences.onboardingCompleted) {
        if (preferences.onboardingCompleted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(statusBannerMessage) {
        if (statusBannerMessage != null) {
            delay(4000L)
            viewModel.clearBannerMessage()
        }
    }

    GlowUpTheme(
        themeMode = preferences.themeMode,
        darkTheme = preferences.darkModeEnabled
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            floatingActionButton = {
                // Compact persistent ASTHA AI entry point on main tabs (except when already inside a subscreen)
                if (preferences.onboardingCompleted && activeSubScreen == ActiveSubScreen.None) {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.openSubScreen(ActiveSubScreen.AsthaAiSheet) },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.testTag("fab_astha_ai")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "Ask ASTHA AI"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ASTHA AI",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            bottomBar = {
                if (preferences.onboardingCompleted && activeSubScreen == ActiveSubScreen.None) {
                    BottomNavigation(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (!preferences.onboardingCompleted) {
                    OnboardingScreen(
                        initialName = preferences.userName,
                        onFinishOnboarding = { name, focusSet, freeTime, durationMins ->
                            viewModel.completeOnboarding(name, focusSet, freeTime, durationMins)
                        }
                    )
                } else {
                    when (val sub = activeSubScreen) {
                        is ActiveSubScreen.FocusMode -> {
                            FocusModeSubScreen(
                                taskTitle = sub.taskTitle,
                                purpose = sub.purpose,
                                durationMinutes = sub.durationMinutes,
                                currentStepText = sub.currentStepText,
                                totalSteps = sub.totalSteps,
                                linkedTask = sub.linkedTask,
                                onOpenImStuck = { viewModel.openSubScreen(ActiveSubScreen.ImStuckSheet) },
                                onClose = { viewModel.closeSubScreen() },
                                onFinishFocus = { task, title, mins, rating, distraction ->
                                    viewModel.completeFocusSession(task, title, mins, rating, distraction)
                                }
                            )
                        }

                        is ActiveSubScreen.CalendarHorizonsSheet -> {
                            CalendarHorizonsSubScreen(
                                events = calendarEvents,
                                specialDayPlans = specialDayPlans,
                                eventPrepMilestones = eventPrepMilestones,
                                monthlyGoals = monthlyGoals,
                                longTermGoals = longTermGoals,
                                deadlineSuggestions = deadlineSuggestions,
                                preferences = preferences,
                                initialTab = sub.initialTab,
                                onClose = { viewModel.closeSubScreen() },
                                onAddCalendarEvent = { title, startD, endD, startT, endT, allDay, cat, prio, loc, notes, rep, remEn, remPref, linkedGoal, checklist ->
                                    viewModel.addCalendarEvent(
                                        title = title,
                                        dateString = startD,
                                        endDateString = endD,
                                        startTime = startT,
                                        endTime = endT,
                                        isAllDay = allDay,
                                        category = cat,
                                        priority = prio,
                                        location = loc,
                                        notes = notes,
                                        repeatRule = rep,
                                        reminderEnabled = remEn,
                                        reminderPreference = remPref,
                                        linkedProjectOrGoal = linkedGoal,
                                        beforeEventChecklistCsv = checklist
                                    )
                                },
                                onUpdateCalendarEvent = { viewModel.updateCalendarEvent(it) },
                                onDeleteCalendarEvent = { viewModel.deleteCalendarEvent(it) },
                                onSaveSpecialDayPlan = { dateStr, dayType, focusNote, itemsCsv, conflictOk ->
                                    viewModel.saveSpecialDayPlan(dateStr, dayType, focusNote, itemsCsv, conflictOk)
                                },
                                onDeleteSpecialDayPlan = { viewModel.deleteSpecialDayPlan(it) },
                                onHandleEventPrepMilestoneAction = { milestone, action ->
                                    viewModel.handleEventPrepMilestoneAction(milestone, action)
                                },
                                onAcceptDeadlineSuggestion = { viewModel.acceptDeadlineSuggestion(it) },
                                onDismissDeadlineSuggestion = { viewModel.dismissDeadlineSuggestion(it) },
                                onAddMonthlyGoal = { dom, title, weekly, smallest ->
                                    viewModel.addMonthlyGoal(dom, title, weekly, smallest)
                                },
                                onAdvanceMonthlyGoal = { viewModel.advanceMonthlyGoal(it) },
                                onUpdateMonthlyGoal = { viewModel.updateMonthlyGoal(it) },
                                onDeleteMonthlyGoal = { viewModel.deleteMonthlyGoal(it) },
                                onUpdateLongTermGoal = { viewModel.updateLongTermGoal(it) },
                                onSaveMonthlyReview = { built, learned, ev, cont, stop ->
                                    viewModel.saveMonthlyReview(built, learned, ev, cont, stop)
                                },
                                onSaveWeeklyPlan = { mon, tue, wed, thu, fri, sat, sun ->
                                    viewModel.saveWeeklyPlan(mon, tue, wed, thu, fri, sat, sun)
                                },
                                onStartGoalActionFocus = { title, step, mins ->
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = title,
                                            purpose = step,
                                            durationMinutes = mins,
                                            currentStepText = step
                                        )
                                    )
                                },
                                onOpenCloseTodayGently = {
                                    viewModel.openSubScreen(ActiveSubScreen.CloseTodayGentlySheet)
                                }
                            )
                        }

                        is ActiveSubScreen.CloseTodayGentlySheet -> {
                            val todayDateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
                            CloseTodayGentlySubScreen(
                                todayDateString = todayDateStr,
                                activeDayType = nowRecommendation.selectedDayType,
                                unfinishedTasks = osTasks.filter { !it.isCompleted && !it.isPostponed },
                                existingReflection = endOfDayReflections.find { it.dateString == todayDateStr },
                                onClose = { viewModel.closeSubScreen() },
                                onHandleUnfinishedTask = { task, dec ->
                                    viewModel.handleUnfinishedTaskEndOfDay(task, dec)
                                },
                                onSaveReflection = { meaningful, learned, glad, carry, letGo, quickTag, tomorrowStep ->
                                    viewModel.saveEndOfDayReflection(
                                        dateString = todayDateStr,
                                        meaningfulToday = meaningful,
                                        learnedOrExperienced = learned,
                                        gladMadeTimeFor = glad,
                                        carryIntoTomorrow = carry,
                                        letGoOfForNow = letGo,
                                        quickTagSelected = quickTag,
                                        tomorrowFirstStep = tomorrowStep
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.ImStuckSheet -> {
                            ImStuckSubScreen(
                                onClose = { viewModel.closeSubScreen() },
                                onAskAsthaAiHint = { prompt ->
                                    viewModel.sendPromptToAsthaAi(prompt)
                                    viewModel.openSubScreen(ActiveSubScreen.AsthaAiSheet)
                                }
                            )
                        }

                        is ActiveSubScreen.AsthaAiSheet -> {
                            AsthaAiSubScreen(
                                messages = aiMessages,
                                isLoading = isAiLoading,
                                onClose = { viewModel.closeSubScreen() },
                                onSendPrompt = { viewModel.sendPromptToAsthaAi(it) }
                            )
                        }

                        is ActiveSubScreen.OneShotSheet -> {
                            OneShotLearningSubScreen(
                                chapter = sub.chapter,
                                onClose = { viewModel.closeSubScreen() },
                                onCompleteOneShot = { id, video, purpose, concept, formula, doubt, status ->
                                    viewModel.saveOneShotCompletion(
                                        chapterId = id,
                                        videoResource = video,
                                        purpose = purpose,
                                        capturedConcept = concept,
                                        capturedFormula = formula,
                                        capturedDoubt = doubt,
                                        understandingStatus = status
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.PracticeQuestionsSheet -> {
                            PracticeQuestionsSubScreen(
                                initialSubject = sub.initialSubject,
                                skipOrEscapeCount = skipOrEscapeCount,
                                onClose = { viewModel.closeSubScreen() },
                                onRecordOutcome = { q, outcome, logErr, why ->
                                    viewModel.recordPracticeQuestionOutcome(q, outcome, logErr, why)
                                },
                                onOpenImStuckForQuestion = {
                                    viewModel.openSubScreen(ActiveSubScreen.ImStuckSheet)
                                }
                            )
                        }

                        is ActiveSubScreen.ErrorLogSheet -> {
                            ErrorLogSubScreen(
                                errorLogs = errorLogs,
                                onClose = { viewModel.closeSubScreen() },
                                onReviewError = { viewModel.reviewErrorLogEntry(it) },
                                onAddError = { subj, chap, q, type, why, correct, src ->
                                    viewModel.addErrorLogEntry(subj, chap, q, type, why, correct, src)
                                }
                            )
                        }

                        is ActiveSubScreen.BrainDumpSheet -> {
                            BrainDumpSubScreen(
                                brainDumps = brainDumps,
                                onClose = { viewModel.closeSubScreen() },
                                onSaveBrainDump = { content, category ->
                                    viewModel.saveBrainDumpThought(content, category)
                                }
                            )
                        }

                        is ActiveSubScreen.AfterSchoolResetSheet -> {
                            AfterSchoolResetSubScreen(
                                nextFocusTitle = nowRecommendation.title,
                                onClose = { viewModel.closeSubScreen() },
                                onStartNextFocusBlock = {
                                    viewModel.startFocusForRecommendation(nowRecommendation)
                                }
                            )
                        }

                        is ActiveSubScreen.EntertainmentWalletSheet -> {
                            val priorityDone = osTasks.any { it.isTopThree && it.isCompleted }
                            EntertainmentWalletSubScreen(
                                preferences = preferences,
                                hasCompletedPriorityToday = priorityDone,
                                onClose = { viewModel.closeSubScreen() },
                                onFocusFirst25Min = {
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = nowRecommendation.title,
                                            purpose = "25-minute focus sprint before enjoying ${preferences.entertainmentTitle}",
                                            durationMinutes = 25,
                                            currentStepText = "Complete 25 focused minutes, then enjoy your break!",
                                            linkedTask = nowRecommendation.taskToStart
                                        )
                                    )
                                },
                                onWatchAnyway = {
                                    viewModel.showGentleMessage("Enjoy your ${preferences.entertainmentTitle} break guilt-free!")
                                }
                            )
                        }

                        is ActiveSubScreen.MorningModeSheet -> {
                            MorningModeSubScreen(
                                userName = preferences.userName.ifBlank { "Astha" },
                                tasks = osTasks,
                                plan = morningDecisionPlan,
                                onPlanDayAgain = { mins, energy ->
                                    viewModel.refreshMorningDecisionPlan(mins, energy)
                                },
                                onStartPriorityFocus = { priority ->
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = "${priority.subject}: ${priority.exactTopic}",
                                            purpose = "${priority.startingResourceOrMethod} → ${priority.questionsToAttempt}",
                                            durationMinutes = priority.estimatedMinutes,
                                            currentStepText = priority.activeRecallStep
                                        )
                                    )
                                },
                                onClose = { viewModel.closeSubScreen() }
                            )
                        }

                        is ActiveSubScreen.SchoolModeSheet -> {
                            SchoolModeSubScreen(
                                onClose = { viewModel.closeSubScreen() },
                                onStartQuickSchoolFocus = { title, mins ->
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = "School Period: $title",
                                            purpose = "Focused $mins-minute school free-period mission",
                                            durationMinutes = mins,
                                            currentStepText = title
                                        )
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.YoutubeStudySheet -> {
                            YoutubeStudySubScreen(
                                chapters = studyChapters,
                                onClose = { viewModel.closeSubScreen() },
                                onStartPostYoutubePractice = { topic ->
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = "Post-YouTube Recall & 5 Questions: $topic",
                                            purpose = "Active recall + solve 5 targeted questions without video open",
                                            durationMinutes = 25,
                                            currentStepText = "Write 3 key ideas from memory, then solve Question 1"
                                        )
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.RecoveryResetSheet -> {
                            RecoveryResetSubScreen(
                                tasks = osTasks,
                                onClose = { viewModel.closeSubScreen() },
                                onUpdateTaskBucket = { task, newBucket ->
                                    viewModel.rescheduleOsTask(task, task.scheduledTimeSlot, newBucket)
                                },
                                onRegeneratePlan = {
                                    viewModel.triggerIntelligentRecovery()
                                }
                            )
                        }

                        is ActiveSubScreen.WeeklyReviewSheet -> {
                            WeeklyReviewSubScreen(
                                preferences = preferences,
                                chapters = studyChapters,
                                errorLogs = errorLogs,
                                projects = projects,
                                opportunities = opportunities,
                                distractionLogs = distractionLogs,
                                weeklyStats = weeklyStats,
                                onClose = { viewModel.closeSubScreen() },
                                onSaveWeeklyReflections = { worked, didNot, change, nextFocus ->
                                    viewModel.updatePreferences {
                                        it.copy(
                                            weeklyWorkedWell = worked,
                                            weeklyDidNotWork = didNot,
                                            weeklyShouldChange = change,
                                            nextWeekFocus = nextFocus
                                        )
                                    }
                                    viewModel.showGentleMessage("Weekly review saved!")
                                }
                            )
                        }

                        is ActiveSubScreen.SettingsSheet -> {
                            SettingsSubScreen(
                                preferences = preferences,
                                onClose = { viewModel.closeSubScreen() },
                                onUpdatePreferences = { viewModel.updatePreferences(it) },
                                onTriggerSampleNotification = { viewModel.triggerSampleNotification(it) },
                                onResetTodaysHabits = { viewModel.resetTodaysHabits() },
                                onExportData = { viewModel.exportUserData() },
                                onDeleteAllData = { viewModel.deleteAllUserData() }
                            )
                        }

                        is ActiveSubScreen.ActiveWorkoutTimer -> {
                            TimerScreen(
                                routine = sub.routine,
                                onClose = { viewModel.closeSubScreen() },
                                onFinishRoutine = { completedRoutine ->
                                    viewModel.finishWorkoutRoutine(completedRoutine)
                                }
                            )
                        }

                        is ActiveSubScreen.HydrationSheet -> {
                            HydrationSubScreen(
                                preferences = preferences,
                                reminders = hydrationReminders,
                                onClose = { viewModel.closeSubScreen() },
                                onToggleMasterHydration = { enabled ->
                                    viewModel.updatePreferences { it.copy(hydrationRemindersEnabled = enabled) }
                                },
                                onTogglePauseHydration = { paused ->
                                    viewModel.updatePreferences { it.copy(hydrationPaused = paused) }
                                },
                                onToggleReminderCompleted = { viewModel.toggleHydrationReminderCompleted(it) },
                                onToggleReminderEnabled = { viewModel.toggleHydrationReminderEnabled(it) },
                                onAddReminder = { label, hr, min ->
                                    viewModel.addHydrationReminder(label, hr, min)
                                },
                                onDeleteReminder = { viewModel.deleteHydrationReminder(it) },
                                onSendHydrationNotificationNow = {
                                    viewModel.triggerSampleNotification(
                                        GlowUpNotificationHelper.notificationSamples.last()
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.SleepSheet -> {
                            SleepSubScreen(
                                preferences = preferences,
                                onClose = { viewModel.closeSubScreen() },
                                onUpdateSleepSchedule = { wake, windDown, bedtime ->
                                    viewModel.updatePreferences {
                                        it.copy(
                                            wakeTime = wake,
                                            windDownTime = windDown,
                                            targetBedtime = bedtime
                                        )
                                    }
                                    viewModel.showGentleMessage("Sleep schedule updated 🌙")
                                },
                                onCompleteWindDown = { viewModel.markSleepWindDownDone() },
                                onMissedBedtimeSupport = { viewModel.acknowledgeMissedBedtimeSupport() }
                            )
                        }

                        is ActiveSubScreen.ConfidenceCheckInSheet -> {
                            ConfidenceCheckInSubScreen(
                                recentEntries = confidenceEntries,
                                onClose = { viewModel.closeSubScreen() },
                                onSaveCheckIn = { emoji, label, proud, helped ->
                                    viewModel.saveConfidenceCheckIn(emoji, label, proud, helped)
                                }
                            )
                        }

                        is ActiveSubScreen.StudyResetSheet -> {
                            StudyResetSubScreen(
                                preferences = preferences,
                                onClose = { viewModel.closeSubScreen() },
                                onUpdateStudyReset = { enabled, interval ->
                                    viewModel.updatePreferences {
                                        it.copy(
                                            studyResetEnabled = enabled,
                                            studyResetIntervalMinutes = interval
                                        )
                                    }
                                },
                                onTriggerStudyNotification = {
                                    viewModel.triggerSampleNotification(
                                        GlowUpNotificationHelper.notificationSamples[3]
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.DailyRoutineGeneratorSheet -> {
                            DailyRoutineGeneratorSubScreen(
                                userName = preferences.userName.ifBlank { "Astha" },
                                routineBlocks = viewModel.generateDailyRoutine(preferences),
                                preferences = preferences,
                                onClose = { viewModel.closeSubScreen() }
                            )
                        }

                        is ActiveSubScreen.ConfidenceLabSheet -> {
                            ConfidenceLabSubScreen(
                                onClose = { viewModel.closeSubScreen() }
                            )
                        }

                        is ActiveSubScreen.SetbackRecoverySheet -> {
                            SetbackRecoverySubScreen(
                                setbacks = setbackRecoveries,
                                onClose = { viewModel.closeSubScreen() },
                                onRecordSetback = { title, cat, happened, feeling, stillTrue, outControl, inControl, lesson, nextStep ->
                                    viewModel.recordSetbackRecovery(title, cat, happened, feeling, stillTrue, outControl, inControl, lesson, nextStep)
                                },
                                onToggleResolved = { viewModel.toggleSetbackResolved(it) },
                                onDeleteSetback = { viewModel.deleteSetbackRecovery(it) }
                            )
                        }

                        is ActiveSubScreen.FinancialLiteracySheet -> {
                            FinancialLiteracySubScreen(
                                experiments = financialExperiments,
                                onClose = { viewModel.closeSubScreen() },
                                onAddExperiment = { title, skill, stage, pot, actual, client, learned ->
                                    viewModel.addFinancialExperiment(title, skill, stage, pot, actual, client, learned)
                                },
                                onDeleteExperiment = { viewModel.deleteFinancialExperiment(it) }
                            )
                        }

                        is ActiveSubScreen.AcademicExcellenceSheet -> {
                            AcademicExcellenceSubScreen(
                                topics = academicTopics,
                                onClose = { viewModel.closeSubScreen() },
                                onUpdateTopic = { viewModel.updateAcademicTopic(it) },
                                onAddTopic = { viewModel.addAcademicTopic(it) },
                                onDeleteTopic = { viewModel.deleteAcademicTopic(it) }
                            )
                        }

                        is ActiveSubScreen.ChemistryMissionSheet -> {
                            ChemistryMissionSubScreen(
                                topics = academicTopics,
                                onClose = { viewModel.closeSubScreen() },
                                onUpdateTopic = { viewModel.updateAcademicTopic(it) },
                                onAddTopic = { viewModel.addAcademicTopic(it) }
                            )
                        }

                        is ActiveSubScreen.ScienceExhibitionSheet -> {
                            ScienceExhibitionSubScreen(
                                scorecards = scienceExhibitionScorecards,
                                onClose = { viewModel.closeSubScreen() },
                                onAddScorecard = { viewModel.addScienceExhibitionScorecard(it) },
                                onUpdateScorecard = { viewModel.updateScienceExhibitionScorecard(it) },
                                onSelectForExecution = { viewModel.selectProjectForExecution(it) },
                                onAdvanceMilestone = { viewModel.advanceExecutionMilestone(it) },
                                onDeleteScorecard = { viewModel.deleteScienceExhibitionScorecard(it) }
                            )
                        }

                        is ActiveSubScreen.ScholarshipStrategySheet -> {
                            ScholarshipStrategySubScreen(
                                strategies = scholarshipStrategies,
                                onClose = { viewModel.closeSubScreen() },
                                onAddStrategy = { viewModel.addScholarshipStrategy(it) },
                                onUpdateStrategy = { viewModel.updateScholarshipStrategy(it) },
                                onDeleteStrategy = { viewModel.deleteScholarshipStrategy(it) }
                            )
                        }

                        is ActiveSubScreen.ScenarioPlanningSheet -> {
                            ScenarioPlanningSubScreen(
                                scenarios = scenarioPlans,
                                onClose = { viewModel.closeSubScreen() },
                                onAddScenario = { viewModel.addScenarioPlan(it) },
                                onUpdateScenario = { viewModel.updateScenarioPlan(it) },
                                onDeleteScenario = { viewModel.deleteScenarioPlan(it) }
                            )
                        }

                        is ActiveSubScreen.ProfileEvidenceSheet -> {
                            ProfileEvidenceLedgerSubScreen(
                                items = profileEvidenceLedger,
                                onClose = { viewModel.closeSubScreen() },
                                onAddItem = { viewModel.addProfileEvidence(it) },
                                onDeleteItem = { viewModel.deleteProfileEvidence(it) }
                            )
                        }

                        is ActiveSubScreen.AngerOverwhelmPauseSheet -> {
                            AngerOverwhelmPauseSubScreen(
                                pauses = angerOverwhelmPauses,
                                onClose = { viewModel.closeSubScreen() },
                                onRecordPause = { trig, imp, real, a1, a2, resp ->
                                    viewModel.recordAngerOverwhelmPause(trig, imp, real, a1, a2, resp)
                                },
                                onDeletePause = { viewModel.deleteAngerOverwhelmPause(it) }
                            )
                        }

                        is ActiveSubScreen.MissionJeetSheet -> {
                            MissionJeetSubScreen(
                                lectures = missionJeetLectures,
                                onClose = { viewModel.closeSubScreen() },
                                onSaveLecture = { viewModel.saveMissionJeetLecture(it) },
                                onUpdateLecture = { viewModel.updateMissionJeetLecture(it) },
                                onStartFocusSession = { task ->
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = task.title,
                                            purpose = task.subtitle,
                                            durationMinutes = task.durationMinutes,
                                            currentStepText = "Active lecture notes & worked examples",
                                            totalSteps = 5,
                                            linkedTask = task
                                        )
                                    )
                                }
                            )
                        }

                        is ActiveSubScreen.HobbiesAndJoySheet -> {
                            HobbiesAndJoySubScreen(
                                hobbies = hobbies,
                                onClose = { viewModel.closeSubScreen() },
                                onSaveHobby = { viewModel.saveHobby(it) },
                                onUpdateHobby = { viewModel.updateHobby(it) },
                                onScheduleHobbyOnCalendar = { viewModel.scheduleHobbyOnCalendar(it) }
                            )
                        }

                        is ActiveSubScreen.DistractionRecoverySheet -> {
                            MorningModeSubScreen(
                                academicTopics = academicTopics,
                                calendarEvents = calendarEvents,
                                generatedPlan = morningDecisionPlan,
                                availableMinutes = 45,
                                currentEnergy = preferences.currentEnergyLevel,
                                onClose = { viewModel.closeSubScreen() },
                                onGenerateMorningPlan = { mins, energy ->
                                    viewModel.generateMorningDecisionPlan(mins, energy)
                                },
                                onStartPriorityFocus = { prio ->
                                    viewModel.openSubScreen(
                                        ActiveSubScreen.FocusMode(
                                            taskTitle = "${prio.subject}: ${prio.exactTopic}",
                                            purpose = prio.startingResourceOrMethod,
                                            durationMinutes = prio.estimatedMinutes,
                                            currentStepText = prio.activeRecallStep,
                                            totalSteps = 5
                                        )
                                    )
                                }
                            )
                        }

                        ActiveSubScreen.None -> {
                            when (selectedTab) {
                                BottomNavTab.TODAY -> {
                                    HomeScreen(
                                        greeting = viewModel.getDynamicGreeting(preferences),
                                        formattedDate = viewModel.getFormattedTodayDate(),
                                        preferences = preferences,
                                        nowRecommendation = nowRecommendation,
                                        tasks = osTasks,
                                        timelineBlocks = timelineBlocks,
                                        calendarEvents = calendarEvents,
                                        monthlyGoals = monthlyGoals,
                                        recentWins = recentWins,
                                        onSetEnergyLevel = { viewModel.setEnergyLevel(it) },
                                        onSetDayMode = { viewModel.setDayMode(it) },
                                        onStartNowRecommendation = { viewModel.startFocusForRecommendation(it) },
                                        onSkipNowRecommendation = { viewModel.skipCurrentRecommendation(it) },
                                        onStartTaskFocus = { viewModel.startFocusForTask(it) },
                                        onToggleTask = { viewModel.toggleOsTask(it) },
                                        onPostponeTask = { viewModel.postponeOsTask(it) },
                                        onRescheduleTask = { task, slot, bucket ->
                                            viewModel.rescheduleOsTask(task, slot, bucket)
                                        },
                                        onAddTask = { title, subTitle, why, dom, bucket, mins, energy, slot, top3, topSlot ->
                                            viewModel.addOsTask(title, subTitle, why, dom, bucket, mins, energy, slot, top3, topSlot)
                                        },
                                        onToggleTimelineBlock = { viewModel.toggleTimelineBlock(it) },
                                        onCalculateMinutesMission = { viewModel.calculateActionForMinutes(it) },
                                        onResolveConfusion = { viewModel.resolveConfusionToSingleAction(it) },
                                        onOpenSubScreen = { viewModel.openSubScreen(it) },
                                        onNavigateTab = { viewModel.selectTab(it) }
                                    )
                                }

                                BottomNavTab.STUDY -> {
                                    StudyScreen(
                                        chapters = studyChapters,
                                        errorLogs = errorLogs,
                                        onStartStudyMission = { ch ->
                                            viewModel.openSubScreen(
                                                ActiveSubScreen.FocusMode(
                                                    taskTitle = "${ch.subject} — ${ch.chapterName}",
                                                    purpose = "0-5m Active recall (${ch.capturedFormula.ifBlank { ch.currentTopic }}) → 5-10m Easy Qs → 10-25m Medium Qs → 25-38m Challenge → 38-45m Mistake Analysis",
                                                    durationMinutes = 45,
                                                    currentStepText = "0–5 min: Active recall of key concepts & formulas without looking at notes"
                                                )
                                            )
                                        },
                                        onOpenOneShotMode = { ch ->
                                            viewModel.openSubScreen(ActiveSubScreen.OneShotSheet(ch))
                                        },
                                        onOpenPracticeQuestions = { subj ->
                                            viewModel.openSubScreen(ActiveSubScreen.PracticeQuestionsSheet(subj))
                                        },
                                        onOpenErrorLog = {
                                            viewModel.openSubScreen(ActiveSubScreen.ErrorLogSheet)
                                        },
                                        onOpenImStuck = {
                                            viewModel.openSubScreen(ActiveSubScreen.ImStuckSheet)
                                        },
                                        onOpenYoutubeMission = {
                                            viewModel.openSubScreen(ActiveSubScreen.YoutubeStudySheet)
                                        },
                                        onOpenRevisionWithAi = {
                                            viewModel.sendPromptToAsthaAi("Give me an active recall question on my current chapter.")
                                            viewModel.openSubScreen(ActiveSubScreen.AsthaAiSheet)
                                        },
                                        onAddChapter = { subj, name, topic, weak ->
                                            viewModel.addStudyChapter(subj, name, topic, weak)
                                        },
                                        onOpenChemistryMission = {
                                            viewModel.openSubScreen(ActiveSubScreen.ChemistryMissionSheet)
                                        },
                                        onOpenAcademicExcellence = {
                                            viewModel.openSubScreen(ActiveSubScreen.AcademicExcellenceSheet)
                                        },
                                        onOpenMissionJeetTracker = {
                                            viewModel.openSubScreen(ActiveSubScreen.MissionJeetSheet)
                                        }
                                    )
                                }

                                BottomNavTab.BUILD -> {
                                    BuildScreen(
                                        projects = projects,
                                        preferences = preferences,
                                        onUpdateProject = { viewModel.updateProject(it) },
                                        onAddProject = { name, tag, prob, user, sol, tech, next ->
                                            viewModel.addProject(name, tag, prob, user, sol, tech, next)
                                        },
                                        onStartProjectFocus = { proj ->
                                            viewModel.openSubScreen(
                                                ActiveSubScreen.FocusMode(
                                                    taskTitle = "${proj.name} (${proj.pipelineStage})",
                                                    purpose = proj.nextConcreteAction,
                                                    durationMinutes = 30,
                                                    currentStepText = proj.nextConcreteAction
                                                )
                                            )
                                        },
                                        onUpdateNcscSettings = { evName, date, title, csv ->
                                            viewModel.updatePreferences {
                                                it.copy(
                                                    ncscEventName = evName,
                                                    ncscTargetDate = date,
                                                    ncscProjectTitle = title,
                                                    ncscChecklistCsv = csv
                                                )
                                            }
                                        },
                                        onOpenScienceExhibition = {
                                            viewModel.openSubScreen(ActiveSubScreen.ScienceExhibitionSheet)
                                        },
                                        onOpenProfileEvidence = {
                                            viewModel.openSubScreen(ActiveSubScreen.ProfileEvidenceSheet)
                                        }
                                    )
                                }

                                BottomNavTab.WELLNESS -> {
                                    WellnessScreen(
                                        preferences = preferences,
                                        habits = habits,
                                        skincareSteps = skincareSteps,
                                        onToggleHabit = { viewModel.toggleHabit(it) },
                                        onAddCustomHabit = { title, subTitle, emoji ->
                                            viewModel.addCustomHabit(title, subTitle, emoji)
                                        },
                                        onStartMovementRoutine = { viewModel.startWorkoutRoutine(it) },
                                        onToggleSkincareStep = { viewModel.toggleSkincareStep(it) },
                                        onAddSkincareStep = { period, title -> viewModel.addSkincareStep(period, title) },
                                        onDeleteSkincareStep = { viewModel.deleteSkincareStep(it) },
                                        onOpenSubScreen = { viewModel.openSubScreen(it) }
                                    )
                                }

                                BottomNavTab.PROFILE -> {
                                    ProfileScreen(
                                        preferences = preferences,
                                        achievements = profileAchievements,
                                        opportunities = opportunities,
                                        projects = projects,
                                        onUpdatePreferences = { viewModel.updatePreferences(it) },
                                        onAddAchievement = { cat, title, date, org, role, what, res, imp, ev, link ->
                                            viewModel.addProfileAchievement(cat, title, date, org, role, what, res, imp, ev, link)
                                        },
                                        onDeleteAchievement = { viewModel.deleteProfileAchievement(it) },
                                        onAddOpportunity = { name, cat, country, org, elig, dead, fund, req, src, stat, notes, ver ->
                                            viewModel.addOpportunity(name, cat, country, org, elig, dead, fund, req, src, stat, notes, ver)
                                        },
                                        onCycleOpportunityStatus = { viewModel.cycleOpportunityStatus(it) },
                                        onVerifyOpportunity = { viewModel.verifyOpportunity(it) },
                                        onOpenSubScreen = { viewModel.openSubScreen(it) },
                                        onNavigateTab = { viewModel.selectTab(it) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Gentle Floating Status Banner
                AnimatedVisibility(
                    visible = statusBannerMessage != null,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.inverseSurface,
                        tonalElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.clearBannerMessage() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = statusBannerMessage.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { viewModel.clearBannerMessage() }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Dismiss notification banner",
                                    tint = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Export Data Summary Dialog
        if (exportSummaryDialogText != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissExportDialog() },
                title = { Text("Export ASTHA OS Data") },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = exportSummaryDialogText.orEmpty(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissExportDialog() }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
