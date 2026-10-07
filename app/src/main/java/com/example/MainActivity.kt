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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.GlowUpDatabase
import com.example.data.GlowUpRepository
import com.example.notifications.GlowUpNotificationHelper
import com.example.ui.components.BottomNavigation
import com.example.ui.screens.ConfidenceCheckInSubScreen
import com.example.ui.screens.DailyRoutineGeneratorSubScreen
import com.example.ui.screens.GlowScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HydrationSubScreen
import com.example.ui.screens.MoveScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.SleepSubScreen
import com.example.ui.screens.StudyResetSubScreen
import com.example.ui.screens.TimerScreen
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
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val skincareSteps by viewModel.skincareSteps.collectAsStateWithLifecycle()
    val hydrationReminders by viewModel.hydrationReminders.collectAsStateWithLifecycle()
    val confidenceEntries by viewModel.confidenceEntries.collectAsStateWithLifecycle()
    val weeklyStats by viewModel.weeklyStats.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val activeSubScreen by viewModel.activeSubScreen.collectAsStateWithLifecycle()
    val statusBannerMessage by viewModel.statusBannerMessage.collectAsStateWithLifecycle()
    val exportSummaryDialogText by viewModel.exportSummaryDialogText.collectAsStateWithLifecycle()

    // Request POST_NOTIFICATIONS gracefully on Android 13+ after onboarding completes
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Handled gently without guilt */ }
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

    GlowUpTheme(darkTheme = preferences.darkModeEnabled) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
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
                                        GlowUpNotificationHelper.notificationSamples[1]
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
                                    viewModel.showGentleMessage("Sleep schedule updated gently 🌙")
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
                                        GlowUpNotificationHelper.notificationSamples[4]
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

                        ActiveSubScreen.None -> {
                            when (selectedTab) {
                                BottomNavTab.HOME -> {
                                    HomeScreen(
                                        greeting = viewModel.getDynamicGreeting(preferences),
                                        preferences = preferences,
                                        habits = habits,
                                        onToggleHabit = { viewModel.toggleHabit(it) },
                                        onAddCustomHabit = { title, subTitle, emoji ->
                                            viewModel.addCustomHabit(title, subTitle, emoji)
                                        },
                                        onOpenSubScreen = { viewModel.openSubScreen(it) },
                                        onNavigateTab = { viewModel.selectTab(it) }
                                    )
                                }

                                BottomNavTab.MOVE -> {
                                    val moveDone = habits.any { it.category == "MOVE" && it.completedToday }
                                    MoveScreen(
                                        movementCompletedToday = moveDone,
                                        onStartRoutine = { viewModel.startWorkoutRoutine(it) },
                                        onLogRestDay = {
                                            viewModel.showGentleMessage(
                                                "You're allowed to rest. Rest day honored with care 🌿"
                                            )
                                        }
                                    )
                                }

                                BottomNavTab.GLOW -> {
                                    GlowScreen(
                                        skincareSteps = skincareSteps,
                                        onToggleStep = { viewModel.toggleSkincareStep(it) },
                                        onAddStep = { period, title -> viewModel.addSkincareStep(period, title) },
                                        onDeleteStep = { viewModel.deleteSkincareStep(it) }
                                    )
                                }

                                BottomNavTab.PROGRESS -> {
                                    ProgressScreen(
                                        stats = weeklyStats,
                                        nextWeekFocus = preferences.nextWeekFocus,
                                        confidenceEntries = confidenceEntries,
                                        onSelectNextWeekFocus = { focus ->
                                            viewModel.updatePreferences { it.copy(nextWeekFocus = focus) }
                                            viewModel.showGentleMessage("Next week focus set: $focus 🌱")
                                        },
                                        onOpenConfidenceCheckIn = {
                                            viewModel.openSubScreen(ActiveSubScreen.ConfidenceCheckInSheet)
                                        }
                                    )
                                }

                                BottomNavTab.PROFILE -> {
                                    ProfileScreen(
                                        preferences = preferences,
                                        onUpdatePreferences = { viewModel.updatePreferences(it) },
                                        onOpenSubScreen = { viewModel.openSubScreen(it) },
                                        onNavigateTab = { viewModel.selectTab(it) },
                                        onTriggerSampleNotification = { viewModel.triggerSampleNotification(it) },
                                        onResetTodaysHabits = { viewModel.resetTodaysHabits() },
                                        onExportData = { viewModel.exportUserData() },
                                        onDeleteAllData = { viewModel.deleteAllUserData() }
                                    )
                                }
                            }
                        }
                    }
                }

                // Gentle Floating Toast / Banner
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
                title = { Text("Export My Data") },
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
