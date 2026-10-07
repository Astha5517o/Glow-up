package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ConfidenceEntryEntity
import com.example.data.GeneratedRoutineBlock
import com.example.data.HydrationReminderEntity
import com.example.data.UserPreferencesEntity
import com.example.notifications.GlowUpNotificationHelper
import com.example.ui.components.MoodSelector
import com.example.ui.components.PrimaryButton
import com.example.ui.components.ReminderCard
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import kotlinx.coroutines.delay

@Composable
fun HydrationSubScreen(
    preferences: UserPreferencesEntity,
    reminders: List<HydrationReminderEntity>,
    onClose: () -> Unit,
    onToggleMasterHydration: (Boolean) -> Unit,
    onTogglePauseHydration: (Boolean) -> Unit,
    onToggleReminderCompleted: (HydrationReminderEntity) -> Unit,
    onToggleReminderEnabled: (HydrationReminderEntity) -> Unit,
    onAddReminder: (timeLabel: String, hour24: Int, minute: Int) -> Unit,
    onDeleteReminder: (Int) -> Unit,
    onSendHydrationNotificationNow: () -> Unit
) {
    BackHandler { onClose() }
    var showAddTimeDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("hydration_sub_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SubScreenTopBar(title = "💧 Gentle Hydration", onBack = onClose)
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "💧 Hydration break",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"Take a moment for some water.\"\nNo strict liter targets or pressure—just gentle reminders to help you feel refreshed throughout your day.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { onTogglePauseHydration(!preferences.hydrationPaused) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pause_hydration_button")
                        ) {
                            Icon(
                                imageVector = if (preferences.hydrationPaused) Icons.Filled.PlayCircleOutline else Icons.Filled.PauseCircleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (preferences.hydrationPaused) "Resume Reminders" else "Pause Reminders")
                        }
                        OutlinedButton(
                            onClick = onSendHydrationNotificationNow,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("test_hydration_notification_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.NotificationsActive,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Remind Now")
                        }
                    }
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Hydration reminders", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = if (preferences.hydrationRemindersEnabled) "Active on your schedule" else "Turned off",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = preferences.hydrationRemindersEnabled,
                        onCheckedChange = onToggleMasterHydration
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Your Reminder Times",
                subtitle = "Tap the circle to mark a water break completed",
                actionText = "+ Add time",
                onActionClick = { showAddTimeDialog = true }
            )
        }

        items(reminders, key = { it.id }) { reminder ->
            ReminderCard(
                reminder = reminder,
                isPaused = preferences.hydrationPaused || !preferences.hydrationRemindersEnabled,
                onToggleCompleted = { onToggleReminderCompleted(reminder) },
                onToggleEnabled = { onToggleReminderEnabled(reminder) },
                onDelete = if (reminders.size > 2) ({ onDeleteReminder(reminder.id) }) else null
            )
        }

        item {
            OutlinedButton(
                onClick = { showAddTimeDialog = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_hydration_time_button")
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add custom reminder time")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showAddTimeDialog) {
        var timeLabelInput by rememberSaveable { mutableStateOf("8:00 PM") }
        AlertDialog(
            onDismissRequest = { showAddTimeDialog = false },
            title = { Text("Add Hydration Reminder") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Enter a gentle reminder time (e.g., 3:30 PM or 8:00 PM):",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = timeLabelInput,
                        onValueChange = { timeLabelInput = it },
                        label = { Text("Time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (timeLabelInput.isNotBlank()) {
                            onAddReminder(timeLabelInput.trim(), 20, 0)
                            showAddTimeDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTimeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SleepSubScreen(
    preferences: UserPreferencesEntity,
    onClose: () -> Unit,
    onUpdateSleepSchedule: (wake: String, windDown: String, bedtime: String) -> Unit,
    onCompleteWindDown: () -> Unit,
    onMissedBedtimeSupport: () -> Unit
) {
    BackHandler { onClose() }
    var wakeInput by rememberSaveable { mutableStateOf(preferences.wakeTime) }
    var windDownInput by rememberSaveable { mutableStateOf(preferences.windDownTime) }
    var bedtimeInput by rememberSaveable { mutableStateOf(preferences.targetBedtime) }
    var showMissedSupportCard by rememberSaveable { mutableStateOf(false) }

    val timelineItems = listOf(
        "9:00 PM" to "Prepare for tomorrow",
        "9:15 PM" to "Skincare",
        windDownInput to "Screen wind-down",
        bedtimeInput to "Sleep"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("sleep_sub_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SubScreenTopBar(title = "😴 Sleep & Wind-Down", onBack = onClose)
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Your evening",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tomorrow starts with the rest you get tonight.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    timelineItems.forEach { (timeStr, stepTitle) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.width(88.dp)
                            ) {
                                Text(
                                    text = timeStr,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = stepTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // Supportive Missed Bedtime Message
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🌙 Up later than planned?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"Tonight didn't go as planned. That's okay. Tomorrow is another chance.\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {
                            showMissedSupportCard = true
                            onMissedBedtimeSupport()
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("missed_bedtime_button")
                    ) {
                        Text("Be gentle with tonight")
                    }
                }
            }
        }

        // Customize Sleep Schedule
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Customize Sleep Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = wakeInput,
                        onValueChange = { wakeInput = it },
                        label = { Text("Wake time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = windDownInput,
                        onValueChange = { windDownInput = it },
                        label = { Text("Wind-down time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = bedtimeInput,
                        onValueChange = { bedtimeInput = it },
                        label = { Text("Target bedtime") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SecondaryButton(
                        text = "Save Sleep Schedule",
                        onClick = {
                            onUpdateSleepSchedule(
                                wakeInput.ifBlank { "7:00 AM" },
                                windDownInput.ifBlank { "9:30 PM" },
                                bedtimeInput.ifBlank { "10:00 PM" }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "save_sleep_schedule_button"
                    )
                }
            }
        }

        item {
            PrimaryButton(
                text = "Complete Evening Wind-Down 🌙",
                onClick = {
                    onCompleteWindDown()
                    onClose()
                },
                modifier = Modifier.fillMaxWidth(),
                testTag = "complete_wind_down_button"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ConfidenceCheckInSubScreen(
    recentEntries: List<ConfidenceEntryEntity>,
    onClose: () -> Unit,
    onSaveCheckIn: (moodEmoji: String, moodLabel: String, proudOf: String, helpedFeelGood: String) -> Unit
) {
    BackHandler { onClose() }

    var selectedEmoji by rememberSaveable { mutableStateOf("🙂") }
    var selectedLabel by rememberSaveable { mutableStateOf("Good") }
    var proudOfText by rememberSaveable { mutableStateOf("") }
    var helpedFeelGoodText by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("confidence_sub_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SubScreenTopBar(title = "🌸 Confidence Check-In", onBack = onClose)
        }

        item {
            Text(
                text = "How did you feel today?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            MoodSelector(
                selectedMoodLabel = selectedLabel,
                onMoodSelected = { emoji, label ->
                    selectedEmoji = emoji
                    selectedLabel = label
                }
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "What is one thing you're proud of today?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = proudOfText,
                        onValueChange = { proudOfText = it },
                        placeholder = { Text("Even small things count — like drinking water, finishing homework, or resting.") },
                        minLines = 3,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("confidence_proud_input")
                    )

                    Text(
                        text = "What helped you feel good today? (Optional)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = helpedFeelGoodText,
                        onValueChange = { helpedFeelGoodText = it },
                        placeholder = { Text("Stretching, talking to a friend, fresh air, music...") },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("confidence_helped_input")
                    )

                    PrimaryButton(
                        text = "Save Reflection 🌸",
                        onClick = {
                            onSaveCheckIn(
                                selectedEmoji,
                                selectedLabel,
                                proudOfText.ifBlank { "Took a quiet moment to check in with myself." },
                                helpedFeelGoodText
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "save_confidence_checkin_button"
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Past Reflections",
                subtitle = if (recentEntries.isEmpty()) "Give yourself one minute today." else "Your personal moments of gratitude & capability"
            )
        }

        if (recentEntries.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Give yourself one minute today.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        } else {
            items(recentEntries, key = { it.id }) { entry ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${entry.moodEmoji} ${entry.moodLabel}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = entry.dateString,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Proud of: ${entry.proudOfText}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (entry.helpedFeelGoodText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Helped me feel good: ${entry.helpedFeelGoodText}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudyResetSubScreen(
    preferences: UserPreferencesEntity,
    onClose: () -> Unit,
    onUpdateStudyReset: (enabled: Boolean, intervalMinutes: Int) -> Unit,
    onTriggerStudyNotification: () -> Unit
) {
    BackHandler { onClose() }
    var customMinutesInput by rememberSaveable {
        mutableStateOf(preferences.studyResetIntervalMinutes.toString())
    }
    var breakCountdownSeconds by rememberSaveable { mutableIntStateOf(60) }
    var isBreakTimerRunning by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isBreakTimerRunning, breakCountdownSeconds) {
        if (isBreakTimerRunning && breakCountdownSeconds > 0) {
            delay(1000L)
            breakCountdownSeconds -= 1
        } else if (breakCountdownSeconds == 0) {
            isBreakTimerRunning = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("study_reset_sub_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SubScreenTopBar(title = "📚 Study Reset", onBack = onClose)
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📚 Study reset",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"Look away from the screen, relax your shoulders, stand up and move for a minute.\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PrimaryButton(
                            text = if (isBreakTimerRunning) "Resetting (${breakCountdownSeconds}s)" else "Start 1-Min Break",
                            onClick = {
                                if (breakCountdownSeconds == 0) breakCountdownSeconds = 60
                                isBreakTimerRunning = !isBreakTimerRunning
                            },
                            modifier = Modifier.weight(1f),
                            testTag = "start_study_break_timer"
                        )
                        SecondaryButton(
                            text = "Notify",
                            onClick = onTriggerStudyNotification,
                            testTag = "test_study_notification_button"
                        )
                    }
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Enable Study Reset", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Optional reminder while studying or reading",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = preferences.studyResetEnabled,
                        onCheckedChange = { enabled ->
                            onUpdateStudyReset(enabled, preferences.studyResetIntervalMinutes)
                        }
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Break Interval",
                subtitle = "Choose how often you'd like a posture & eye break"
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(30, 45, 60).forEach { mins ->
                    val isSelected = preferences.studyResetIntervalMinutes == mins
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                customMinutesInput = mins.toString()
                                onUpdateStudyReset(preferences.studyResetEnabled, mins)
                            }
                            .minimumInteractiveComponentSize()
                            .testTag("study_interval_$mins"),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$mins minutes",
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Custom Interval Option
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = customMinutesInput,
                            onValueChange = { customMinutesInput = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Custom minutes") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(
                            onClick = {
                                val parsed = customMinutesInput.toIntOrNull()?.coerceIn(10, 180) ?: 45
                                onUpdateStudyReset(preferences.studyResetEnabled, parsed)
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Set Custom")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyRoutineGeneratorSubScreen(
    userName: String,
    routineBlocks: List<GeneratedRoutineBlock>,
    preferences: UserPreferencesEntity,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("daily_routine_generator_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SubScreenTopBar(title = "✨ Today's Routine", onBack = onClose)
        }

        item {
            Text(
                text = "TODAY'S ROUTINE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Crafted gently for $userName (${preferences.dailyTimeCommitmentMinutes} min movement • ${preferences.freeTimeWindow})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(routineBlocks, key = { it.periodTitle }) { block ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "${block.emoji} ${block.periodTitle}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    block.items.forEach { itemText ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = itemText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "\"Your routine should support your life — not take it over. Progress can look different every day.\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SubScreenTopBar(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag("subscreen_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
