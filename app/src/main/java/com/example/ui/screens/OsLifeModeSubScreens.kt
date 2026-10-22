package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
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
import com.example.data.BrainDumpEntity
import com.example.data.DistractionLogEntity
import com.example.data.ErrorLogEntity
import com.example.data.OpportunityEntity
import com.example.data.OsTaskEntity
import com.example.data.ProjectEntity
import com.example.data.StudyChapterEntity
import com.example.data.UserPreferencesEntity
import com.example.data.MorningDecisionPlan
import com.example.data.MorningStudyPriority
import com.example.notifications.GlowUpNotificationHelper
import com.example.ui.components.NotificationCard
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.SettingsRow
import com.example.ui.components.StatusBadgeChip
import com.example.ui.components.WeeklyProgressCard
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.WeeklyStatsUi
import kotlinx.coroutines.delay

@Composable
fun AfterSchoolResetSubScreen(
    nextFocusTitle: String,
    onClose: () -> Unit,
    onStartNextFocusBlock: () -> Unit
) {
    BackHandler { onClose() }

    var step1EatHydrate by rememberSaveable { mutableStateOf(false) }
    var step2Decompress by rememberSaveable { mutableStateOf(false) }
    var chosenRestMinutes by rememberSaveable { mutableIntStateOf(20) }
    var restCountdownSeconds by rememberSaveable { mutableIntStateOf(0) }
    var restCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(restCountdownSeconds) {
        if (restCountdownSeconds > 0) {
            delay(1000L)
            restCountdownSeconds -= 1
            if (restCountdownSeconds == 0) {
                restCompleted = true
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("after_school_reset_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("🏠 WELCOME HOME — AFTER-SCHOOL RESET", style = MaterialTheme.typography.titleLarge)
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Transition gently from school to home. Controlled power resets prevent accidental 1–2 hour naps so your evening stays calm.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Step 1 & Step 2
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { step1EatHydrate = !step1EatHydrate }
                    ) {
                        Checkbox(checked = step1EatHydrate, onCheckedChange = { step1EatHydrate = it })
                        Column {
                            Text("Step 1: Eat / hydrate", style = MaterialTheme.typography.titleMedium)
                            Text("Have lunch and a glass of water first", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { step2Decompress = !step2Decompress }
                    ) {
                        Checkbox(checked = step2Decompress, onCheckedChange = { step2Decompress = it })
                        Column {
                            Text("Step 2: 10-minute decompression", style = MaterialTheme.typography.titleMedium)
                            Text("Change clothes, put your bag down, and breathe", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        // Step 3: Controlled Power Rest
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Step 3: Choose Controlled Rest",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tip: If you ever feel groggy after 30-min naps, choose 10 min reset or 20 min power rest.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            10 to "10 min reset",
                            20 to "20 min power rest",
                            30 to "30 min rest"
                        ).forEach { (mins, label) ->
                            val isSel = chosenRestMinutes == mins
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(
                                    if (isSel) 2.dp else 1.dp,
                                    if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { chosenRestMinutes = mins }
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                                )
                            }
                        }
                    }

                    if (restCountdownSeconds > 0) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GentleSuccessLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⏰ Power Rest Alarm Active: ${restCountdownSeconds / 60}:${"%02d".format(restCountdownSeconds % 60)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = GentleSuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Close your eyes. We'll transition to '$nextFocusTitle' when done.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GentleSuccessGreen
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { restCountdownSeconds = chosenRestMinutes * 60 },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_power_rest_button")
                        ) {
                            Icon(Icons.Filled.Alarm, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Set $chosenRestMinutes-Min Rest Timer & Alarm")
                        }
                    }
                }
            }
        }

        // Next Focus Block Recommendation After Rest
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "NEXT UP AFTER RESET",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = nextFocusTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = "Start Next Focus Block Now",
                        onClick = onStartNextFocusBlock,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun EntertainmentWalletSubScreen(
    preferences: UserPreferencesEntity,
    hasCompletedPriorityToday: Boolean,
    onClose: () -> Unit,
    onFocusFirst25Min: () -> Unit,
    onWatchAnyway: () -> Unit
) {
    BackHandler { onClose() }
    var enjoyingNow by rememberSaveable { mutableStateOf(false) }

    val isUnlocked = preferences.entertainmentUnlockedToday || hasCompletedPriorityToday

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("entertainment_wallet_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("🎬 ENTERTAINMENT WALLET", style = MaterialTheme.typography.headlineSmall)
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = preferences.entertainmentTitle,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        StatusBadgeChip(
                            label = if (isUnlocked || enjoyingNow) "Unlocked ✨" else "Available after priority block",
                            containerColor = if (isUnlocked || enjoyingNow) GentleSuccessLight else WarmPeachLight,
                            contentColor = if (isUnlocked || enjoyingNow) GentleSuccessGreen else WarmPeachHighlight
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isUnlocked || enjoyingNow) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = GentleSuccessLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "\"Your important tasks are done. Enjoy your time.\"\n\nEnjoy ${preferences.entertainmentEpisodesAllowed} episode of ${preferences.entertainmentTitle} completely guilt-free!",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = GentleSuccessGreen,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "\"Want to finish your next 25-minute focus block first?\"",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Entertainment is never banned or punished. Finishing 25 minutes first makes your ${preferences.entertainmentTitle} break feel even more relaxing.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onFocusFirst25Min,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("entertainment_focus_first_button")
                            ) {
                                Text("FOCUS FIRST")
                            }

                            OutlinedButton(
                                onClick = {
                                    enjoyingNow = true
                                    onWatchAnyway()
                                },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("entertainment_watch_anyway_button")
                            ) {
                                Text("WATCH ANYWAY")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MorningModeSubScreen(
    userName: String,
    tasks: List<OsTaskEntity>,
    plan: MorningDecisionPlan? = null,
    onPlanDayAgain: (minutesOverride: Int?, energyOverride: String?) -> Unit = { _, _ -> },
    onStartPriorityFocus: (MorningStudyPriority) -> Unit = {},
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    var selectedMinutes by rememberSaveable { mutableIntStateOf(plan?.availableMinutes ?: 90) }
    var selectedEnergy by rememberSaveable { mutableStateOf(plan?.energyLevel ?: "NORMAL") }
    var showRecalculateControls by rememberSaveable { mutableStateOf(false) }

    var checkWater by rememberSaveable { mutableStateOf(false) }
    var checkReady by rememberSaveable { mutableStateOf(false) }
    var checkMove by rememberSaveable { mutableStateOf(false) }
    var checkSchedule by rememberSaveable { mutableStateOf(false) }
    var checkLeave by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("morning_mode_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "☀️ MORNING DECISION ENGINE",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Clarity before action, ${userName.ifBlank { "Astha" }} — based on actual exams & topics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 1. THE FIVE CORE QUESTIONS (DIRECT ACTIONABLE ANSWERS)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎯 TODAY'S CORE DECISION",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${plan?.availableMinutes ?: selectedMinutes} MIN • $selectedEnergy ENERGY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Question 1: What is the most important thing today?
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "1. WHAT IS THE MOST IMPORTANT THING TODAY?",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = plan?.mostImportantThingToday
                                ?: "Physics 45-min: Rotational Motion (Torque & Angular Momentum) with zero doubts.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Question 2: Which subject and exact topic to start with?
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "2. EXACT TOPIC TO START WITH:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${plan?.startingSubject ?: "Physics"} → ${plan?.startingExactTopic ?: "Rotational Motion"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Question 3: What should I do after studying the topic?
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "3. AFTER STUDYING THE TOPIC:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = plan?.whatToDoAfterStudying
                                ?: "5-min posture & hydration reset, then review 2 previous mistakes from Error Log.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Question 4: Next priority after that?
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "4. NEXT PRIORITY AFTER THAT:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = plan?.nextPriorityAfterThat
                                ?: "Krow / Smart EcoDrain: Document 1 concrete milestone or test observation.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Question 5: What can wait until another day?
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "⏳ WHAT CAN WAIT UNTIL ANOTHER DAY:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = plan?.whatCanWaitUntilAnotherDay
                                    ?: "Passive YouTube marathons, secondary research rabbit-holes, and speculative deadlines can wait.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // 2. PLAN MY DAY AGAIN (RECALCULATE FOR LESS TIME / COMMITMENTS)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🔄 PLAN MY DAY AGAIN",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Recalculate priorities if time or energy changed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(
                            onClick = { showRecalculateControls = !showRecalculateControls },
                            modifier = Modifier.testTag("toggle_recalculate_button")
                        ) {
                            Text(if (showRecalculateControls) "Hide" else "Adjust")
                        }
                    }

                    if (showRecalculateControls) {
                        Text(
                            text = "Available time today:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(30, 45, 60, 90, 120).forEach { mins ->
                                val isSel = selectedMinutes == mins
                                FilterChip(
                                    selected = isSel,
                                    onClick = {
                                        selectedMinutes = mins
                                        onPlanDayAgain(mins, selectedEnergy)
                                    },
                                    label = { Text("${mins}m") },
                                    modifier = Modifier.testTag("minute_chip_$mins")
                                )
                            }
                        }

                        Text(
                            text = "Energy level:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("LOW", "NORMAL", "HIGH").forEach { lvl ->
                                val isSel = selectedEnergy == lvl
                                FilterChip(
                                    selected = isSel,
                                    onClick = {
                                        selectedEnergy = lvl
                                        onPlanDayAgain(selectedMinutes, lvl)
                                    },
                                    label = { Text(lvl) },
                                    modifier = Modifier.testTag("energy_chip_$lvl")
                                )
                            }
                        }

                        Button(
                            onClick = {
                                onPlanDayAgain(selectedMinutes, selectedEnergy)
                                showRecalculateControls = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize().testTag("recalculate_plan_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Recalculate Remaining Plan")
                        }
                    }
                }
            }
        }

        // 3. UP TO THREE STUDY & ACTION PRIORITIES (DETAILED CRITERIA)
        item {
            Text(
                text = "TODAY'S 3 ACTION PRIORITIES (MAX 3 AT ONCE)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        val priorities = plan?.priorities ?: listOf(
            MorningStudyPriority(
                priorityRank = 1,
                subject = "Physics",
                exactTopic = "Rotational Motion: Torque and Equilibrium",
                startingResourceOrMethod = "NCERT notes and core derivations (no video distractions)",
                activeRecallStep = "Write all torque equilibrium conditions (ΣFx=0, ΣFy=0, Στ=0) from memory on scratch paper.",
                questionsToAttempt = "Solve 3 standard NCERT in-text numerical problems.",
                completionCriterion = "All 3 problems solved independently with complete free-body diagrams and correct units.",
                nextStepIfStuck = "Level 1: Re-draw free-body diagram. Level 2: Check Error Log. Level 3: ASAA hint.",
                estimatedMinutes = 45
            ),
            MorningStudyPriority(
                priorityRank = 2,
                subject = "Mathematics",
                exactTopic = "Trigonometric Equations: General Solutions",
                startingResourceOrMethod = "Review 2 previous calculation mistakes in the Error Log.",
                activeRecallStep = "State general solution formula for sin θ = sin α and cos θ = cos α without looking.",
                questionsToAttempt = "Attempt 3 medium algebraic equations.",
                completionCriterion = "Zero quadrant or sign errors in the solutions.",
                nextStepIfStuck = "Write exact line of confusion for teacher query.",
                estimatedMinutes = 30
            ),
            MorningStudyPriority(
                priorityRank = 3,
                subject = "Innovation / Project",
                exactTopic = "Krow / Smart EcoDrain: Log 1 observation or user test note",
                startingResourceOrMethod = "Open Project Evidence Ledger.",
                activeRecallStep = "Summarize the problem statement in two simple sentences.",
                questionsToAttempt = "Test 1 prototype feature or answer 1 judge question.",
                completionCriterion = "1 entry saved with concrete observation.",
                nextStepIfStuck = "Write down 3 user assumptions to verify.",
                estimatedMinutes = 20
            )
        )

        items(priorities.take(3), key = { it.priorityRank }) { priority ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("morning_priority_${priority.priorityRank}")
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "PRIORITY #${priority.priorityRank} • ${priority.subject.uppercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "${priority.estimatedMinutes} MIN",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = priority.exactTopic,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Step 1: Starting Resource
                    Row(verticalAlignment = Alignment.Top) {
                        Text("📖 Starting Resource: ", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(priority.startingResourceOrMethod, style = MaterialTheme.typography.bodySmall)
                    }

                    // Step 2: Active Recall
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("🧠 Active-Recall Step:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                            Text(priority.activeRecallStep, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Step 3: Questions
                    Row(verticalAlignment = Alignment.Top) {
                        Text("✍️ Questions to attempt: ", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(priority.questionsToAttempt, style = MaterialTheme.typography.bodySmall)
                    }

                    // Step 4: Completion Criterion
                    Row(verticalAlignment = Alignment.Top) {
                        Text("✅ Completion Criterion: ", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = GentleSuccessGreen)
                        Text(priority.completionCriterion, style = MaterialTheme.typography.bodySmall)
                    }

                    // Step 5: Next Step If Stuck
                    Row(verticalAlignment = Alignment.Top) {
                        Text("🧩 If Stuck: ", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        Text(priority.nextStepIfStuck, style = MaterialTheme.typography.bodySmall)
                    }

                    Button(
                        onClick = { onStartPriorityFocus(priority) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize().testTag("start_priority_${priority.priorityRank}_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Priority ${priority.priorityRank} (${priority.estimatedMinutes}m)")
                    }
                }
            }
        }

        // 4. MORNING LAUNCH CHECKLIST
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "☀️ 5-STEP MORNING LAUNCH",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val items = listOf(
                        Triple("💧 Drink 1 full glass of water", checkWater) { v: Boolean -> checkWater = v },
                        Triple("✨ Wash face & morning skincare", checkReady) { v: Boolean -> checkReady = v },
                        Triple("🧘 3-minute movement or sun stretch", checkMove) { v: Boolean -> checkMove = v },
                        Triple("📅 Check today's timetable & exams", checkSchedule) { v: Boolean -> checkSchedule = v },
                        Triple("🎒 Pack bag & leave on time without rushing", checkLeave) { v: Boolean -> checkLeave = v }
                    )

                    items.forEach { (label, checked, setter) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { setter(!checked) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = checked, onCheckedChange = setter)
                            Text(label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SchoolModeSubScreen(
    onClose: () -> Unit,
    onStartQuickSchoolFocus: (title: String, mins: Int) -> Unit
) {
    BackHandler { onClose() }
    var selectedMinutes by rememberSaveable { mutableIntStateOf(10) }
    var enjoyingFriendTime by rememberSaveable { mutableStateOf(false) }

    val recommendation = when (selectedMinutes) {
        5 -> listOf("Recall key Physics/Chemistry formulas on paper", "Quick flashcards", "Review 1 mistake in Error Log")
        10 -> listOf("Solve 2–3 targeted questions", "Active recall of today's class topic")
        20 -> listOf("Targeted practice set (4–5 questions)", "Spaced Error Log review")
        else -> listOf("Deeper chapter practice", "Complete school assignment early", "Work on NCSC research notes")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("school_mode_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("🏫 SCHOOL MODE", style = MaterialTheme.typography.headlineSmall)
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("HOW MUCH FREE TIME DO YOU HAVE?", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 20, 30, 45).forEach { m ->
                            val sel = selectedMinutes == m && !enjoyingFriendTime
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        enjoyingFriendTime = false
                                        selectedMinutes = m
                                    }
                            ) {
                                Text(
                                    text = if (m == 45) "45+ MIN" else "$m MIN",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (enjoyingFriendTime) GentleSuccessGreen else MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { enjoyingFriendTime = true }
                                .testTag("school_friend_time_button")
                        ) {
                            Text(
                                text = "🤝 FRIEND TIME",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (enjoyingFriendTime) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }

                    if (enjoyingFriendTime) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GentleSuccessLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🤝 Enjoy time with your friends! School breaks are not supposed to become 100% study time. Laugh, talk, walk around, and recharge.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GentleSuccessGreen,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "Recommended for $selectedMinutes minutes (Never long videos during short periods):",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        recommendation.forEach { rec ->
                            Text("• $rec", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        PrimaryButton(
                            text = "Start $selectedMinutes-Min School Mission",
                            onClick = {
                                onStartQuickSchoolFocus(recommendation.first(), selectedMinutes)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YoutubeStudySubScreen(
    chapters: List<StudyChapterEntity>,
    onClose: () -> Unit,
    onStartPostYoutubePractice: (topic: String) -> Unit
) {
    BackHandler { onClose() }

    var topicInput by rememberSaveable { mutableStateOf(chapters.firstOrNull()?.currentTopic ?: "Rotational Dynamics") }
    var goalInput by rememberSaveable { mutableStateOf("Understand concept & 1 worked example") }
    var timeAvail by rememberSaveable { mutableIntStateOf(20) }
    var idea1 by rememberSaveable { mutableStateOf("") }
    var idea2 by rememberSaveable { mutableStateOf("") }
    var idea3 by rememberSaveable { mutableStateOf("") }

    val alreadyOneShotDone = chapters.firstOrNull()?.oneShotCompleted == true

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("youtube_study_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("▶️ SMART YOUTUBE STUDY MODE", style = MaterialTheme.typography.headlineSmall)
            }
        }

        if (alreadyOneShotDone) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = WarmPeachLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💡 Note: You already completed the one-shot for your current chapter. Keep this watch clip to 20 minutes max for targeted clarification rather than another full lecture.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmPeachHighlight,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("YOUTUBE MISSION SETUP", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text("TOPIC") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = goalInput,
                        onValueChange = { goalInput = it },
                        label = { Text("GOAL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Watch: $timeAvail minutes maximum → Write 3 key ideas → Close YouTube → Active recall → 5 questions → Mistake check", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)

                    OutlinedTextField(
                        value = idea1,
                        onValueChange = { idea1 = it },
                        label = { Text("Key Idea 1") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = idea2,
                        onValueChange = { idea2 = it },
                        label = { Text("Key Idea 2") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = idea3,
                        onValueChange = { idea3 = it },
                        label = { Text("Key Idea 3") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PrimaryButton(
                        text = "Close YouTube & Solve 5 Questions",
                        onClick = { onStartPostYoutubePractice(topicInput) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrainDumpSubScreen(
    brainDumps: List<BrainDumpEntity>,
    onClose: () -> Unit,
    onSaveBrainDump: (content: String, category: String) -> Unit
) {
    BackHandler { onClose() }

    var thoughtText by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Tomorrow") }
    var windDownCountdown by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(windDownCountdown) {
        if (windDownCountdown > 0) {
            delay(1000L)
            windDownCountdown -= 1
        }
    }

    val categories = listOf("Study", "Project", "Scholarship", "Personal", "Tomorrow", "Random")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("brain_dump_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("🧠 EMPTY YOUR HEAD", style = MaterialTheme.typography.headlineSmall)
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "What's on your mind?",
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = thoughtText,
                        onValueChange = { thoughtText = it },
                        placeholder = { Text("Put every unfinished worry, tomorrow task, project idea, or random thought here...") },
                        minLines = 4,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brain_dump_input")
                    )

                    Text("Categorize (Auto-organizes into Tasks, Priority, Backlog, or Notes):", style = MaterialTheme.typography.labelMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSel = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedCategory = cat }
                                    .testTag("brain_dump_cat_${cat.lowercase()}")
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    PrimaryButton(
                        text = "Empty Head & Organize",
                        onClick = {
                            if (thoughtText.isNotBlank()) {
                                onSaveBrainDump(thoughtText, selectedCategory)
                                thoughtText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "save_brain_dump_button"
                    )
                }
            }
        }

        // Reassurance + 2-Minute Wind-Down
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "\"Nothing else needs to be solved tonight.\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (windDownCountdown > 0) {
                        Text(
                            text = "🌙 2-Minute Wind-Down Breath: ${windDownCountdown / 60}:${"%02d".format(windDownCountdown % 60)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = GentleSuccessGreen
                        )
                    } else {
                        OutlinedButton(
                            onClick = { windDownCountdown = 120 },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Start 2-Minute Wind-Down")
                        }
                    }
                }
            }
        }

        if (brainDumps.isNotEmpty()) {
            item {
                SectionHeader(title = "ORGANIZED THOUGHTS")
            }
            items(brainDumps, key = { it.id }) { item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatusBadgeChip(label = "${item.category}  •  ${item.organizedBucket}")
                            Text(item.dateString, style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun RecoveryResetSubScreen(
    tasks: List<OsTaskEntity>,
    onClose: () -> Unit,
    onUpdateTaskBucket: (OsTaskEntity, String) -> Unit,
    onRegeneratePlan: () -> Unit
) {
    BackHandler { onClose() }
    val unfinished = tasks.filter { !it.isCompleted }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("recovery_reset_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("🔄 RESET YOUR PLAN", style = MaterialTheme.typography.headlineSmall)
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "\"Let's reset from here.\"",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "A missed task does not mean the day is ruined. Categorize remaining tasks below and ASTHA OS will recalculate a realistic rest of your day without piling everything up.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        items(unfinished, key = { it.id }) { task ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(task.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("MUST DO", "SHOULD DO", "CAN POSTPONE", "CAN DELETE").forEach { bucket ->
                            val sel = task.priorityBucket == bucket
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onUpdateTaskBucket(task, bucket) }
                            ) {
                                Text(
                                    text = bucket,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            PrimaryButton(
                text = "Regenerate Remaining Schedule",
                onClick = onRegeneratePlan,
                modifier = Modifier.fillMaxWidth(),
                testTag = "regenerate_plan_button"
            )
        }
    }
}

@Composable
fun WeeklyReviewSubScreen(
    preferences: UserPreferencesEntity,
    chapters: List<StudyChapterEntity>,
    errorLogs: List<ErrorLogEntity>,
    projects: List<ProjectEntity>,
    opportunities: List<OpportunityEntity>,
    distractionLogs: List<DistractionLogEntity>,
    weeklyStats: WeeklyStatsUi,
    onClose: () -> Unit,
    onSaveWeeklyReflections: (worked: String, didNotWork: String, shouldChange: String, nextFocus: String) -> Unit
) {
    BackHandler { onClose() }

    var workedInput by rememberSaveable { mutableStateOf(preferences.weeklyWorkedWell) }
    var didNotWorkInput by rememberSaveable { mutableStateOf(preferences.weeklyDidNotWork) }
    var shouldChangeInput by rememberSaveable { mutableStateOf(preferences.weeklyShouldChange) }
    var nextWeekFocusInput by rememberSaveable { mutableStateOf(preferences.nextWeekFocus) }

    val topDistraction = distractionLogs
        .filter { it.distractionSource != "None" }
        .groupingBy { it.distractionSource }
        .eachCount()
        .maxByOrNull { it.value }?.key ?: "Unclear task"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("weekly_review_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("📊 WEEK IN REVIEW", style = MaterialTheme.typography.headlineSmall)
            }
        }

        // Comprehensive 10-Point Overview Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("THIS WEEK'S SNAPSHOT", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("• Academic progress: ${chapters.size} active chapters (${chapters.count { it.understandingStatus == "GREEN" }} Green)")
                    Text("• Weak topics tracked: ${chapters.firstOrNull()?.weakAreas ?: "Angular momentum"}")
                    Text("• Questions solved: ${chapters.sumOf { it.questionsSolved }} / ${chapters.sumOf { it.questionsAttempted }}")
                    Text("• Mistakes in Error Log: ${errorLogs.count { it.isResolved }} fixed, ${errorLogs.count { !it.isResolved }} in spaced review")
                    Text("• Projects progressed: ${projects.joinToString(", ") { "${it.name} (${it.pipelineStage})" }}")
                    Text("• Evidence added: ${projects.sumOf { it.evidenceCheckedCount }} verified items across projects")
                    Text("• Opportunities tracked: ${opportunities.size} (${opportunities.count { it.status == "Preparing" }} preparing)")
                    Text("• Wellness & Sleep: ${weeklyStats.movementCompletedDays}/5 movement, ${weeklyStats.sleepCompletedDays}/7 wind-downs")
                }
            }
        }

        // Private Distraction Engine Pattern Insight
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🔍 DISTRACTION PATTERN INSIGHT (NO SHAME)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Main friction point noticed: $topDistraction.\n" +
                            "\"You tend to lose focus when the next task isn't clearly defined. Let's define your next 20-minute mission before starting.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Actionable Weekly Reflection Inputs
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = workedInput,
                        onValueChange = { workedInput = it },
                        label = { Text("WHAT WORKED?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = didNotWorkInput,
                        onValueChange = { didNotWorkInput = it },
                        label = { Text("WHAT DIDN'T?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = shouldChangeInput,
                        onValueChange = { shouldChangeInput = it },
                        label = { Text("WHAT SHOULD CHANGE?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nextWeekFocusInput,
                        onValueChange = { nextWeekFocusInput = it },
                        label = { Text("NEXT WEEK'S TOP FOCUS") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrimaryButton(
                        text = "Save Weekly Review",
                        onClick = {
                            onSaveWeeklyReflections(
                                workedInput,
                                didNotWorkInput,
                                shouldChangeInput,
                                nextWeekFocusInput
                            )
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSubScreen(
    preferences: UserPreferencesEntity,
    onClose: () -> Unit,
    onUpdatePreferences: ((UserPreferencesEntity) -> UserPreferencesEntity) -> Unit,
    onTriggerSampleNotification: (GlowUpNotificationHelper.GentleNotificationSample) -> Unit,
    onResetTodaysHabits: () -> Unit,
    onExportData: () -> Unit,
    onDeleteAllData: () -> Unit
) {
    BackHandler { onClose() }

    var morningStart by rememberSaveable { mutableStateOf(preferences.morningStudyStartTime) }
    var morningEnd by rememberSaveable { mutableStateOf(preferences.morningStudyEndTime) }
    var schoolStart by rememberSaveable { mutableStateOf(preferences.schoolStartTime) }
    var schoolEnd by rememberSaveable { mutableStateOf(preferences.schoolEndTime) }
    var resetStart by rememberSaveable { mutableStateOf(preferences.afterSchoolResetStartTime) }
    var resetEnd by rememberSaveable { mutableStateOf(preferences.afterSchoolResetEndTime) }
    var eveningStart by rememberSaveable { mutableStateOf(preferences.eveningStudyStartTime) }
    var eveningEnd by rememberSaveable { mutableStateOf(preferences.eveningStudyEndTime) }
    var eveningPlanMode by rememberSaveable { mutableStateOf(preferences.eveningPlanMode) }
    var wakeTime by rememberSaveable { mutableStateOf(preferences.wakeTime) }
    var windDown by rememberSaveable { mutableStateOf(preferences.windDownTime) }
    var bedtime by rememberSaveable { mutableStateOf(preferences.targetBedtime) }
    var entTitle by rememberSaveable { mutableStateOf(preferences.entertainmentTitle) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_sub_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("⚙️ ASTHA OS SETTINGS", style = MaterialTheme.typography.headlineSmall)
            }
        }

        // Theme Mode: Light / Dark / System
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("THEME", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("LIGHT", "DARK", "SYSTEM").forEach { mode ->
                            val isSel = preferences.themeMode == mode
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        onUpdatePreferences {
                                            it.copy(
                                                themeMode = mode,
                                                darkModeEnabled = mode == "DARK"
                                            )
                                        }
                                    }
                            ) {
                                Text(
                                    text = mode,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Real Daily Routine & Sleep Schedule Settings
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("REAL DAILY ROUTINE & SCHEDULE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(
                        "Configure your real study & rest windows. The engine adapts flexibly without rigid or coaching timetable constraints.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Morning study window
                    Text("🌅 Morning Study Window", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = morningStart,
                            onValueChange = { morningStart = it },
                            label = { Text("Start") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = morningEnd,
                            onValueChange = { morningEnd = it },
                            label = { Text("End") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // School hours
                    Text("🏫 School Hours", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = schoolStart,
                            onValueChange = { schoolStart = it },
                            label = { Text("Start") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = schoolEnd,
                            onValueChange = { schoolEnd = it },
                            label = { Text("End") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // After-school reset
                    Text("🍃 After-School Reset Window", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = resetStart,
                            onValueChange = { resetStart = it },
                            label = { Text("Start") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = resetEnd,
                            onValueChange = { resetEnd = it },
                            label = { Text("End") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Evening study window
                    Text("📚 Evening Study Window", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = eveningStart,
                            onValueChange = { eveningStart = it },
                            label = { Text("Start") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = eveningEnd,
                            onValueChange = { eveningEnd = it },
                            label = { Text("End") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Evening Plan Mode selector
                    Text("Evening Plan Preset", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("STANDARD" to "Standard", "SHORT" to "Short", "LOW_ENERGY" to "Low Energy").forEach { (modeVal, label) ->
                            val isSel = eveningPlanMode == modeVal
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { eveningPlanMode = modeVal }
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    // Sleep & Wind-Down
                    Text("🌙 Sleep & Recovery Routine", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = wakeTime,
                            onValueChange = { wakeTime = it },
                            label = { Text("Wake") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = windDown,
                            onValueChange = { windDown = it },
                            label = { Text("Wind-Down") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = bedtime,
                            onValueChange = { bedtime = it },
                            label = { Text("Bedtime") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = entTitle,
                        onValueChange = { entTitle = it },
                        label = { Text("Entertainment / Hobby Preference (e.g. Anime, Drawing)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PrimaryButton(
                        text = "Save Schedule & Recalculate Today",
                        onClick = {
                            onUpdatePreferences {
                                it.copy(
                                    morningStudyStartTime = morningStart,
                                    morningStudyEndTime = morningEnd,
                                    schoolStartTime = schoolStart,
                                    schoolEndTime = schoolEnd,
                                    afterSchoolResetStartTime = resetStart,
                                    afterSchoolResetEndTime = resetEnd,
                                    eveningStudyStartTime = eveningStart,
                                    eveningStudyEndTime = eveningEnd,
                                    eveningPlanMode = eveningPlanMode,
                                    dronaSchedule = "",
                                    wakeTime = wakeTime,
                                    windDownTime = windDown,
                                    targetBedtime = bedtime,
                                    entertainmentTitle = entTitle.ifBlank { "Anime" }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "save_schedule_settings_button"
                    )
                }
            }
        }

        // Notifications
        item {
            SettingsRow(
                title = "Sparse Smart Notifications",
                subtitle = "Supportive reminders for morning study, after-school reset & wind-down",
                checked = preferences.notificationsEnabled,
                onCheckedChange = { enabled ->
                    onUpdatePreferences { it.copy(notificationsEnabled = enabled) }
                }
            )
        }

        items(GlowUpNotificationHelper.notificationSamples, key = { it.id }) { sample ->
            NotificationCard(
                sample = sample,
                onPreviewClick = { onTriggerSampleNotification(sample) }
            )
        }

        // Privacy & Data Management
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("PRIVACY & DATA MANAGEMENT", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(
                        "All tasks, study sessions, error logs, projects, opportunities, and wellness notes are stored privately on your device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SecondaryButton(
                        text = "Reset Today's Habits",
                        onClick = onResetTodaysHabits,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SecondaryButton(
                        text = "Export My Data",
                        onClick = onExportData,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete All Data")
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Reset All ASTHA OS Data?") },
            text = { Text("This will clear your custom entries and restore default ASTHA OS starter data.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteAllData()
                    }
                ) {
                    Text("Reset Everything", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
