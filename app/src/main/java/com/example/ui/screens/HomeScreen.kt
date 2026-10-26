package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CalendarEventEntity
import com.example.data.DailyHabitEntity
import com.example.data.MonthlyGoalEntity
import com.example.data.NowRecommendation
import com.example.data.OsTaskEntity
import com.example.data.RecentWinEntity
import com.example.data.TimelineBlockEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayConcentricRingsCard
import com.example.ui.components.ClayIconBadge
import com.example.ui.components.ClayPill
import com.example.ui.components.ClayPillButton
import com.example.ui.components.ClayStatCard
import com.example.ui.theme.ClayCoralAction
import com.example.ui.theme.ClayCoralLight
import com.example.ui.theme.ClayDeepPurple
import com.example.ui.theme.ClayLavenderCard
import com.example.ui.theme.ClayMintCard
import com.example.ui.theme.ClayPaleLavenderSurface
import com.example.ui.theme.ClayPeach
import com.example.ui.theme.ClayPeachCard
import com.example.ui.theme.ClayPrimaryLavender
import com.example.ui.theme.ClayPrimaryText
import com.example.ui.theme.ClayRoseCard
import com.example.ui.theme.ClaySecondaryText
import com.example.ui.theme.ClaySkyBlue
import com.example.ui.theme.ClaySoftMint
import com.example.ui.theme.ClayWarmIvory
import com.example.ui.theme.GentleSuccessGreen
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.BottomNavTab
import java.util.Calendar

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
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
    habits: List<DailyHabitEntity> = emptyList(),
    onToggleHabit: ((DailyHabitEntity) -> Unit)? = null,
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
    var showEnergyCheckIn by rememberSaveable { mutableStateOf(true) }
    var showSecondaryActionSheet by rememberSaveable { mutableStateOf(false) }
    var showSeeAllTasksSheet by rememberSaveable { mutableStateOf(false) }
    var showAddTaskDialog by rememberSaveable { mutableStateOf(false) }

    // Secondary action options (I'm distracted, I have less time)
    var showLessTimeDialog by rememberSaveable { mutableStateOf(false) }
    var selectedLessMinutes by rememberSaveable { mutableIntStateOf(15) }

    val activeTasks = tasks.filter { !it.isCompleted && !it.isPostponed }
    val topPriorityTasks = activeTasks.filter { it.isTopThree }.sortedBy { it.topThreeSlot }.take(3)
    val displayTasks = if (topPriorityTasks.isNotEmpty()) topPriorityTasks else activeTasks.take(3)

    // Upcoming event filtering: find today/imminent event if any
    val upcomingEvent = calendarEvents.firstOrNull { it.attendanceOrCompletionStatus != "COMPLETED" }

    // Time & Day detection for Astha's personal life operating rhythm:
    // - Morning (at or before 6 AM): Know immediately what to do right now
    // - 10:00 PM: Today's productivity summary + tomorrow's queued tasks
    // - Sunday: Weekly planning ritual to schedule the week for high productivity
    // - Month boundary: Monthly horizon schedule & progress toward goals
    val currentCal = remember { Calendar.getInstance() }
    val currentHour = currentCal.get(Calendar.HOUR_OF_DAY)
    val currentDayOfWeek = currentCal.get(Calendar.DAY_OF_WEEK)
    val currentDayOfMonth = currentCal.get(Calendar.DAY_OF_MONTH)
    val daysInMonth = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val autoRhythm = when {
        currentDayOfWeek == Calendar.SUNDAY && currentHour in 11..21 -> "SUNDAY"
        currentDayOfMonth >= daysInMonth - 2 || currentDayOfMonth == 1 -> "MONTH"
        currentHour in 4..11 -> "MORNING"
        currentHour >= 21 || currentHour < 4 -> "NIGHT"
        else -> "MORNING"
    }
    var selectedRhythm by rememberSaveable { mutableStateOf(autoRhythm) }

    // Contextual greeting supporting line
    val greetingHeadline = when {
        greeting.contains("evening", ignoreCase = true) -> "Good evening."
        greeting.contains("afternoon", ignoreCase = true) -> "Good afternoon."
        else -> "Good morning."
    }
    val supportingLine = when {
        greeting.contains("evening", ignoreCase = true) -> "Your evening is still yours."
        greeting.contains("afternoon", ignoreCase = true) -> "Stay steady. You're doing great."
        else -> "Know what matters. Do the next right thing."
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ClayWarmIvory)
            .testTag("today_screen_list"),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // -------------------------------------------------------------
        // 1. WELCOMING HEADER
        // -------------------------------------------------------------
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = greetingHeadline,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = ClayPrimaryText,
                            modifier = Modifier.testTag("home_greeting_text")
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = supportingLine,
                            style = MaterialTheme.typography.bodyLarge,
                            color = ClaySecondaryText,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.labelMedium,
                            color = ClayDeepPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Subtle ASAA AI Badge
                        Surface(
                            onClick = { onOpenSubScreen(ActiveSubScreen.AsthaAiSheet) },
                            shape = RoundedCornerShape(18.dp),
                            color = ClayPaleLavenderSurface,
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier.testTag("open_astha_ai_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = "ASAA AI",
                                    tint = ClayDeepPurple,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "ASAA",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayDeepPurple
                                )
                            }
                        }

                        // Delightful Clay Personal Avatar
                        Surface(
                            shape = CircleShape,
                            color = ClayPaleLavenderSurface,
                            shadowElevation = 3.dp,
                            border = BorderStroke(2.dp, Color.White),
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .clickable { onNavigateTab(BottomNavTab.PROFILE) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = preferences.userName.take(1).ifBlank { "A" }.uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayDeepPurple
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 2. CONCENTRIC 3D ACTIVITY RINGS CARD (SCREENSHOT 1 CENTERPIECE)
        // -------------------------------------------------------------
        item {
            ClayConcentricRingsCard(
                studyProgress = 0.79f,
                buildProgress = 0.52f,
                wellnessProgress = 0.90f,
                centerValue = "742",
                centerUnit = "flow score today",
                studyLabel = "Study 79%",
                buildLabel = "Build 52%",
                wellnessLabel = "Rhythm 90%",
                onCardClick = { onNavigateTab(BottomNavTab.STUDY) },
                testTag = "home_concentric_rings_card"
            )
        }

        // -------------------------------------------------------------
        // 3. QUICK 3 PILLOWY STAT CARDS (STEPS, DISTANCE, HEART RATE)
        // -------------------------------------------------------------
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 1: Study PCM (Mint Card)
                ClayStatCard(
                    title = "Study PCM",
                    value = "2h 15m",
                    icon = Icons.Default.Schedule,
                    containerColor = ClayMintCard,
                    iconColor = Color(0xFF285E3D),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(BottomNavTab.STUDY) },
                    testTag = "quick_stat_study"
                )

                // Stat 2: Build Krow (Peach Card)
                ClayStatCard(
                    title = "Build Krow",
                    value = "Stage 2",
                    icon = Icons.Default.Code,
                    containerColor = ClayPeachCard,
                    iconColor = Color(0xFF9A4518),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(BottomNavTab.BUILD) },
                    testTag = "quick_stat_build"
                )

                // Stat 3: Wellness & Joy (Rose Card)
                ClayStatCard(
                    title = "Dance / Joy",
                    value = "20 min",
                    icon = Icons.Default.Favorite,
                    containerColor = ClayRoseCard,
                    iconColor = Color(0xFF993B47),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(BottomNavTab.WELLNESS) },
                    testTag = "quick_stat_wellness"
                )
            }
        }

        // -------------------------------------------------------------
        // 4. ENERGY CHECK-IN (Compact, tactile, skippable)
        // -------------------------------------------------------------
        if (showEnergyCheckIn) {
            item {
                ClayCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    containerColor = Color.White,
                    elevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "How are you feeling?",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Text(
                                text = "Skip",
                                style = MaterialTheme.typography.labelSmall,
                                color = ClaySecondaryText,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showEnergyCheckIn = false }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "LOW" to "Low energy",
                                "NORMAL" to "Normal",
                                "HIGH" to "High energy"
                            ).forEach { (levelKey, label) ->
                                val isSelected = preferences.currentEnergyLevel.equals(levelKey, ignoreCase = true)
                                ClayPill(
                                    label = label,
                                    isSelected = isSelected,
                                    onClick = {
                                        onSetEnergyLevel(levelKey)
                                    },
                                    selectedContainerColor = when (levelKey) {
                                        "LOW" -> ClayPeach
                                        "HIGH" -> ClaySoftMint
                                        else -> ClayPaleLavenderSurface
                                    },
                                    selectedContentColor = when (levelKey) {
                                        "LOW" -> Color(0xFF9A4518)
                                        "HIGH" -> Color(0xFF285E3D)
                                        else -> ClayDeepPurple
                                    },
                                    modifier = Modifier.weight(1f),
                                    testTag = "energy_pill_${levelKey.lowercase()}"
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 5. ASTHA'S PERSONAL OPERATING RHYTHM SELECTOR
        // (Morning 6 AM, 10 PM Close, Sunday Week Schedule, Monthly Horizon)
        // -------------------------------------------------------------
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Operating Rhythm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Text(
                        text = when (selectedRhythm) {
                            "MORNING" -> "🌅 6:00 AM Launch"
                            "NIGHT" -> "🌙 10:00 PM Gentle Close"
                            "SUNDAY" -> "🗓️ Sunday Week Reset"
                            else -> "🎯 Monthly Horizons"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = ClayDeepPurple,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val rhythmPills = listOf(
                        "MORNING" to "🌅 Morning (6 AM)",
                        "NIGHT" to "🌙 10 PM Close",
                        "SUNDAY" to "🗓️ Sunday Week",
                        "MONTH" to "🎯 Monthly Goals"
                    )
                    rhythmPills.forEach { (key, label) ->
                        val isSelected = selectedRhythm == key
                        ClayPill(
                            label = label,
                            isSelected = isSelected,
                            onClick = { selectedRhythm = key },
                            selectedContainerColor = when (key) {
                                "MORNING" -> ClayPeachCard
                                "NIGHT" -> ClayPaleLavenderSurface
                                "SUNDAY" -> ClayMintCard
                                else -> ClayRoseCard
                            },
                            selectedContentColor = when (key) {
                                "MORNING" -> Color(0xFF9A4518)
                                "NIGHT" -> ClayDeepPurple
                                "SUNDAY" -> Color(0xFF285E3D)
                                else -> Color(0xFF993B47)
                            },
                            testTag = "rhythm_pill_${key.lowercase()}"
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 6. DEDICATED RHYTHM CARD (DYNAMICALLY REACTIVE TO SELECTED MODE)
        // -------------------------------------------------------------
        item {
            when (selectedRhythm) {
                "MORNING" -> {
                    // -------------------------------------------------
                    // MORNING LAUNCH CARD (BEFORE / AT 6:00 AM)
                    // "immediately what to do right now with zero friction"
                    // -------------------------------------------------
                    val task = nowRecommendation.taskToStart
                    val category = task?.domain?.ifBlank { "MISSION JEET" } ?: "CLASS 11 PCM"
                    val title = nowRecommendation.title.ifBlank { "Rotational Motion & Core Formulas" }
                    val instruction = task?.subtitle?.ifBlank {
                        "Try two easy questions, then move to one medium question."
                    } ?: "Focus purely on understanding this single concept before moving on."
                    val durationMinutes = nowRecommendation.durationMinutes.coerceAtLeast(15)

                    ClayCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("morning_now_card"),
                        shape = RoundedCornerShape(28.dp),
                        containerColor = ClayLavenderCard,
                        borderColor = Color.White.copy(alpha = 0.95f),
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = Color.White.copy(alpha = 0.85f),
                                    border = BorderStroke(1.dp, Color.White)
                                ) {
                                    Text(
                                        text = "🌅 6:00 AM • WHAT TO DO NOW",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ClayDeepPurple,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { showSecondaryActionSheet = true },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("task_secondary_action_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreHoriz,
                                        contentDescription = "More options",
                                        tint = ClaySecondaryText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(22.dp),
                                    color = Color.White.copy(alpha = 0.85f),
                                    border = BorderStroke(1.2.dp, Color.White),
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.size(62.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = ClayDeepPurple,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = ClayPrimaryText,
                                        lineHeight = 26.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = Color.White.copy(alpha = 0.7f),
                                            border = BorderStroke(0.8.dp, Color.White)
                                        ) {
                                            Text(
                                                text = "$durationMinutes min",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ClayDeepPurple,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = ClayPaleLavenderSurface,
                                            border = BorderStroke(0.8.dp, Color.White)
                                        ) {
                                            Text(
                                                text = category,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = ClayDeepPurple,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = instruction,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ClaySecondaryText,
                                lineHeight = 20.sp
                            )

                            // Morning quick habit checkoffs if available
                            val morningHabits = habits.take(3)
                            if (morningHabits.isNotEmpty() && onToggleHabit != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Morning Primers:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ClaySecondaryText
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    morningHabits.forEach { habit ->
                                        Surface(
                                            onClick = { onToggleHabit(habit) },
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (habit.completedToday) ClaySoftMint.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.7f),
                                            border = BorderStroke(1.dp, Color.White),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = if (habit.completedToday) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                                    contentDescription = null,
                                                    tint = if (habit.completedToday) GentleSuccessGreen else ClaySecondaryText,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = habit.title.take(12),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Medium,
                                                    color = ClayPrimaryText,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Coral Action Button
                            ClayPillButton(
                                text = "Start morning focus now (${durationMinutes}m)",
                                icon = Icons.Default.PlayArrow,
                                onClick = {
                                    onStartNowRecommendation(nowRecommendation)
                                },
                                containerColor = ClayCoralAction,
                                height = 48.dp,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "start_focus_primary_button"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(
                                    onClick = { onOpenSubScreen(ActiveSubScreen.MorningModeSheet) }
                                ) {
                                    Text(
                                        text = "Open full morning routine & schedule 🌅",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ClayDeepPurple,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                "NIGHT" -> {
                    // -------------------------------------------------
                    // 10:00 PM GENTLE CLOSE & TOMORROW'S TASKS CARD
                    // "at 10 pm what are tomorrows task and today day productivity"
                    // -------------------------------------------------
                    val completedCount = tasks.count { it.isCompleted }
                    val totalTasks = tasks.size
                    val completedHabits = habits.count { it.completedToday }
                    val tomorrowTasks = activeTasks.take(3)

                    ClayCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("night_close_card"),
                        shape = RoundedCornerShape(28.dp),
                        containerColor = ClayPaleLavenderSurface,
                        borderColor = Color.White.copy(alpha = 0.95f),
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.85f),
                                border = BorderStroke(1.dp, Color.White)
                            ) {
                                Text(
                                    text = "🌙 10:00 PM GENTLE CLOSE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ClayDeepPurple,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Today's Productivity Recap",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Zero guilt. You showed up today with heart Astha.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ClaySecondaryText
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 3 Clay Productivity Stat Cards
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFE8E2D7)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "$completedCount / $totalTasks",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = ClayCoralAction
                                        )
                                        Text(
                                            text = "Tasks Done",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ClaySecondaryText
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFE8E2D7)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "742",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = ClayDeepPurple
                                        )
                                        Text(
                                            text = "Flow Score",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ClaySecondaryText
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFE8E2D7)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "$completedHabits / ${habits.size.coerceAtLeast(1)}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF285E3D)
                                        )
                                        Text(
                                            text = "Habit Wins",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ClaySecondaryText
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Tomorrow's Queued Priorities",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (tomorrowTasks.isEmpty()) {
                                Text(
                                    text = "No pending tasks! Tomorrow's slate is pristine.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ClaySecondaryText
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    tomorrowTasks.forEachIndexed { index, taskItem ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = Color.White.copy(alpha = 0.85f),
                                            border = BorderStroke(1.dp, Color.White),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    modifier = Modifier.weight(1f),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = ClayCoralLight,
                                                        modifier = Modifier.size(22.dp)
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Text(
                                                                text = "${index + 1}",
                                                                style = MaterialTheme.typography.labelSmall,
                                                                fontWeight = FontWeight.Bold,
                                                                color = ClayCoralAction
                                                            )
                                                        }
                                                    }
                                                    Column {
                                                        Text(
                                                            text = taskItem.title,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = ClayPrimaryText,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                        Text(
                                                            text = taskItem.domain.ifBlank { "Class 11 PCM" },
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = ClaySecondaryText
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = "${taskItem.durationMinutes}m",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ClayDeepPurple
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            ClayPillButton(
                                text = "Complete 10 PM gentle close & lock tomorrow",
                                icon = Icons.Default.CheckCircle,
                                onClick = {
                                    onOpenSubScreen(ActiveSubScreen.CloseTodayGentlySheet)
                                },
                                containerColor = ClayCoralAction,
                                height = 48.dp,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "night_gentle_close_button"
                            )
                        }
                    }
                }

                "SUNDAY" -> {
                    // -------------------------------------------------
                    // SUNDAY WEEKLY RESET & SCHEDULING CARD
                    // "each sunday i have to schedule my week to make it productive"
                    // -------------------------------------------------
                    ClayCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sunday_schedule_card"),
                        shape = RoundedCornerShape(28.dp),
                        containerColor = ClayMintCard,
                        borderColor = Color.White.copy(alpha = 0.95f),
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.85f),
                                border = BorderStroke(1.dp, Color.White)
                            ) {
                                Text(
                                    text = "🗓️ SUNDAY RITUAL • WEEK PLANNER",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF285E3D),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Design Your High-Flow Week",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Map your weekly commitments across study, innovation, and joy.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ClaySecondaryText
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 3 Pillars Overview
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.9f),
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ClayIconBadge(
                                            icon = Icons.Default.Schedule,
                                            containerColor = ClayMintCard,
                                            iconColor = Color(0xFF285E3D),
                                            size = 36.dp,
                                            iconSize = 18.dp
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "1. Class 11 PCM Mastery (18h)",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ClayPrimaryText
                                            )
                                            Text(
                                                text = "Rotational Dynamics, Thermodynamics, Calculus",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ClaySecondaryText
                                            )
                                        }
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.9f),
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ClayIconBadge(
                                            icon = Icons.Default.Code,
                                            containerColor = ClayPeachCard,
                                            iconColor = Color(0xFF9A4518),
                                            size = 36.dp,
                                            iconSize = 18.dp
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "2. Krow Innovation Prototype (6h)",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ClayPrimaryText
                                            )
                                            Text(
                                                text = "Stage 2 audio hardware assembly & sensor tests",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ClaySecondaryText
                                            )
                                        }
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.9f),
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ClayIconBadge(
                                            icon = Icons.Default.Favorite,
                                            containerColor = ClayRoseCard,
                                            iconColor = Color(0xFF993B47),
                                            size = 36.dp,
                                            iconSize = 18.dp
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "3. Wellness, Kathak & Rest (Daily)",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ClayPrimaryText
                                            )
                                            Text(
                                                text = "20 min dance groove, restorative 8h sleep cycle",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ClaySecondaryText
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            ClayPillButton(
                                text = "Open Sunday Weekly Planner",
                                icon = Icons.Default.CalendarMonth,
                                onClick = {
                                    onOpenSubScreen(ActiveSubScreen.WeeklyReviewSheet)
                                },
                                containerColor = ClayCoralAction,
                                height = 48.dp,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "sunday_weekly_plan_button"
                            )
                        }
                    }
                }

                else -> {
                    // -------------------------------------------------
                    // MONTHLY HORIZON & GOALS TRACKER CARD
                    // "before each month to schedule what i am gonna do and how i am getting close to my goal"
                    // -------------------------------------------------
                    val displayGoals = if (monthlyGoals.isNotEmpty()) monthlyGoals else listOf(
                        com.example.data.MonthlyGoalEntity(
                            id = 101,
                            title = "Physics Mechanics Mastery (PYQs)",
                            domain = "ACADEMICS",
                            weeklyTarget = "Solve 25 advanced PYQs per week",
                            todaySmallestStep = "Derive moment of inertia for ring & disk",
                            progressStepsDone = 3,
                            progressStepsTotal = 4
                        ),
                        com.example.data.MonthlyGoalEntity(
                            id = 102,
                            title = "Krow Audio Prototype Stage 2",
                            domain = "PROJECT",
                            weeklyTarget = "Assemble breadboard circuitry & testing",
                            todaySmallestStep = "Wire microphone sensor to ESP32 board",
                            progressStepsDone = 2,
                            progressStepsTotal = 4
                        ),
                        com.example.data.MonthlyGoalEntity(
                            id = 103,
                            title = "Kathak Dance & Energetic Health",
                            domain = "WELLNESS",
                            weeklyTarget = "4 sessions per week (20 min each)",
                            todaySmallestStep = "Complete 20 min morning Kathak footwork",
                            progressStepsDone = 4,
                            progressStepsTotal = 5
                        )
                    )

                    ClayCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("monthly_goals_card"),
                        shape = RoundedCornerShape(28.dp),
                        containerColor = ClayPeachCard,
                        borderColor = Color.White.copy(alpha = 0.95f),
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.85f),
                                border = BorderStroke(1.dp, Color.White)
                            ) {
                                Text(
                                    text = "🎯 MONTHLY HORIZON & GOALS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF9A4518),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "How You're Getting Closer To Your Goals",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Schedule next month's focus & see milestones completed.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ClaySecondaryText
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                displayGoals.forEach { goal ->
                                    val progressFraction = (goal.progressStepsDone.toFloat() / goal.progressStepsTotal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
                                    val percentInt = (progressFraction * 100).toInt()

                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color.White.copy(alpha = 0.9f),
                                        border = BorderStroke(1.dp, Color.White),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = goal.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ClayPrimaryText,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(50),
                                                    color = ClayCoralLight,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                ) {
                                                    Text(
                                                        text = "$percentInt%",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = ClayCoralAction,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            LinearProgressIndicator(
                                                progress = { progressFraction },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(7.dp)
                                                    .clip(RoundedCornerShape(4.dp)),
                                                color = ClayCoralAction,
                                                trackColor = Color(0xFFF0EBE1)
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Next step: ${goal.todaySmallestStep}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = ClaySecondaryText,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = "${goal.progressStepsDone}/${goal.progressStepsTotal} milestones",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = ClayDeepPurple
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            ClayPillButton(
                                text = "Schedule month & review horizons",
                                icon = Icons.AutoMirrored.Filled.TrendingUp,
                                onClick = {
                                    onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("MONTHLY GOALS"))
                                },
                                containerColor = ClayCoralAction,
                                height = 48.dp,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "monthly_horizon_schedule_button"
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 4. TODAY'S PLAN (Compact, max 3 priority items, simple rows)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                containerColor = Color.White,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's plan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClayPrimaryText
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "See all (${tasks.count { !it.isCompleted }})",
                                style = MaterialTheme.typography.labelMedium,
                                color = ClayDeepPurple,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showSeeAllTasksSheet = true }
                                    .padding(4.dp)
                            )
                            IconButton(
                                onClick = { showAddTaskDialog = true },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add task",
                                    tint = ClayDeepPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (displayTasks.isEmpty()) {
                        Text(
                            text = "All clear! You're completely up to date.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ClaySecondaryText,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            displayTasks.forEach { taskItem ->
                                Surface(
                                    onClick = { onToggleTask(taskItem) },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (taskItem.isCompleted) ClaySoftMint.copy(alpha = 0.5f) else ClayPaleLavenderSurface.copy(alpha = 0.4f),
                                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("plan_task_row_${taskItem.id}")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            // Tactile Completion Checkbox
                                            Icon(
                                                imageVector = if (taskItem.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                                contentDescription = if (taskItem.isCompleted) "Completed" else "Incomplete",
                                                tint = if (taskItem.isCompleted) GentleSuccessGreen else ClaySecondaryText,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = taskItem.title,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = ClayPrimaryText,
                                                    textDecoration = if (taskItem.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                if (taskItem.domain.isNotBlank()) {
                                                    Text(
                                                        text = taskItem.domain,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = ClaySecondaryText
                                                    )
                                                }
                                            }
                                        }

                                        // Duration label
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color.White,
                                            border = BorderStroke(1.dp, Color(0xFFF0EBE1))
                                        ) {
                                            Text(
                                                text = "${taskItem.durationMinutes}m",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ClayDeepPurple,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 5. UPCOMING EVENT (Small, elegant, shown only when exists)
        // -------------------------------------------------------------
        if (upcomingEvent != null) {
            item {
                ClayCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    containerColor = ClaySkyBlue.copy(alpha = 0.5f),
                    borderColor = ClaySkyBlue,
                    elevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.Event,
                            containerColor = Color.White,
                            iconColor = Color(0xFF4A749E),
                            size = 38.dp,
                            iconSize = 20.dp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "UPCOMING",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A749E)
                            )
                            Text(
                                text = upcomingEvent.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            if (upcomingEvent.dateString.isNotBlank()) {
                                Text(
                                    text = upcomingEvent.dateString,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ClaySecondaryText
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------
    // MODAL BOTTOM SHEET: SECONDARY TASK ACTIONS (Clean progressive disclosure)
    // -----------------------------------------------------------------
    if (showSecondaryActionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSecondaryActionSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Adjust Next Step",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ClayPrimaryText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Adapt your plan based on real-time focus or constraints.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ClaySecondaryText
                )
                Spacer(modifier = Modifier.height(18.dp))

                // Option 1: I have less time
                Surface(
                    onClick = {
                        showSecondaryActionSheet = false
                        showLessTimeDialog = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = ClayPaleLavenderSurface.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.Schedule,
                            containerColor = Color.White,
                            iconColor = ClayDeepPurple
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "I have less time",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Text(
                                text = "Scale task to 10 or 15 minutes",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClaySecondaryText
                            )
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = ClaySecondaryText)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 2: I'm distracted / need recovery
                Surface(
                    onClick = {
                        showSecondaryActionSheet = false
                        onOpenSubScreen(ActiveSubScreen.DistractionRecoverySheet)
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = ClayPeach.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.SwapHoriz,
                            containerColor = Color.White,
                            iconColor = Color(0xFFC7696E)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "I'm distracted / Reset",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Text(
                                text = "Take a 2-min breather and reschedule guilt-free",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClaySecondaryText
                            )
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = ClaySecondaryText)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 3: Switch to another task
                Surface(
                    onClick = {
                        showSecondaryActionSheet = false
                        showSeeAllTasksSheet = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = ClaySoftMint.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.PlayArrow,
                            containerColor = Color.White,
                            iconColor = GentleSuccessGreen
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Choose a different task",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Text(
                                text = "Browse and select from your full plan",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClaySecondaryText
                            )
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = ClaySecondaryText)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // -----------------------------------------------------------------
    // MODAL BOTTOM SHEET: "SEE ALL TASKS"
    // -----------------------------------------------------------------
    if (showSeeAllTasksSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSeeAllTasksSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Task Plan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    IconButton(onClick = { showSeeAllTasksSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks) { taskItem ->
                        Surface(
                            onClick = { onToggleTask(taskItem) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (taskItem.isCompleted) ClaySoftMint.copy(alpha = 0.4f) else ClayPaleLavenderSurface.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (taskItem.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (taskItem.isCompleted) GentleSuccessGreen else ClaySecondaryText,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Column {
                                        Text(
                                            text = taskItem.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ClayPrimaryText,
                                            textDecoration = if (taskItem.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                        )
                                        if (taskItem.subtitle.isNotBlank()) {
                                            Text(
                                                text = taskItem.subtitle,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ClaySecondaryText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                ClayButton(
                                    text = "Focus",
                                    onClick = {
                                        showSeeAllTasksSheet = false
                                        onStartTaskFocus(taskItem)
                                    },
                                    containerColor = ClayDeepPurple,
                                    contentColor = Color.White,
                                    modifier = Modifier.height(38.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // -----------------------------------------------------------------
    // DIALOG: I HAVE LESS TIME
    // -----------------------------------------------------------------
    if (showLessTimeDialog) {
        AlertDialog(
            onDismissRequest = { showLessTimeDialog = false },
            title = {
                Text(
                    text = "Scale Down Session",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Choose a micro-focus sprint. Doing one small thing keeps your momentum alive without overwhelm.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(10, 15, 20).forEach { mins ->
                            ClayPill(
                                label = "${mins}m sprint",
                                isSelected = selectedLessMinutes == mins,
                                onClick = { selectedLessMinutes = mins },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                ClayButton(
                    text = "Start ${selectedLessMinutes}m Sprint",
                    onClick = {
                        showLessTimeDialog = false
                        nowRecommendation.taskToStart?.let {
                            onStartTaskFocus(it.copy(durationMinutes = selectedLessMinutes))
                        } ?: onStartNowRecommendation(nowRecommendation.copy(durationMinutes = selectedLessMinutes))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showLessTimeDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    // -----------------------------------------------------------------
    // DIALOG: ADD QUICK TASK
    // -----------------------------------------------------------------
    if (showAddTaskDialog) {
        var taskTitleInput by rememberSaveable { mutableStateOf("") }
        var taskSubtitleInput by rememberSaveable { mutableStateOf("") }
        var taskMinutesInput by rememberSaveable { mutableIntStateOf(20) }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = {
                Text(
                    text = "Add Task to Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = taskTitleInput,
                        onValueChange = { taskTitleInput = it },
                        label = { Text("Task Title") },
                        placeholder = { Text("e.g. Physics: Derivation 3") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = taskSubtitleInput,
                        onValueChange = { taskSubtitleInput = it },
                        label = { Text("Single concrete action") },
                        placeholder = { Text("e.g. Solve questions 4 & 5") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(15, 20, 30, 45).forEach { mins ->
                            ClayPill(
                                label = "${mins}m",
                                isSelected = taskMinutesInput == mins,
                                onClick = { taskMinutesInput = mins },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                ClayButton(
                    text = "Add to Plan",
                    onClick = {
                        if (taskTitleInput.isNotBlank()) {
                            onAddTask(
                                taskTitleInput.trim(),
                                taskSubtitleInput.trim(),
                                "Personal growth & academic step",
                                "TODAY",
                                "HIGH",
                                taskMinutesInput,
                                "NORMAL",
                                "EVENING",
                                true,
                                1
                            )
                            showAddTaskDialog = false
                        }
                    },
                    enabled = taskTitleInput.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}
