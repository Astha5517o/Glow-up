package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DailyHabitEntity
import com.example.data.DefaultWellnessData
import com.example.data.HydrationReminderEntity
import com.example.data.MovementRoutine
import com.example.data.SkincareStepEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.SoftBlueHydrate
import com.example.ui.theme.SoftBlueLight
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.SoftLavenderLight
import com.example.ui.theme.SoftSageGreen
import com.example.ui.theme.SoftSageLight
import com.example.ui.theme.WarmAmberAccent
import com.example.ui.theme.WarmAmberLight
import com.example.ui.components.SkeuoCard
import com.example.ui.theme.SkeuoBorderLight
import com.example.ui.theme.SkeuoBorderSubtle
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import com.example.viewmodel.ActiveSubScreen

/**
 * Main Wellness Dashboard UI with a clean, gentle aesthetic that displays:
 * 1. Today's Core Wellness Goals (Hydration, Sleep & Wind-Down, Movement, Mindfulness, Self-Care)
 * 2. Interactive Habit Cards with gentle spring completion, category badges & streaks
 * 3. Sanctuary Hero Header with tranquil illustration
 * 4. Filter chips & + Custom Habit dialog
 */
@Composable
fun WellnessDashboardUI(
    preferences: UserPreferencesEntity,
    habits: List<DailyHabitEntity>,
    skincareSteps: List<SkincareStepEntity>,
    hydrationReminders: List<HydrationReminderEntity> = emptyList(),
    onToggleHabit: (DailyHabitEntity) -> Unit,
    onAddCustomHabit: (String, String, String) -> Unit,
    onStartMovementRoutine: (MovementRoutine) -> Unit,
    onToggleHydrationReminder: ((HydrationReminderEntity) -> Unit)? = null,
    onResetTodaysHabits: (() -> Unit)? = null,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    onSelectSubSection: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable { mutableStateOf("ALL") }
    var showAddHabitDialog by rememberSaveable { mutableStateOf(false) }
    var showResetConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val completedHabitsCount = habits.count { it.completedToday }
    val totalHabitsCount = habits.size
    val progressFraction = if (totalHabitsCount > 0) completedHabitsCount.toFloat() / totalHabitsCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "wellness_progress"
    )

    val recommendedFitness = when (preferences.currentEnergyLevel) {
        "LOW" -> DefaultWellnessData.movementRoutines.last()
        "HIGH" -> DefaultWellnessData.movementRoutines[2]
        else -> DefaultWellnessData.movementRoutines[1]
    }

    val completedSkincareCount = skincareSteps.count { it.completedToday }
    val completedHydrationCount = hydrationReminders.count { it.completedToday }
    val totalHydrationCount = if (hydrationReminders.isNotEmpty()) hydrationReminders.size else 8

    val filteredHabits = when (selectedFilter) {
        "PENDING" -> habits.filter { !it.completedToday }
        "COMPLETED" -> habits.filter { it.completedToday }
        "HYDRATE" -> habits.filter { it.category == "HYDRATE" }
        "MOVE" -> habits.filter { it.category == "MOVE" }
        "GLOW & SLEEP" -> habits.filter { it.category == "GLOW" || it.category == "SLEEP" }
        "MIND" -> habits.filter { it.category == "CONFIDENCE" || it.category == "POSTURE" || it.category == "CUSTOM" }
        else -> habits
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("wellness_dashboard_root"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. HERO SANCTUARY BANNER
        item {
            SkeuoCard(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 3.dp,
                testTag = "wellness_hero_sanctuary_card"
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_wellness_sanctuary_1792761636537),
                        contentDescription = "Gentle wellness sanctuary illustration",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Soft gentle scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.55f)
                                    ),
                                    startY = 60f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = GentleSuccessLight.copy(alpha = 0.95f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "🌿 TODAY'S SANCTUARY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GentleSuccessGreen,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "Gentle alignment, ${preferences.userName.ifBlank { "Astha" }}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Take what you need today. Consistency is kindness, not pressure.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // 2. TODAY'S WELLNESS GOALS PROGRESS RING & AFFIRMATION
        item {
            SkeuoCard(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 3.dp,
                testTag = "wellness_goals_progress_card"
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TODAY'S WELLNESS GOALS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$completedHabitsCount of $totalHabitsCount habits completed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val progressMessage = when {
                                progressFraction == 1f && totalHabitsCount > 0 -> "All habits fulfilled! Rest deeply & guilt-free ✨"
                                progressFraction >= 0.5f -> "Gently moving forward. Honor what feels nourishing."
                                completedHabitsCount > 0 -> "Wonderful start. Every small step cares for your body."
                                else -> "Take a gentle breath and begin whenever you are ready."
                            }
                            Text(
                                text = progressMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Circular Progress Indicator
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(68.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.size(68.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                strokeWidth = 7.dp
                            )
                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier.size(68.dp),
                                color = if (completedHabitsCount == totalHabitsCount && totalHabitsCount > 0) GentleSuccessGreen else MaterialTheme.colorScheme.primary,
                                strokeWidth = 7.dp,
                                strokeCap = StrokeCap.Round
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${(animatedProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Daily routine blueprint quick-strip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.DailyRoutineGeneratorSheet) }
                            .testTag("wellness_open_routine_strip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "✨ View today's flexible routine timeline",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Open timeline",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // 3. TODAY'S WELLNESS GOALS PILLARS (5 GENTLE PILLAR CARDS)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "CORE WELLNESS PILLARS",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pillar 1: Hydration Goal Card
                    WellnessPillarCard(
                        title = "Hydration",
                        metric = "${completedHydrationCount * 250} / 2000 mL",
                        subtitle = "Regular sips keep energy steady",
                        icon = Icons.Filled.LocalDrink,
                        accentColor = SoftBlueHydrate,
                        bgColor = SoftBlueLight,
                        onClick = { onOpenSubScreen(ActiveSubScreen.HydrationSheet) },
                        modifier = Modifier.weight(1f)
                    )

                    // Pillar 2: Sleep & Wind-Down Card
                    WellnessPillarCard(
                        title = "Sleep & Rest",
                        metric = "${preferences.windDownTime} → ${preferences.targetBedtime}",
                        subtitle = "Calm wind-down prepares tomorrow",
                        icon = Icons.Filled.DarkMode,
                        accentColor = SoftLavender,
                        bgColor = SoftLavenderLight,
                        onClick = { onOpenSubScreen(ActiveSubScreen.SleepSheet) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pillar 3: Movement & Mobility Card
                    WellnessPillarCard(
                        title = "Movement",
                        metric = "${recommendedFitness.durationMinutes} min ${recommendedFitness.difficulty}",
                        subtitle = recommendedFitness.title.take(28),
                        icon = Icons.Filled.FitnessCenter,
                        accentColor = SoftSageGreen,
                        bgColor = SoftSageLight,
                        onClick = { onStartMovementRoutine(recommendedFitness) },
                        modifier = Modifier.weight(1f)
                    )

                    // Pillar 4: Mind & Poise Card
                    WellnessPillarCard(
                        title = "Calm Pause",
                        metric = "Emotional Poise",
                        subtitle = "De-escalate stress with quiet dignity",
                        icon = Icons.Filled.SelfImprovement,
                        accentColor = WarmPeachHighlight,
                        bgColor = WarmPeachLight,
                        onClick = { onOpenSubScreen(ActiveSubScreen.AngerOverwhelmPauseSheet) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Pillar 5: Self-Care & Skincare Card (Full Width)
                SkeuoCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = 2.5.dp,
                    onClick = { onSelectSubSection?.invoke("SELF-CARE / GLOW") },
                    testTag = "wellness_pillar_skincare"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WarmAmberLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Spa,
                                contentDescription = "Skincare & Glow",
                                tint = WarmAmberAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Self-Care & Daily Skincare",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$completedSkincareCount of ${skincareSteps.size} morning/evening steps completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Open Skincare",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 4. DAILY HABIT CARDS SECTION HEADER & FILTER PILLS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TODAY'S HABIT CARDS",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "One-tap toggle • Gently honors your mental & physical energy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { showResetConfirmDialog = true },
                            modifier = Modifier.size(36.dp).testTag("reset_habits_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Reset today's habits calmly",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        FilledTonalButton(
                            onClick = { showAddHabitDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("add_custom_habit_button")
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Habit", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                // Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val filterOptions = listOf(
                        "ALL" to "All (${habits.size})",
                        "PENDING" to "Pending (${habits.count { !it.completedToday }})",
                        "COMPLETED" to "Done (${habits.count { it.completedToday }})",
                        "HYDRATE" to "💧 Hydrate",
                        "MOVE" to "🍃 Move",
                        "GLOW & SLEEP" to "✨ Glow & Sleep",
                        "MIND" to "🧘 Mind"
                    )
                    filterOptions.forEach { (key, label) ->
                        val isSelected = selectedFilter == key
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedFilter = key }
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // 5. HABIT CARDS LIST
        if (filteredHabits.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🌿", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (selectedFilter == "PENDING") "All habits are completed for today!" else "No habits match this filter.",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Feel free to rest, or add a custom habit that fits your rhythm.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredHabits, key = { it.id }) { habit ->
                GentleHabitCard(
                    habit = habit,
                    onToggleComplete = { onToggleHabit(habit) },
                    onCardClick = {
                        when (habit.category) {
                            "HYDRATE" -> onOpenSubScreen(ActiveSubScreen.HydrationSheet)
                            "MOVE" -> onSelectSubSection?.invoke("FITNESS & MOBILITY") ?: onStartMovementRoutine(recommendedFitness)
                            "GLOW" -> onSelectSubSection?.invoke("SELF-CARE / GLOW")
                            "SLEEP" -> onOpenSubScreen(ActiveSubScreen.SleepSheet)
                            "CONFIDENCE" -> onOpenSubScreen(ActiveSubScreen.ConfidenceCheckInSheet)
                            "POSTURE" -> onOpenSubScreen(ActiveSubScreen.StudyResetSheet)
                            else -> onToggleHabit(habit)
                        }
                    }
                )
            }
        }

        // 6. QUICK WELLNESS ACTIONS & RECHARGE ROW
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wellness_hobbies_quick_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🎨 Hobbies, Joy & Family Time",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Drawing, 1 episode of anime, evening walk or family dinners are not guilty pleasures—they are essential recharge.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    FilledTonalButton(
                        onClick = { onOpenSubScreen(ActiveSubScreen.HobbiesAndJoySheet) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Open Joy")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ADD CUSTOM HABIT DIALOG
    if (showAddHabitDialog) {
        var habitTitle by rememberSaveable { mutableStateOf("") }
        var habitSubtitle by rememberSaveable { mutableStateOf("") }
        var habitCategory by rememberSaveable { mutableStateOf("CUSTOM") }
        var habitEmoji by rememberSaveable { mutableStateOf("✨") }

        AlertDialog(
            onDismissRequest = { showAddHabitDialog = false },
            title = {
                Text(
                    text = "🌱 New Wellness Habit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Build a habit that supports your calm and focus without burden.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = habitTitle,
                        onValueChange = { habitTitle = it },
                        label = { Text("Habit Title (e.g. 10m Balcony Stroll)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_habit_title")
                    )
                    OutlinedTextField(
                        value = habitSubtitle,
                        onValueChange = { habitSubtitle = it },
                        label = { Text("Gentle Note / Preferred Timing") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_habit_subtitle")
                    )

                    // Emoji choices
                    Text("Choose an Icon:", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("✨", "🌱", "💧", "🧘", "🍵", "🚶‍♀️", "📖", "🌙", "🌸", "🎨").forEach { em ->
                            Surface(
                                shape = CircleShape,
                                color = if (habitEmoji == em) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .clickable { habitEmoji = em }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = em, fontSize = 20.sp)
                                }
                            }
                        }
                    }

                    // Category choices
                    Text("Category:", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("MOVE", "HYDRATE", "GLOW", "SLEEP", "CONFIDENCE", "CUSTOM").forEach { cat ->
                            val isSel = habitCategory == cat
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { habitCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (habitTitle.isNotBlank()) {
                            onAddCustomHabit(
                                habitTitle.trim(),
                                habitSubtitle.ifBlank { "Daily gentle consistency" }.trim(),
                                habitEmoji
                            )
                            showAddHabitDialog = false
                        }
                    },
                    modifier = Modifier.testTag("confirm_add_habit_button")
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

    // CONFIRM RESET HABITS DIALOG
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("🌿 Refresh Habits for Today?") },
            text = {
                Text("This gently unchecks today's habits so you can start with a fresh slate. No guilt or streak penalty is applied.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetTodaysHabits?.invoke()
                        showResetConfirmDialog = false
                    }
                ) {
                    Text("Yes, Refresh Calmly")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Keep As Is")
                }
            }
        )
    }
}

/**
 * Reusable Wellness Pillar Card with clean, gentle aesthetic
 */
@Composable
fun WellnessPillarCard(
    title: String,
    metric: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SkeuoCard(
        modifier = modifier,
        elevation = 2.5.dp,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = metric,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Enhanced Gentle Habit Card with clean aesthetic, category pills, smooth animations,
 * and 48dp interactive checkmark target.
 */
@Composable
fun GentleHabitCard(
    habit: DailyHabitEntity,
    onToggleComplete: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = habit.completedToday

    // Soft scale-up animation for habit cards when marked complete to give a sense of accomplishment
    val cardScale = remember { Animatable(1f) }
    var previousCompleted by rememberSaveable { mutableStateOf(isCompleted) }

    LaunchedEffect(isCompleted) {
        if (isCompleted && !previousCompleted) {
            // Accomplishment feedback: soft celebratory pulse scale-up, settling gently into place
            cardScale.animateTo(
                targetValue = 1.045f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )
            cardScale.animateTo(
                targetValue = 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        } else if (!isCompleted && previousCompleted) {
            cardScale.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(durationMillis = 150)
            )
        }
        previousCompleted = isCompleted
    }

    val cardBgColor by animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "habit_card_bg"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (isCompleted) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "check_scale"
    )

    val (badgeText, badgeBg, badgeColor) = when (habit.category) {
        "HYDRATE" -> Triple("💧 Hydration", SoftBlueLight, SoftBlueHydrate)
        "MOVE" -> Triple("🍃 Movement", SoftSageLight, SoftSageGreen)
        "GLOW" -> Triple("🌸 Self-Care", WarmPeachLight, WarmPeachHighlight)
        "SLEEP" -> Triple("🌙 Sleep", SoftLavenderLight, SoftLavender)
        "CONFIDENCE" -> Triple("✨ Confidence", WarmAmberLight, WarmAmberAccent)
        "POSTURE" -> Triple("🧘 Posture", SoftSageLight, SoftSageGreen)
        else -> Triple("🌱 Daily", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = BorderStroke(
            1.dp,
            if (isCompleted) GentleSuccessGreen.copy(alpha = 0.35f)
            else SkeuoBorderLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 1.dp else 2.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale.value
                scaleY = cardScale.value
            }
            .clip(RoundedCornerShape(20.dp))
            .clickable { onCardClick() }
            .testTag("gentle_habit_card_${habit.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isCompleted) GentleSuccessLight
                        else badgeBg
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = habit.emoji,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Habit Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = habit.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Category pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 48dp Interactive Checkbox Touch Target with smooth scale & color
            IconButton(
                onClick = onToggleComplete,
                modifier = Modifier
                    .size(48.dp)
                    .scale(checkScale)
                    .minimumInteractiveComponentSize()
                    .testTag("toggle_habit_${habit.id}")
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Completed ${habit.title}",
                        tint = GentleSuccessGreen,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Mark ${habit.title} complete",
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
