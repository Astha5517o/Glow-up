package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.DailyHabitEntity
import com.example.data.DefaultWellnessData
import com.example.data.MovementRoutine
import com.example.data.SkincareStepEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.CheckListItem
import com.example.ui.components.HabitCard
import com.example.ui.components.ProgressCard
import com.example.ui.components.RoutineCard
import com.example.ui.components.SectionHeader
import com.example.viewmodel.ActiveSubScreen

@Composable
fun WellnessScreen(
    preferences: UserPreferencesEntity,
    habits: List<DailyHabitEntity>,
    skincareSteps: List<SkincareStepEntity>,
    onToggleHabit: (DailyHabitEntity) -> Unit,
    onAddCustomHabit: (String, String, String) -> Unit,
    onStartMovementRoutine: (MovementRoutine) -> Unit,
    onToggleSkincareStep: (SkincareStepEntity) -> Unit,
    onAddSkincareStep: (String, String) -> Unit,
    onDeleteSkincareStep: (Int) -> Unit,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val subSections = listOf("OVERVIEW", "MINDSET & CONFIDENCE", "FITNESS & MOBILITY", "SELF-CARE / GLOW", "SLEEP & BRAIN DUMP")
    var selectedSubSection by rememberSaveable { mutableStateOf("OVERVIEW") }
    var addSkincarePeriod by rememberSaveable { mutableStateOf<String?>(null) }

    val completedHabits = habits.count { it.completedToday }
    val recommendedFitness = when (preferences.currentEnergyLevel) {
        "LOW" -> DefaultWellnessData.movementRoutines.last()
        "HIGH" -> DefaultWellnessData.movementRoutines[2]
        else -> DefaultWellnessData.movementRoutines[1]
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("wellness_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HEADER
        item {
            Column {
                Text(
                    text = "WELLNESS",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Feel energetic, healthy, strong, comfortable, confident & well-groomed.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // SUB-NAVIGATION CHIPS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subSections.forEach { sec ->
                    val isSel = selectedSubSection == sec
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSubSection = sec }
                    ) {
                        Text(
                            text = sec,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        when (selectedSubSection) {
            "MINDSET & CONFIDENCE" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.SetbackRecoverySheet) }
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🛡️", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Learn Without Losing Self-Respect",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "8-step gentle reframe for low test scores, mistakes, or competition feedback. Diagnostic, not a verdict on your future.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FilledTonalButton(
                                onClick = { onOpenSubScreen(ActiveSubScreen.SetbackRecoverySheet) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Open Reframe Journal")
                            }
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.ConfidenceLabSheet) }
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎤", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Confidence Lab & Speaking Practice",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Word-for-word scripts for doubts in class, 30s project pitches, saying NO politely, and handling tough feedback.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FilledTonalButton(
                                onClick = { onOpenSubScreen(ActiveSubScreen.ConfidenceLabSheet) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Open Situation Playbook")
                            }
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.FinancialLiteracySheet) }
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📈", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Financial Foundations & Student Value",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Value creation principles, high-leverage teen skills, 50/30/20 budgeting calculator, and small earning experiments.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FilledTonalButton(
                                onClick = { onOpenSubScreen(ActiveSubScreen.FinancialLiteracySheet) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Open Wealth & Value")
                            }
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.AngerOverwhelmPauseSheet) }
                            .testTag("wellness_anger_pause_card")
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "🕊️ Calm Pause Protocol (Anger & Overwhelm De-escalation)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "When triggered or defensive: 6-step grounding protocol to pause, separate objective facts from impulses, and respond with poise and dignity.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FilledTonalButton(
                                onClick = { onOpenSubScreen(ActiveSubScreen.AngerOverwhelmPauseSheet) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Open Pause Protocol")
                            }
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.ConfidenceCheckInSheet) }
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "🌸 Daily Confidence Check-In",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Record how you showed up today — posture, speech, decisions, and quiet self-belief.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            "FITNESS & MOBILITY" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_move_banner_1791381874071),
                                contentDescription = "Personalized fitness illustration",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Energy: ${preferences.currentEnergyLevel} • You have ${preferences.dailyTimeCommitmentMinutes} min. Try a ${recommendedFitness.category.lowercase()} session.",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    SectionHeader(
                        title = "PERSONALIZED FITNESS MODES",
                        subtitle = "QUICK RESET (5m) • NORMAL (12m) • FULL (20m) • LOW ENERGY (7m)"
                    )
                }

                items(DefaultWellnessData.movementRoutines, key = { it.id }) { routine ->
                    RoutineCard(
                        routine = routine,
                        onStartClick = { onStartMovementRoutine(routine) }
                    )
                }
            }

            "SELF-CARE / GLOW" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_glow_banner_1791381884382),
                            contentDescription = "Self-care shelf illustration",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                val morningSteps = skincareSteps.filter { it.period == "MORNING" }
                val eveningSteps = skincareSteps.filter { it.period == "EVENING" }

                item {
                    SectionHeader(
                        title = "MORNING SELF-CARE",
                        subtitle = "Skin routine, hygiene, grooming, water & posture",
                        actionText = "+ Step",
                        onActionClick = { addSkincarePeriod = "MORNING" }
                    )
                }

                items(morningSteps, key = { it.id }) { step ->
                    CheckListItem(
                        step = step,
                        onToggle = { onToggleSkincareStep(step) },
                        onDelete = if (morningSteps.size > 2) ({ onDeleteSkincareStep(step.id) }) else null
                    )
                }

                item {
                    SectionHeader(
                        title = "EVENING SELF-CARE",
                        subtitle = "Skincare, teeth, prepare tomorrow & wind-down",
                        actionText = "+ Step",
                        onActionClick = { addSkincarePeriod = "EVENING" }
                    )
                }

                items(eveningSteps, key = { it.id }) { step ->
                    CheckListItem(
                        step = step,
                        onToggle = { onToggleSkincareStep(step) },
                        onDelete = if (eveningSteps.size > 2) ({ onDeleteSkincareStep(step.id) }) else null
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSubScreen(ActiveSubScreen.ConfidenceCheckInSheet) }
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "🌸 Confidence Check: \"How did you show up today?\"",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Confidence comes from consistency, skills, health, grooming, posture, achievements, and self-respect.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            "SLEEP & BRAIN DUMP" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "CALM EVENING FLOW (${preferences.windDownTime} → ${preferences.targetBedtime})",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "LIGHT REVISION  →  PLAN TOMORROW  →  BRAIN DUMP  →  HYGIENE  →  WIND-DOWN  →  SLEEP",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onOpenSubScreen(ActiveSubScreen.BrainDumpSheet) },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("🧠 Empty Your Head")
                                }
                                OutlinedButton(
                                    onClick = { onOpenSubScreen(ActiveSubScreen.SleepSheet) },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("🌙 Sleep Schedule")
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                // OVERVIEW
                item {
                    ProgressCard(
                        completedCount = completedHabits,
                        totalCount = habits.size,
                        supportiveQuote = "You have ${preferences.dailyTimeCommitmentMinutes} minutes. Try a ${recommendedFitness.category.lowercase()} movement session.",
                        onViewDailyRoutineClick = { onOpenSubScreen(ActiveSubScreen.DailyRoutineGeneratorSheet) }
                    )
                }

                // Quick Wellness Modules Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { onOpenSubScreen(ActiveSubScreen.BrainDumpSheet) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🧠 Brain Dump")
                        }
                        FilledTonalButton(
                            onClick = { onOpenSubScreen(ActiveSubScreen.HydrationSheet) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("💧 Hydration")
                        }
                        FilledTonalButton(
                            onClick = { onOpenSubScreen(ActiveSubScreen.ConfidenceCheckInSheet) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🌸 Check-In")
                        }
                    }
                }

                // Recommended Fitness Card
                item {
                    SectionHeader(
                        title = "RECOMMENDED FOR YOUR ENERGY (${preferences.currentEnergyLevel})",
                        actionText = "All Modes",
                        onActionClick = { selectedSubSection = "FITNESS & MOBILITY" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RoutineCard(
                        routine = recommendedFitness,
                        onStartClick = { onStartMovementRoutine(recommendedFitness) }
                    )
                }

                // Daily Wellness Habits
                item {
                    SectionHeader(
                        title = "DAILY WELLNESS TRACKER",
                        subtitle = "Sleep • Hydration • Movement • Meals • Skincare • Posture • Mood"
                    )
                }

                items(habits, key = { it.id }) { habit ->
                    HabitCard(
                        habit = habit,
                        onCardClick = {
                            when (habit.category) {
                                "HYDRATE" -> onOpenSubScreen(ActiveSubScreen.HydrationSheet)
                                "MOVE" -> selectedSubSection = "FITNESS & MOBILITY"
                                "GLOW" -> selectedSubSection = "SELF-CARE / GLOW"
                                "SLEEP" -> onOpenSubScreen(ActiveSubScreen.SleepSheet)
                                "CONFIDENCE" -> onOpenSubScreen(ActiveSubScreen.ConfidenceCheckInSheet)
                                "POSTURE" -> onOpenSubScreen(ActiveSubScreen.StudyResetSheet)
                                else -> onToggleHabit(habit)
                            }
                        },
                        onToggleComplete = { onToggleHabit(habit) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (addSkincarePeriod != null) {
        var stepTitle by rememberSaveable { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { addSkincarePeriod = null },
            title = { Text("Add ${addSkincarePeriod?.lowercase()?.replaceFirstChar { it.uppercase() }} Step") },
            text = {
                OutlinedTextField(
                    value = stepTitle,
                    onValueChange = { stepTitle = it },
                    label = { Text("Step name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (stepTitle.isNotBlank()) {
                            onAddSkincareStep(addSkincarePeriod!!, stepTitle)
                            addSkincarePeriod = null
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { addSkincarePeriod = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
