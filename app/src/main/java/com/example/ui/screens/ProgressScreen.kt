package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ConfidenceEntryEntity
import com.example.data.DefaultWellnessData
import com.example.ui.components.SectionHeader
import com.example.ui.components.WeeklyProgressCard
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.SoftBlueHydrate
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.SoftSageGreen
import com.example.viewmodel.WeeklyStatsUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgressScreen(
    stats: WeeklyStatsUi,
    nextWeekFocus: String,
    confidenceEntries: List<ConfidenceEntryEntity>,
    onSelectNextWeekFocus: (String) -> Unit,
    onOpenConfidenceCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("progress_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Your week",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Celebrating consistency, self-care, and showing up for yourself.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Consistency Highlight Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Consistency",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your strongest habit this week:\n${stats.strongestHabitTitle}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You're building a routine. One small step at a time.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Weekly Progress Visualizations
        item {
            WeeklyProgressCard(
                title = "Movement",
                completed = stats.movementCompletedDays,
                target = stats.movementTargetDays,
                accentColor = SoftLavender
            )
        }

        item {
            WeeklyProgressCard(
                title = "Hydration reminders",
                completed = stats.hydrationCompletedCount,
                target = stats.hydrationTargetCount,
                accentColor = SoftBlueHydrate
            )
        }

        item {
            WeeklyProgressCard(
                title = "Skincare",
                completed = stats.skincareCompletedCount,
                target = stats.skincareTargetCount,
                accentColor = GentleSuccessGreen
            )
        }

        item {
            WeeklyProgressCard(
                title = "Sleep routine",
                completed = stats.sleepCompletedDays,
                target = stats.sleepTargetDays,
                accentColor = SoftLavender
            )
        }

        item {
            WeeklyProgressCard(
                title = "Confidence check-ins",
                completed = stats.confidenceCompletedDays,
                target = stats.confidenceTargetDays,
                accentColor = SoftSageGreen
            )
        }

        // Weekly Reflection ("Your week in review 🌱")
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "Your week in review 🌱",
                subtitle = "A gentle look at how you cared for yourself"
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
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ReviewSummaryBlock(
                        category = "MOVEMENT",
                        summary = "You moved ${stats.movementCompletedDays} times this week."
                    )
                    ReviewSummaryBlock(
                        category = "SELF-CARE",
                        summary = "You completed ${stats.skincareCompletedCount} skincare routines."
                    )
                    ReviewSummaryBlock(
                        category = "SLEEP",
                        summary = "You followed your wind-down routine ${stats.sleepCompletedDays} times."
                    )
                    ReviewSummaryBlock(
                        category = "CONFIDENCE",
                        summary = "You checked in with yourself ${stats.confidenceCompletedDays} times."
                    )
                }
            }
        }

        // Something You Did Well
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Something you did well",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Add check-in",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onOpenConfidenceCheckIn() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (confidenceEntries.isEmpty()) {
                        Text(
                            text = "Give yourself one minute today. Tap 'Add check-in' to record something you're proud of.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    } else {
                        Text(
                            text = "\"${stats.latestReflectionText}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Next Week Focus Selection
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "Next week, focus on one small thing.",
                subtitle = "Tap to choose your gentle intention for next week"
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DefaultWellnessData.nextWeekFocusOptions.forEach { option ->
                    val isSelected = nextWeekFocus == option
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectNextWeekFocus(option) }
                            .minimumInteractiveComponentSize()
                            .testTag("next_week_focus_${option.lowercase().replace(" ", "_")}"),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = option,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ReviewSummaryBlock(
    category: String,
    summary: String
) {
    Column {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
