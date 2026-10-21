package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.CalendarEventEntity
import com.example.data.MonthlyGoalEntity
import com.example.data.NowRecommendation
import com.example.data.OsTaskEntity
import com.example.data.RecentWinEntity
import com.example.data.TimelineBlockEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.BottomNavTab

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    greeting: String,
    formattedDate: String,
    preferences: UserPreferencesEntity,
    nowRecommendation: NowRecommendation,
    tasks: List<OsTaskEntity>,
    timelineBlocks: List<TimelineBlockEntity>,
    calendarEvents: List<CalendarEventEntity> = emptyList(),
    monthlyGoals: List<MonthlyGoalEntity> = emptyList(),
    recentWins: List<RecentWinEntity> = emptyList(),
    onSetEnergyLevel: (String) -> Unit,
    onSetDayMode: (String) -> Unit,
    onStartNowRecommendation: (NowRecommendation) -> Unit,
    onSkipNowRecommendation: (NowRecommendation) -> Unit,
    onStartTaskFocus: (OsTaskEntity) -> Unit,
    onToggleTask: (OsTaskEntity) -> Unit,
    onPostponeTask: (OsTaskEntity) -> Unit,
    onRescheduleTask: (OsTaskEntity, String, String) -> Unit,
    onAddTask: (
        title: String,
        subtitle: String,
        whyItMatters: String,
        domain: String,
        priorityBucket: String,
        durationMinutes: Int,
        energyRequired: String,
        scheduledTimeSlot: String,
        isTopThree: Boolean,
        topThreeSlot: Int
    ) -> Unit,
    onToggleTimelineBlock: (TimelineBlockEntity) -> Unit,
    onCalculateMinutesMission: (Int) -> String,
    onResolveConfusion: (String) -> Pair<String, ActiveSubScreen?>,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    onNavigateTab: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by rememberSaveable { mutableStateOf(false) }
    var showIHaveXMinutesDialog by rememberSaveable { mutableStateOf(false) }
    var showImConfusedDialog by rememberSaveable { mutableStateOf(false) }
    var showWhyExpanded by rememberSaveable { mutableStateOf(false) }
    var showOtherOptionsExpanded by rememberSaveable { mutableStateOf(false) }
    var showFullTimeline by rememberSaveable { mutableStateOf(false) }
    var taskToReschedule by rememberSaveable { mutableStateOf<Int?>(null) }

    val topThreeTasks = tasks.filter { it.isTopThree }.sortedBy { it.topThreeSlot }
    val otherActiveTasks = tasks.filter { !it.isTopThree && !it.isPostponed }
    val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("today_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. PERSONAL FEED COMMAND CENTER HEADER
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = greeting.uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.testTag("home_greeting_text")
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "“${nowRecommendation.contextualLine}”",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Compact ASTHA AI entry pill
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.AsthaAiSheet) }
                            .testTag("header_astha_ai_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "Open ASTHA AI",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ASTHA AI",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Effortless Energy & Mode Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("LOW" to "🌿 Low Energy", "NORMAL" to "✨ Normal", "HIGH" to "⚡ High Energy").forEach { (key, label) ->
                        val isSelected = preferences.currentEnergyLevel == key
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSetEnergyLevel(key) }
                                .testTag("energy_chip_${key.lowercase()}")
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }

                    val modes = listOf("School day", "Weekend", "Exam preparation", "Competition preparation")
                    modes.forEach { mode ->
                        val selected = preferences.currentDayMode == mode
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                1.dp,
                                if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSetDayMode(mode) }
                        ) {
                            Text(
                                text = mode,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. ONE LARGE DYNAMIC "YOUR NEXT MOVE" HERO CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("what_should_i_do_now_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = nowRecommendation.categoryBadge,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        StatusBadgeChip(
                            label = "${nowRecommendation.durationMinutes} min",
                            containerColor = WarmPeachLight,
                            contentColor = WarmPeachHighlight
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // My Day, My Way active rhythm pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = GentleSuccessLight,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("PLAN MY DAY")) }
                                .testTag("my_day_my_way_badge")
                        ) {
                            Text(
                                text = "My Day, My Way: ${nowRecommendation.selectedDayType} ✎",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GentleSuccessGreen,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Conflict Warning Banner when Rest/Family day overlaps with an urgent event
                    if (!nowRecommendation.conflictWarningMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WarmPeachLight,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("PLAN MY DAY")) }
                        ) {
                            Text(
                                text = nowRecommendation.conflictWarningMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmPeachHighlight,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Big scannable subject / project title (e.g. Krow • 20 min)
                    Text(
                        text = "${nowRecommendation.title} • ${nowRecommendation.durationMinutes} min",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Concise 1-2 line concrete description
                    Text(
                        text = nowRecommendation.subtitleShort,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Human-intelligent personal context sentence
                    Text(
                        text = nowRecommendation.humanHeadline,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progressive Disclosure: Tap to expand full steps & why
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { showWhyExpanded = !showWhyExpanded }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (showWhyExpanded) "HOW & WHY (Tap to collapse)" else "Tap for exact step-by-step & why ▾",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = nowRecommendation.priorityDomain,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            if (showWhyExpanded) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = nowRecommendation.concreteAction,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Why now: ${nowRecommendation.whyReason}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // Expandable Alternate Options ("OTHER OPTIONS")
                    if (showOtherOptionsExpanded && nowRecommendation.alternateOptions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "OTHER BALANCED OPTIONS RIGHT NOW",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                nowRecommendation.alternateOptions.forEach { alt ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onSkipNowRecommendation(nowRecommendation) }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "• $alt",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Text(
                                            text = "Switch →",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Obvious Primary Action: [ START ] + [ OTHER OPTIONS ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onStartNowRecommendation(nowRecommendation) },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .weight(1.6f)
                                .height(56.dp)
                                .minimumInteractiveComponentSize()
                                .testTag("start_now_recommendation_button")
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "START",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                showOtherOptionsExpanded = true
                                onSkipNowRecommendation(nowRecommendation)
                            },
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp)
                                .testTag("skip_recommendation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Other options",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "OTHER OPTIONS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Subtle helper row inside hero card
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showIHaveXMinutesDialog = true },
                            modifier = Modifier.testTag("i_have_x_minutes_button")
                        ) {
                            Text("⏱️ I only have X minutes")
                        }
                        TextButton(
                            onClick = { showImConfusedDialog = true },
                            modifier = Modifier.testTag("im_confused_button")
                        ) {
                            Text("🧭 I'm overwhelmed / confused")
                        }
                    }
                }
            }
        }

        // 2B. THIS WEEK (Upcoming Calendar Deadlines & Calm Countdowns) + THIS MONTH (Monthly Focus)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // THIS WEEK CARD
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("CALENDAR")) }
                        .testTag("home_this_week_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "THIS WEEK",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val upcomingEvents = calendarEvents.take(3)
                        if (upcomingEvents.isEmpty()) {
                            Text("• Physics test — Tomorrow", style = MaterialTheme.typography.bodySmall)
                            Text("• NCSC prep — 5 days", style = MaterialTheme.typography.bodySmall)
                            Text("• Krow feedback — 3 days", style = MaterialTheme.typography.bodySmall)
                        } else {
                            upcomingEvents.forEach { ev ->
                                val diff = calculateDaysBetween(todayStr, ev.dateString)
                                val label = when {
                                    diff <= 0 -> "Today"
                                    diff == 1 -> "Tomorrow"
                                    else -> "$diff days"
                                }
                                Text(
                                    text = "• ${ev.title.take(18)} — $label",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Open Calendar →",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // THIS MONTH CARD
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("MONTHLY GOALS")) }
                        .testTag("home_this_month_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "THIS MONTH",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val topMonthly = monthlyGoals.take(3)
                        if (topMonthly.isEmpty()) {
                            Text("• Validate Krow with users", style = MaterialTheme.typography.bodySmall)
                            Text("• NCSC presentation ready", style = MaterialTheme.typography.bodySmall)
                            Text("• Strengthen Physics", style = MaterialTheme.typography.bodySmall)
                        } else {
                            topMonthly.forEach { mg ->
                                Text(
                                    text = "• ${mg.title.take(24)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Monthly Goals →",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        // 2C. RECENT WINS & VISIBLE MOMENTUM ("What changed because of my effort?")
        if (recentWins.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = GentleSuccessLight.copy(alpha = 0.65f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recent_wins_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "RECENT WINS • VISIBLE PROGRESS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = GentleSuccessGreen
                            )
                            Text(
                                text = "Momentum",
                                style = MaterialTheme.typography.labelSmall,
                                color = GentleSuccessGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        recentWins.take(3).forEach { win ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✓ ${win.title}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "— ${win.whatChangedDetail}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. CONTEXTUAL LIFE MODES (Effortless Horizontal Strip)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.AfterSchoolResetSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("after_school_reset_button")
                ) {
                    Text("🏠 After-School Reset")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.SchoolModeSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("school_mode_button")
                ) {
                    Text("🏫 School Mode")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.MorningModeSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("morning_mode_button")
                ) {
                    Text("☀️ What Should I Study Today?")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.ChemistryMissionSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("chemistry_mission_quick_button")
                ) {
                    Text("🎯 Chemistry 100 Mission")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.AcademicExcellenceSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("academic_excellence_quick_button")
                ) {
                    Text("📚 Academic Engine")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.ScienceExhibitionSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("science_exhibition_quick_button")
                ) {
                    Text("🔬 Science Exhibition")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.ScholarshipStrategySheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("scholarships_quick_button")
                ) {
                    Text("🎓 MEXT & Scholarships")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.AngerOverwhelmPauseSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("overwhelm_pause_quick_button")
                ) {
                    Text("🕊️ Calm Pause Protocol")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.EntertainmentWalletSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("entertainment_wallet_button")
                ) {
                    Text("🎬 ${preferences.entertainmentTitle} Wallet")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.SetbackRecoverySheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("self_respect_reframe_button")
                ) {
                    Text("🛡️ Reframe Setback")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.ConfidenceLabSheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("confidence_lab_button")
                ) {
                    Text("🎤 Confidence Lab")
                }
                OutlinedButton(
                    onClick = { onOpenSubScreen(ActiveSubScreen.FinancialLiteracySheet) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("value_creation_button")
                ) {
                    Text("📈 Value & Budget")
                }
            }
        }

        // 4. TODAY'S TOP 3 (Clean, Scannable Cards)
        item {
            SectionHeader(
                title = "TODAY'S TOP 3",
                subtitle = "1. Academic  •  2. Project/Profile  •  3. Wellness",
                actionText = "+ Add Task",
                onActionClick = { showAddTaskDialog = true }
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (topThreeTasks.isEmpty() && otherActiveTasks.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Your day is clear.", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = { showAddTaskDialog = true }) {
                            Text("Add Task")
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val displayTasks = (topThreeTasks + otherActiveTasks).take(3)
                    displayTasks.forEachIndexed { idx, task ->
                        TopThreeTaskCard(
                            indexNumber = String.format(java.util.Locale.US, "%02d", idx + 1),
                            task = task,
                            onStart = { onStartTaskFocus(task) },
                            onToggleComplete = { onToggleTask(task) },
                            onPostpone = { onPostponeTask(task) },
                            onReschedule = { taskToReschedule = task.id }
                        )
                    }

                    val repeatedlyPostponed = (topThreeTasks + otherActiveTasks).firstOrNull { it.skipCount >= 2 || it.isPostponed }
                    if (repeatedlyPostponed != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelfImprovement,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Postponement Triage: '${repeatedlyPostponed.title.take(24)}'",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    Text(
                                        text = "Repeated postponing usually means the step is too big. Want to do a gentle 5-minute micro-step?",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        onStartTaskFocus(
                                            repeatedlyPostponed.copy(
                                                title = "5-Min Step: ${repeatedlyPostponed.title}",
                                                durationMinutes = 5
                                            )
                                        )
                                    }
                                ) {
                                    Text("5-Min Step", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. PROGRESSIVE DISCLOSURE: SMART DAILY TIMELINE & RECOVERY
        item {
            SectionHeader(
                title = "DAILY FLOW & RECOVERY",
                subtitle = if (showFullTimeline) "Tap any block to mark complete" else "Showing next key blocks • Expand to see full day",
                actionText = if (showFullTimeline) "Collapse" else "Full Plan (${timelineBlocks.size})",
                onActionClick = { showFullTimeline = !showFullTimeline }
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val visibleBlocks = if (showFullTimeline) {
                        timelineBlocks
                    } else {
                        timelineBlocks.filter { !it.isCompleted }.take(3).ifEmpty { timelineBlocks.take(3) }
                    }
                    visibleBlocks.forEach { block ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onToggleTimelineBlock(block) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (block.isCompleted) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                },
                                modifier = Modifier.width(86.dp)
                            ) {
                                Text(
                                    text = if (block.endTime.isNotBlank() && block.endTime != block.startTime) {
                                        "${block.startTime}–${block.endTime}"
                                    } else {
                                        block.startTime
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (block.isCompleted) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = block.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textDecoration = if (block.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                )
                                Text(
                                    text = block.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Icon(
                                imageVector = if (block.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = "Toggle ${block.title}",
                                tint = if (block.isCompleted) GentleSuccessGreen else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.RecoveryResetSheet) }
                            .testTag("timeline_reset_from_here_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Running late? Let's reset from here.",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Text(
                                text = "Recalculate",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // 6. QUICK ACTIONS (+ Task, Study, Build, Brain Dump, Wellness)
        item {
            SectionHeader(
                title = "QUICK JUMP",
                subtitle = "Direct access without clutter"
            )
            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionPill(
                    label = "+ Task",
                    onClick = { showAddTaskDialog = true },
                    testTag = "quick_action_add_task"
                )
                QuickActionPill(
                    label = "📅 Life Calendar",
                    onClick = { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("CALENDAR")) },
                    testTag = "quick_action_calendar"
                )
                QuickActionPill(
                    label = "✨ Plan My Day",
                    onClick = { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("PLAN MY DAY")) },
                    testTag = "quick_action_plan_my_day"
                )
                QuickActionPill(
                    label = "🌙 Close Today Gently",
                    onClick = { onOpenSubScreen(ActiveSubScreen.CloseTodayGentlySheet) },
                    testTag = "quick_action_close_today"
                )
                QuickActionPill(
                    label = "🎯 Monthly Goals",
                    onClick = { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("MONTHLY GOALS")) },
                    testTag = "quick_action_monthly_goals"
                )
                QuickActionPill(
                    label = "🔭 Long-Term Vision",
                    onClick = { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("LONG-TERM VISION")) },
                    testTag = "quick_action_long_term_vision"
                )
                QuickActionPill(
                    label = "📚 Study",
                    onClick = { onNavigateTab(BottomNavTab.STUDY) },
                    testTag = "quick_action_study"
                )
                QuickActionPill(
                    label = "🚀 Build",
                    onClick = { onNavigateTab(BottomNavTab.BUILD) },
                    testTag = "quick_action_build"
                )
                QuickActionPill(
                    label = "🧠 Brain Dump",
                    onClick = { onOpenSubScreen(ActiveSubScreen.BrainDumpSheet) },
                    testTag = "quick_action_brain_dump"
                )
                QuickActionPill(
                    label = "🌿 Wellness",
                    onClick = { onNavigateTab(BottomNavTab.WELLNESS) },
                    testTag = "quick_action_wellness"
                )
                QuickActionPill(
                    label = "📊 Week in Review",
                    onClick = { onOpenSubScreen(ActiveSubScreen.WeeklyReviewSheet) },
                    testTag = "quick_action_weekly_review"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        var titleInput by rememberSaveable { mutableStateOf("") }
        var subtitleInput by rememberSaveable { mutableStateOf("") }
        var domainInput by rememberSaveable { mutableStateOf("ACADEMIC") }
        var priorityInput by rememberSaveable { mutableStateOf("MUST DO") }
        var durationInput by rememberSaveable { mutableIntStateOf(30) }
        var slotInput by rememberSaveable { mutableStateOf("4:30 PM") }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("Add Task to ASTHA OS") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Task title (e.g., Physics DPP #4)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = subtitleInput,
                        onValueChange = { subtitleInput = it },
                        label = { Text("Concrete next step (How to do it)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("ACADEMIC", "PROJECT", "WELLNESS").forEach { dom ->
                            OutlinedButton(
                                onClick = { domainInput = dom },
                                border = BorderStroke(
                                    if (domainInput == dom) 2.dp else 1.dp,
                                    if (domainInput == dom) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text(dom.take(4), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(15, 30, 45, 60).forEach { mins ->
                            OutlinedButton(
                                onClick = { durationInput = mins },
                                border = BorderStroke(
                                    if (durationInput == mins) 2.dp else 1.dp,
                                    if (durationInput == mins) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text("${mins}m", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            onAddTask(
                                titleInput,
                                subtitleInput,
                                "Added for today's $domainInput focus.",
                                domainInput,
                                priorityInput,
                                durationInput,
                                preferences.currentEnergyLevel,
                                slotInput,
                                false,
                                0
                            )
                            showAddTaskDialog = false
                        }
                    }
                ) {
                    Text("Save Task")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Reschedule Task Dialog
    if (taskToReschedule != null) {
        val target = tasks.find { it.id == taskToReschedule }
        if (target != null) {
            var newTime by rememberSaveable { mutableStateOf(target.scheduledTimeSlot) }
            var newBucket by rememberSaveable { mutableStateOf(target.priorityBucket) }
            AlertDialog(
                onDismissRequest = { taskToReschedule = null },
                title = { Text("Reschedule or Reprioritize") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(target.title, style = MaterialTheme.typography.titleSmall)
                        OutlinedTextField(
                            value = newTime,
                            onValueChange = { newTime = it },
                            label = { Text("New time slot (e.g., 5:00 PM or Tomorrow)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Recovery Category:", style = MaterialTheme.typography.labelMedium)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("MUST DO", "SHOULD DO", "CAN POSTPONE", "CAN DELETE").forEach { b ->
                                OutlinedButton(
                                    onClick = { newBucket = b },
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(
                                        if (newBucket == b) 2.dp else 1.dp,
                                        if (newBucket == b) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                    )
                                ) {
                                    Text(b)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onRescheduleTask(target, newTime, newBucket)
                            taskToReschedule = null
                        }
                    ) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskToReschedule = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    // "I HAVE X MINUTES" Modal
    if (showIHaveXMinutesDialog) {
        var selectedMins by rememberSaveable { mutableIntStateOf(15) }
        val calculatedMission = onCalculateMinutesMission(selectedMins)

        AlertDialog(
            onDismissRequest = { showIHaveXMinutesDialog = false },
            title = { Text("I Have X Minutes") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Pick your available time window. ASTHA AI calculates the highest-value action that fits:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 15, 20, 30, 45, 60, 90).forEach { m ->
                            val isSel = selectedMins == m
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedMins = m }
                            ) {
                                Text(
                                    text = if (m == 90) "90+m" else "${m}m",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = calculatedMission,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showIHaveXMinutesDialog = false
                        onOpenSubScreen(
                            ActiveSubScreen.FocusMode(
                                taskTitle = "$selectedMins-Minute Targeted Mission",
                                purpose = calculatedMission,
                                durationMinutes = selectedMins,
                                currentStepText = "Execute your $selectedMins-minute high-value block"
                            )
                        )
                    }
                ) {
                    Text("Start $selectedMins-Min Block")
                }
            },
            dismissButton = {
                TextButton(onClick = { showIHaveXMinutesDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // "I'M CONFUSED" Modal
    if (showImConfusedDialog) {
        val confusionOptions = listOf(
            "I don't know what to study",
            "I don't know how to study",
            "I have too many tasks",
            "I'm too tired",
            "I'm distracted",
            "I don't understand the topic",
            "I don't know where to start"
        )
        var chosenOption by rememberSaveable { mutableStateOf(confusionOptions.first()) }
        val (resolutionText, targetSubScreen) = onResolveConfusion(chosenOption)

        AlertDialog(
            onDismissRequest = { showImConfusedDialog = false },
            title = { Text("🧭 I'm Confused — Let's Simplify") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "What feels overwhelming right now? ASTHA AI converts confusion into ONE concrete next action:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    confusionOptions.forEach { opt ->
                        val isSel = chosenOption == opt
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(
                                1.dp,
                                if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { chosenOption = opt }
                        ) {
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = resolutionText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showImConfusedDialog = false
                        if (targetSubScreen != null) {
                            onOpenSubScreen(targetSubScreen)
                        }
                    }
                ) {
                    Text("Do This One Thing")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImConfusedDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun TopThreeTaskCard(
    indexNumber: String,
    task: OsTaskEntity,
    onStart: () -> Unit,
    onToggleComplete: () -> Unit,
    onPostpone: () -> Unit,
    onReschedule: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("top_task_card_${task.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (task.isCompleted) GentleSuccessGreen else MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = indexNumber,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (task.isCompleted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(
                        text = "${task.domain}  •  ${task.durationMinutes} min  •  ${task.scheduledTimeSlot}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onToggleComplete) {
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = "Complete ${task.title}",
                        tint = if (task.isCompleted) GentleSuccessGreen else MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = task.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!task.isCompleted) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onStart,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Focus", style = MaterialTheme.typography.labelMedium)
                    }
                    OutlinedButton(
                        onClick = onPostpone,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Postpone", style = MaterialTheme.typography.labelSmall)
                    }
                    OutlinedButton(
                        onClick = onReschedule,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reschedule", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionPill(
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        tonalElevation = 1.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .minimumInteractiveComponentSize()
            .testTag(testTag)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}
