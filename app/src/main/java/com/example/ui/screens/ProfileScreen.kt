package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.UserPreferencesEntity
import com.example.notifications.GlowUpNotificationHelper
import com.example.ui.components.NotificationCard
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.SettingsRow
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.BottomNavTab

@Composable
fun ProfileScreen(
    preferences: UserPreferencesEntity,
    onUpdatePreferences: ((UserPreferencesEntity) -> UserPreferencesEntity) -> Unit,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    onNavigateTab: (BottomNavTab) -> Unit,
    onTriggerSampleNotification: (GlowUpNotificationHelper.GentleNotificationSample) -> Unit,
    onResetTodaysHabits: () -> Unit,
    onExportData: () -> Unit,
    onDeleteAllData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by rememberSaveable { mutableStateOf(false) }
    var showMovementPrefsDialog by rememberSaveable { mutableStateOf(false) }
    var showPrivacyDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showNotificationCenterDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Profile Header Card ("Astha" & "My Routine")
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = preferences.userName.take(1).uppercase().ifBlank { "A" },
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preferences.userName.ifBlank { "Astha" },
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Take care of yourself. Feel good in yourself.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier.testTag("edit_name_greeting_button")
                        ) {
                            Text("Edit")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "My Routine",
                        onClick = { onOpenSubScreen(ActiveSubScreen.DailyRoutineGeneratorSheet) },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "profile_my_routine_button"
                    )
                }
            }
        }

        // Settings List (All 10 requested items)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            SectionHeader(title = "Settings & Personalization")
        }

        // 1. Notification settings
        item {
            SettingsRow(
                title = "Notification settings",
                subtitle = if (preferences.notificationsEnabled) "Gentle, non-judgmental reminders enabled" else "Notifications paused",
                checked = preferences.notificationsEnabled,
                onCheckedChange = { enabled ->
                    onUpdatePreferences { it.copy(notificationsEnabled = enabled) }
                },
                testTag = "settings_notifications"
            )
        }

        // 2. Reminder schedule
        item {
            SettingsRow(
                title = "Reminder schedule",
                subtitle = "Customize hydration break times (${if (preferences.hydrationPaused) "Paused" else "Active"})",
                onClick = { onOpenSubScreen(ActiveSubScreen.HydrationSheet) },
                testTag = "settings_reminder_schedule"
            )
        }

        // 3. Movement preferences
        item {
            SettingsRow(
                title = "Movement preferences",
                subtitle = "${preferences.dailyTimeCommitmentMinutes} min sessions • Best in the ${preferences.freeTimeWindow}",
                onClick = { showMovementPrefsDialog = true },
                testTag = "settings_movement_preferences"
            )
        }

        // 4. Skincare checklist
        item {
            SettingsRow(
                title = "Skincare checklist",
                subtitle = "Customize your morning & evening Glow steps",
                onClick = { onNavigateTab(BottomNavTab.GLOW) },
                testTag = "settings_skincare_checklist"
            )
        }

        // 5. Sleep schedule
        item {
            SettingsRow(
                title = "Sleep schedule",
                subtitle = "Wake ${preferences.wakeTime} • Wind-down ${preferences.windDownTime} • Sleep ${preferences.targetBedtime}",
                onClick = { onOpenSubScreen(ActiveSubScreen.SleepSheet) },
                testTag = "settings_sleep_schedule"
            )
        }

        // 6. Study reset
        item {
            SettingsRow(
                title = "Study reset",
                subtitle = if (preferences.studyResetEnabled) "Screen & posture break every ${preferences.studyResetIntervalMinutes} min" else "Off",
                onClick = { onOpenSubScreen(ActiveSubScreen.StudyResetSheet) },
                testTag = "settings_study_reset"
            )
        }

        // 7. Dark mode
        item {
            SettingsRow(
                title = "Dark mode",
                subtitle = "Soft evening charcoal palette",
                checked = preferences.darkModeEnabled,
                onCheckedChange = { dark ->
                    onUpdatePreferences { it.copy(darkModeEnabled = dark) }
                },
                testTag = "settings_dark_mode"
            )
        }

        // 8. Sound
        item {
            SettingsRow(
                title = "Sound",
                subtitle = "Gentle chime during timers and reminders",
                checked = preferences.soundEnabled,
                onCheckedChange = { sound ->
                    onUpdatePreferences { it.copy(soundEnabled = sound) }
                },
                testTag = "settings_sound"
            )
        }

        // 9. Vibration
        item {
            SettingsRow(
                title = "Vibration",
                subtitle = "Soft haptic feedback",
                checked = preferences.vibrationEnabled,
                onCheckedChange = { vib ->
                    onUpdatePreferences { it.copy(vibrationEnabled = vib) }
                },
                testTag = "settings_vibration"
            )
        }

        // 10. Privacy
        item {
            SettingsRow(
                title = "Privacy",
                subtitle = "100% local on-device storage • No body tracking or uploads",
                onClick = { showPrivacyDialog = true },
                testTag = "settings_privacy"
            )
        }

        // Gentle Notification Preview Section
        item {
            Spacer(modifier = Modifier.height(6.dp))
            SectionHeader(
                title = "Gentle Notification Previews",
                subtitle = "Test any reminder right now",
                actionText = if (showNotificationCenterDialog) "Hide" else "Show all",
                onActionClick = { showNotificationCenterDialog = !showNotificationCenterDialog }
            )
        }

        if (showNotificationCenterDialog) {
            items(GlowUpNotificationHelper.notificationSamples, key = { it.id }) { sample ->
                NotificationCard(
                    sample = sample,
                    onPreviewClick = { onTriggerSampleNotification(sample) }
                )
            }
        } else {
            item {
                NotificationCard(
                    sample = GlowUpNotificationHelper.notificationSamples.first(),
                    onPreviewClick = {
                        onTriggerSampleNotification(GlowUpNotificationHelper.notificationSamples.first())
                    }
                )
            }
        }

        // Data & Habit Actions
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Your Data & Routine Reset")
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton(
                    text = "Reset today's habits",
                    onClick = onResetTodaysHabits,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "reset_todays_habits_button"
                )
                SecondaryButton(
                    text = "Export my data",
                    onClick = onExportData,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "export_my_data_button"
                )
                OutlinedButton(
                    onClick = { showDeleteConfirmDialog = true },
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("delete_my_data_button")
                ) {
                    Text(
                        text = "Delete my data",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showEditProfileDialog) {
        var nameInput by rememberSaveable { mutableStateOf(preferences.userName) }
        var greetingInput by rememberSaveable { mutableStateOf(preferences.customGreeting) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Personalize Name & Greeting") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Your name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = greetingInput,
                        onValueChange = { greetingInput = it },
                        label = { Text("Custom greeting (leave blank for time-based)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUpdatePreferences {
                            it.copy(
                                userName = nameInput.trim().ifBlank { "Astha" },
                                customGreeting = greetingInput.trim()
                            )
                        }
                        showEditProfileDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showMovementPrefsDialog) {
        var chosenMins by rememberSaveable { mutableStateOf(preferences.dailyTimeCommitmentMinutes) }
        var chosenWindow by rememberSaveable { mutableStateOf(preferences.freeTimeWindow) }

        AlertDialog(
            onDismissRequest = { showMovementPrefsDialog = false },
            title = { Text("Movement Preferences") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Preferred workout duration:", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15, 20).forEach { m ->
                            OutlinedButton(
                                onClick = { chosenMins = m },
                                border = BorderStroke(
                                    if (chosenMins == m) 2.dp else 1.dp,
                                    if (chosenMins == m) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text("${m}m")
                            }
                        }
                    }
                    Text("When are you usually free?", style = MaterialTheme.typography.labelLarge)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Morning", "Afternoon", "Evening", "Flexible").forEach { w ->
                            OutlinedButton(
                                onClick = { chosenWindow = w },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(
                                    if (chosenWindow == w) 2.dp else 1.dp,
                                    if (chosenWindow == w) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text(w)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUpdatePreferences {
                            it.copy(
                                dailyTimeCommitmentMinutes = chosenMins,
                                freeTimeWindow = chosenWindow
                            )
                        }
                        showMovementPrefsDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMovementPrefsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy & Personal Care Promise") },
            text = {
                Text(
                    "GlowUp is a private personal space.\n\n" +
                        "• All habits, reminders, confidence reflections, and progress stay stored locally on your device.\n" +
                        "• GlowUp never counts calories, never tracks weight or BMI, and never uses facial recognition or body comparisons.\n" +
                        "• You can export or delete your local data at any time."
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Got it")
                }
            }
        )
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete All Data?") },
            text = {
                Text("This will permanently delete your saved reflections, custom habits, and preferences on this device and reset GlowUp to a fresh start.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteAllData()
                        showDeleteConfirmDialog = false
                    }
                ) {
                    Text("Delete Everything", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
