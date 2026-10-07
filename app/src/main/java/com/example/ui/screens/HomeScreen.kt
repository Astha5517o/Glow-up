package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.DailyHabitEntity
import com.example.data.DefaultWellnessData
import com.example.data.UserPreferencesEntity
import com.example.ui.components.HabitCard
import com.example.ui.components.ProgressCard
import com.example.ui.components.SectionHeader
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.BottomNavTab

@Composable
fun HomeScreen(
    greeting: String,
    preferences: UserPreferencesEntity,
    habits: List<DailyHabitEntity>,
    onToggleHabit: (DailyHabitEntity) -> Unit,
    onAddCustomHabit: (title: String, subtitle: String, emoji: String) -> Unit,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    onNavigateTab: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = habits.count { it.completedToday }
    val totalCount = habits.size
    val quoteIndex = remember(completedCount) {
        completedCount % DefaultWellnessData.supportiveQuotes.size
    }
    var showAddHabitDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Greeting & Tagline
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.testTag("home_greeting_text")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Take care of yourself. Feel good in yourself.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Today's Care Header & Progress Card
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "Today's care",
                actionText = "Daily Flow",
                onActionClick = { onOpenSubScreen(ActiveSubScreen.DailyRoutineGeneratorSheet) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            ProgressCard(
                completedCount = completedCount,
                totalCount = totalCount,
                supportiveQuote = DefaultWellnessData.supportiveQuotes[quoteIndex],
                onViewDailyRoutineClick = { onOpenSubScreen(ActiveSubScreen.DailyRoutineGeneratorSheet) }
            )
        }

        // Empty state banner when 0 habits are completed so far
        if (completedCount == 0) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🌱", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Your day is still yours.",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Start with one small thing whenever you're ready.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        }

        // Habit Cards List (Hydrate, Move, Glow, Sleep, Confidence + Custom)
        items(habits, key = { it.id }) { habit ->
            HabitCard(
                habit = habit,
                onCardClick = {
                    when (habit.category) {
                        "HYDRATE" -> onOpenSubScreen(ActiveSubScreen.HydrationSheet)
                        "MOVE" -> onNavigateTab(BottomNavTab.MOVE)
                        "GLOW" -> onNavigateTab(BottomNavTab.GLOW)
                        "SLEEP" -> onOpenSubScreen(ActiveSubScreen.SleepSheet)
                        "CONFIDENCE" -> onOpenSubScreen(ActiveSubScreen.ConfidenceCheckInSheet)
                        else -> onToggleHabit(habit)
                    }
                },
                onToggleComplete = { onToggleHabit(habit) }
            )
        }

        // Add Custom Habit Affordance
        item {
            OutlinedButton(
                onClick = { showAddHabitDialog = true },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_custom_habit_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Add a gentle habit",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Student Study Reset & Posture Break Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onOpenSubScreen(ActiveSubScreen.StudyResetSheet) }
                    .testTag("study_reset_home_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("📚", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Study reset",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (preferences.studyResetEnabled) {
                                "Every ${preferences.studyResetIntervalMinutes} min • Look away from the screen & relax your shoulders"
                            } else {
                                "Optional posture & screen break timer"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Open",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Core Philosophy Footer Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "💜 Gentle reminder",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"You don't need to change your body to deserve confidence. These habits are here to help you feel stronger, healthier, fresher, and more confident.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (showAddHabitDialog) {
        var habitTitle by rememberSaveable { mutableStateOf("") }
        var habitSubtitle by rememberSaveable { mutableStateOf("") }
        var habitEmoji by rememberSaveable { mutableStateOf("🌿") }

        AlertDialog(
            onDismissRequest = { showAddHabitDialog = false },
            title = { Text("Add a Gentle Habit") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = habitTitle,
                        onValueChange = { habitTitle = it },
                        label = { Text("Habit name (e.g., Fresh air walk)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = habitSubtitle,
                        onValueChange = { habitSubtitle = it },
                        label = { Text("Short note (e.g., 5 min outside)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Icon:", style = MaterialTheme.typography.bodyMedium)
                        listOf("🌿", "🍵", "📖", "🎵", "☀️", "🫶").forEach { emojiOption ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (habitEmoji == emojiOption) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { habitEmoji = emojiOption }
                            ) {
                                Text(
                                    text = emojiOption,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (habitTitle.isNotBlank()) {
                            onAddCustomHabit(habitTitle, habitSubtitle, habitEmoji)
                            showAddHabitDialog = false
                        }
                    }
                ) {
                    Text("Add Habit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHabitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
