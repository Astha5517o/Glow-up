package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.AiChatMessage
import com.example.data.DefaultWellnessData
import com.example.data.ErrorLogEntity
import com.example.data.OsTaskEntity
import com.example.data.PracticeQuestionItem
import com.example.data.StudyChapterEntity
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import kotlinx.coroutines.delay

@Composable
fun FocusModeSubScreen(
    taskTitle: String,
    purpose: String,
    durationMinutes: Int,
    currentStepText: String,
    totalSteps: Int,
    linkedTask: OsTaskEntity?,
    onOpenImStuck: () -> Unit,
    onClose: () -> Unit,
    onFinishFocus: (
        linkedTask: OsTaskEntity?,
        taskTitle: String,
        durationMinutes: Int,
        focusRating: String,
        distractionSource: String
    ) -> Unit
) {
    BackHandler { onClose() }

    var secondsRemaining by rememberSaveable { mutableIntStateOf(durationMinutes * 60) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var currentStep by rememberSaveable { mutableIntStateOf(1) }
    var showCompletionCheckIn by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isPaused, secondsRemaining) {
        if (!isPaused && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        }
    }

    var showDistractionResetModal by rememberSaveable { mutableStateOf(false) }
    var parkedThoughtInput by rememberSaveable { mutableStateOf("") }
    var parkedThoughtsList by rememberSaveable { mutableStateOf(listOf<String>()) }

    val totalSeconds = (durationMinutes * 60).coerceAtLeast(1)
    val timerProgress = (secondsRemaining.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("focus_mode_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Minimal Header (No distracting bottom nav)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit Focus Mode")
            }
            Text(
                text = "FOCUS MODE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = taskTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Current mission: $purpose",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(0.4f))

        // Circular Timer
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(220.dp)
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(220.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                strokeWidth = 10.dp
            )
            CircularProgressIndicator(
                progress = { timerProgress },
                modifier = Modifier.size(220.dp),
                color = if (isPaused) WarmPeachHighlight else MaterialTheme.colorScheme.primary,
                strokeWidth = 10.dp,
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val mins = secondsRemaining / 60
                val secs = secondsRemaining % 60
                Text(
                    text = "%02d:%02d".format(mins, secs),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isPaused) "PAUSED" else "IN FOCUS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Current Step & Progress Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress: $currentStep / $totalSteps",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { if (currentStep < totalSteps) currentStep += 1 }
                    ) {
                        Text("+ Step Done")
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps.coerceAtLeast(1).toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GentleSuccessGreen,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = currentStepText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.6f))

        // Action Controls: [PAUSE] [DISTRACTED] [STUCK]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { isPaused = !isPaused },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("focus_pause_button")
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isPaused) "RESUME" else "PAUSE", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(
                onClick = {
                    isPaused = true
                    showDistractionResetModal = true
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1.2f)
                    .height(52.dp)
                    .testTag("focus_distracted_button")
            ) {
                Text("💭 Distracted", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }

            FilledTonalButton(
                onClick = onOpenImStuck,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(0.9f)
                    .height(52.dp)
                    .testTag("focus_stuck_button")
            ) {
                Text("🧩 STUCK", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showCompletionCheckIn = true },
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GentleSuccessGreen,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("focus_finish_button")
        ) {
            Icon(Icons.Filled.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("FINISH", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }

    // Gentle In-Focus Distraction Recovery & Thought Parking Dialog
    if (showDistractionResetModal) {
        AlertDialog(
            onDismissRequest = {
                showDistractionResetModal = false
                isPaused = false
            },
            title = {
                Text("🌿 Gentle Focus Reset", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Wandering focus is natural human biology, not a failure. No shame, and no streak lost.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Park Thought for Later (Clear Your Head):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = parkedThoughtInput,
                                    onValueChange = { parkedThoughtInput = it },
                                    placeholder = { Text("Jot stray thought...", style = MaterialTheme.typography.bodySmall) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilledTonalButton(
                                    onClick = {
                                        if (parkedThoughtInput.isNotBlank()) {
                                            parkedThoughtsList = parkedThoughtsList + parkedThoughtInput.trim()
                                            parkedThoughtInput = ""
                                        }
                                    }
                                ) {
                                    Text("Park")
                                }
                            }
                            if (parkedThoughtsList.isNotEmpty()) {
                                Text(
                                    text = "Parked: ${parkedThoughtsList.joinToString(", ")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GentleSuccessGreen
                                )
                            }
                        }
                    }

                    Text("Choose how to reset:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)

                    OutlinedButton(
                        onClick = {
                            showDistractionResetModal = false
                            isPaused = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("1. Take 3 deep breaths & resume timer")
                    }

                    OutlinedButton(
                        onClick = {
                            secondsRemaining = (15 * 60).coerceAtMost(secondsRemaining)
                            showDistractionResetModal = false
                            isPaused = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("2. Shorten this session to 15m")
                    }

                    OutlinedButton(
                        onClick = {
                            showDistractionResetModal = false
                            showCompletionCheckIn = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("3. Finish calmly and take a break")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDistractionResetModal = false
                        isPaused = false
                    }
                ) {
                    Text("Resume")
                }
            }
        )
    }

    // Post-Focus Reflection ("How focused were you?" + private distraction pattern tracking)
    if (showCompletionCheckIn) {
        var focusRating by rememberSaveable { mutableStateOf("Fully focused") }
        var distractionSource by rememberSaveable { mutableStateOf("None") }

        val distractionCategories = listOf(
            "YouTube",
            "Anime",
            "Social media",
            "Random browsing",
            "Unclear task",
            "Tiredness",
            "Phone",
            "Other"
        )

        AlertDialog(
            onDismissRequest = { showCompletionCheckIn = false },
            title = { Text("How focused were you?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("Fully focused", "Mostly focused", "Distracted").forEach { rating ->
                        val isSel = focusRating == rating
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    focusRating = rating
                                    if (rating == "Fully focused") distractionSource = "None"
                                    else if (distractionSource == "None") distractionSource = "Unclear task"
                                }
                        ) {
                            Text(
                                text = rating,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }

                    if (focusRating == "Distracted" || focusRating == "Mostly focused") {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "What pulled you away? (Tracked privately for pattern analysis — no guilt)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            distractionCategories.forEach { src ->
                                val sel = distractionSource == src
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (sel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        1.dp,
                                        if (sel) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { distractionSource = src }
                                ) {
                                    Text(
                                        text = src,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCompletionCheckIn = false
                        onFinishFocus(
                            linkedTask,
                            taskTitle,
                            durationMinutes,
                            focusRating,
                            distractionSource
                        )
                    },
                    modifier = Modifier.testTag("confirm_finish_focus_button")
                ) {
                    Text("Complete & Update Plan")
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImStuckSubScreen(
    questionContext: PracticeQuestionItem? = null,
    onClose: () -> Unit,
    onAskAsthaAiHint: (String) -> Unit
) {
    BackHandler { onClose() }

    val stuckLocations = listOf(
        "Concept",
        "Formula selection",
        "How to start",
        "Setup",
        "Calculation",
        "Interpretation",
        "Not sure"
    )
    var selectedWhere by rememberSaveable { mutableStateOf("How to start") }
    var currentHintLevel by rememberSaveable { mutableIntStateOf(1) }
    var givenInput by rememberSaveable { mutableStateOf("") }
    var askedInput by rememberSaveable { mutableStateOf("") }
    var conceptInput by rememberSaveable { mutableStateOf("") }
    var tryThreeMinCountdown by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(tryThreeMinCountdown) {
        if (tryThreeMinCountdown > 0) {
            delay(1000L)
            tryThreeMinCountdown -= 1
        }
    }

    val sampleQ = questionContext ?: DefaultWellnessData.practiceQuestionsBank.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("im_stuck_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "🧩 I'M STUCK SYSTEM",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        // Core Philosophy Banner
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "\"Your job isn't to solve it instantly.\nYour job is to find the next step.\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Where are you stuck?
        item {
            SectionHeader(
                title = "Where are you stuck?",
                subtitle = "Pinpointing the exact friction point is half the solution"
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stuckLocations.forEach { loc ->
                    val isSel = selectedWhere == loc
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedWhere = loc }
                            .testTag("stuck_where_${loc.lowercase().replace(" ", "_")}")
                    ) {
                        Text(
                            text = loc,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // 5-LEVEL PROGRESSIVE LADDER
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "PROGRESSIVE HINT LADDER (LEVEL $currentHintLevel OF 5)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    // LEVEL 1 — THINK (2 minutes)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "LEVEL 1 — THINK (2 minutes)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = givenInput,
                                onValueChange = { givenInput = it },
                                label = { Text("1. What is given?") },
                                placeholder = { Text(sampleQ.givenPrompt) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = askedInput,
                                onValueChange = { askedInput = it },
                                label = { Text("2. What is asked?") },
                                placeholder = { Text(sampleQ.askedPrompt) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = conceptInput,
                                onValueChange = { conceptInput = it },
                                label = { Text("3. What concept might apply?") },
                                placeholder = { Text(sampleQ.conceptPrompt) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (currentHintLevel >= 2) {
                        HintStepBox(
                            levelTitle = "LEVEL 2 — SMALL CONCEPTUAL HINT",
                            content = sampleQ.hint1Concept
                        )
                    }
                    if (currentHintLevel >= 3) {
                        HintStepBox(
                            levelTitle = "LEVEL 3 — STRATEGY & RELEVANT FORMULA",
                            content = "${sampleQ.hint2Formula}\nStrategy: ${sampleQ.hint3Strategy}"
                        )
                    }
                    if (currentHintLevel >= 4) {
                        HintStepBox(
                            levelTitle = "LEVEL 4 — FIRST STEP ONLY",
                            content = sampleQ.step4FirstStep
                        )
                    }
                    if (currentHintLevel >= 5) {
                        HintStepBox(
                            levelTitle = "LEVEL 5 — COMPLETE SOLUTION EXPLANATION",
                            content = sampleQ.step5FullSolution
                        )
                    }
                }
            }
        }

        // Bottom Controls: [TRY 3 MORE MIN] [GET A HINT] [SKIP & RETURN]
        item {
            if (tryThreeMinCountdown > 0) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = GentleSuccessLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⏱️ Trying on your own: ${tryThreeMinCountdown / 60}:${"%02d".format(tryThreeMinCountdown % 60)} remaining. You've got this!",
                        style = MaterialTheme.typography.titleSmall,
                        color = GentleSuccessGreen,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { tryThreeMinCountdown = 180 },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("stuck_try_3_min_button")
                ) {
                    Text("TRY 3 MORE MIN", style = MaterialTheme.typography.labelMedium)
                }

                Button(
                    onClick = {
                        if (currentHintLevel < 5) currentHintLevel += 1
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("stuck_get_hint_button")
                ) {
                    Text(
                        text = if (currentHintLevel < 5) "GET A HINT (L${currentHintLevel + 1})" else "ALL HINTS SHOWN",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = "SKIP & RETURN",
                    onClick = onClose,
                    modifier = Modifier.weight(1f),
                    testTag = "stuck_skip_return_button"
                )
                FilledTonalButton(
                    onClick = {
                        onAskAsthaAiHint("I am stuck on $selectedWhere: Given='$givenInput', Asked='$askedInput'. Give me Hint 1 without spoiling the full solution.")
                    },
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("Ask ASTHA AI")
                }
            }
        }
    }
}

@Composable
private fun HintStepBox(levelTitle: String, content: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = levelTitle,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OneShotLearningSubScreen(
    chapter: StudyChapterEntity,
    onClose: () -> Unit,
    onCompleteOneShot: (
        chapterId: Int,
        videoResource: String,
        purpose: String,
        capturedConcept: String,
        capturedFormula: String,
        capturedDoubt: String,
        understandingStatus: String
    ) -> Unit
) {
    BackHandler { onClose() }

    var videoResource by rememberSaveable { mutableStateOf(chapter.videoResource) }
    var selectedPurpose by rememberSaveable { mutableStateOf(chapter.purpose) }
    var capturedConcept by rememberSaveable { mutableStateOf(chapter.capturedConcept) }
    var capturedFormula by rememberSaveable { mutableStateOf(chapter.capturedFormula) }
    var capturedDoubt by rememberSaveable { mutableStateOf(chapter.capturedDoubt) }
    var videoFinishedStep by rememberSaveable { mutableStateOf(chapter.oneShotCompleted) }
    var recallAnswer1 by rememberSaveable { mutableStateOf("") }
    var recallAnswer2 by rememberSaveable { mutableStateOf("") }
    var understandingColor by rememberSaveable { mutableStateOf(chapter.understandingStatus) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("one_shot_learning_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text("🎯 ONE-SHOT LEARNING MODE", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "${chapter.subject} • ${chapter.chapterName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // STEP 1 — WATCH & PURPOSE
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "STEP 1 — BEFORE WATCHING",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "\"What exactly are you trying to understand?\"",
                        style = MaterialTheme.typography.titleMedium
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Concept", "Full chapter", "Revision", "Weak topic").forEach { p ->
                            val isSel = selectedPurpose == p
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedPurpose = p }
                            ) {
                                Text(
                                    text = p,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = videoResource,
                        onValueChange = { videoResource = it },
                        label = { Text("YouTube / Video / Resource Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "During the video, ONLY capture these 3 things (no endless note-taking):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    OutlinedTextField(
                        value = capturedConcept,
                        onValueChange = { capturedConcept = it },
                        label = { Text("1. One important concept") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = capturedFormula,
                        onValueChange = { capturedFormula = it },
                        label = { Text("2. One key formula / rule") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = capturedDoubt,
                        onValueChange = { capturedDoubt = it },
                        label = { Text("3. One thing I still don't understand") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!videoFinishedStep) {
                        PrimaryButton(
                            text = "VIDEO COMPLETE — Close Video & Recall",
                            onClick = { videoFinishedStep = true },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "one_shot_video_complete_button"
                        )
                    }
                }
            }
        }

        // STEP 2 — ACTIVE RECALL & UNDERSTANDING CHECK (GREEN / YELLOW / RED)
        if (videoFinishedStep) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "STEP 2 — CLOSE THE VIDEO & ACTIVE RECALL",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = recallAnswer1,
                            onValueChange = { recallAnswer1 = it },
                            label = { Text("Explain the main idea & when to use the formula without looking:") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = recallAnswer2,
                            onValueChange = { recallAnswer2 = it },
                            label = { Text("What is one common mistake in this topic?") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "UNDERSTANDING CHECK",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        val checks = listOf(
                            Triple("GREEN", "🟢 GREEN: I can explain it.", "Ready for medium & challenge practice questions."),
                            Triple("YELLOW", "🟡 YELLOW: I understand it but can't solve questions yet.", "Start with 2 worked examples + 3 easy questions."),
                            Triple("RED", "🔴 RED: I still don't understand it.", "Targeted clarification on your specific doubt — do NOT re-watch another full one-shot.")
                        )

                        checks.forEach { (code, title, desc) ->
                            val isSel = understandingColor == code
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(
                                    if (isSel) 2.dp else 1.dp,
                                    if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { understandingColor = code }
                                    .testTag("understanding_check_${code.lowercase()}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text(desc, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        PrimaryButton(
                            text = "Save One-Shot & Continue Workflow",
                            onClick = {
                                onCompleteOneShot(
                                    chapter.id,
                                    videoResource,
                                    selectedPurpose,
                                    capturedConcept,
                                    capturedFormula,
                                    capturedDoubt,
                                    understandingColor
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "save_one_shot_button"
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PracticeQuestionsSubScreen(
    initialSubject: String,
    skipOrEscapeCount: Int,
    onClose: () -> Unit,
    onRecordOutcome: (
        question: PracticeQuestionItem,
        outcome: String,
        logToErrorBook: Boolean,
        whyMistake: String
    ) -> Unit,
    onOpenImStuckForQuestion: (PracticeQuestionItem) -> Unit
) {
    BackHandler { onClose() }

    var selectedSubject by rememberSaveable { mutableStateOf(initialSubject) }
    var selectedDifficulty by rememberSaveable { mutableStateOf("Mixed") }
    var selectedSource by rememberSaveable { mutableStateOf("All") }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var hintStageShown by rememberSaveable(currentIndex) { mutableIntStateOf(0) }
    var showAntiEscapeModal by rememberSaveable { mutableStateOf(false) }

    val filteredQuestions = DefaultWellnessData.practiceQuestionsBank.filter { q ->
        (selectedSubject == "All" || q.subject == selectedSubject) &&
            (selectedDifficulty == "Mixed" || q.difficulty == selectedDifficulty) &&
            (selectedSource == "All" || q.source == selectedSource)
    }.ifEmpty { DefaultWellnessData.practiceQuestionsBank }

    val currentQ = filteredQuestions[currentIndex % filteredQuestions.size]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("practice_questions_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("PRACTICE QUESTIONS", style = MaterialTheme.typography.headlineSmall)
            }
        }

        // Filters: Subject, Difficulty, Source
        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Physics", "Chemistry Physical", "Mathematics", "Computer Science / AI").forEach { s ->
                    val sel = selectedSubject == s
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                selectedSubject = s
                                currentIndex = 0
                            }
                    ) {
                        Text(
                            text = s,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Mixed", "Easy", "Medium", "Hard").forEach { diff ->
                    val sel = selectedDifficulty == diff
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (sel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedDifficulty = diff }
                    ) {
                        Text(
                            text = diff,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
                listOf("All", "Mission Jeet", "School", "Study Panda", "Self-Study").forEach { src ->
                    val sel = selectedSource == src
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedSource = src }
                    ) {
                        Text(
                            text = "Source: $src",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Question Card with Stepwise Hints (Never reveal full answer immediately)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusBadgeChip(label = "${currentQ.subject} • ${currentQ.difficulty}")
                        StatusBadgeChip(
                            label = "${currentQ.source} • ${currentQ.focusSkill}",
                            containerColor = WarmPeachLight,
                            contentColor = WarmPeachHighlight
                        )
                    }

                    Text(
                        text = currentQ.chapter,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = currentQ.questionText,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Progressive Hint Ladder
                    if (hintStageShown >= 1) {
                        HintStepBox("HINT 1 — CONCEPTUAL", currentQ.hint1Concept)
                    }
                    if (hintStageShown >= 2) {
                        HintStepBox("HINT 2 — PRINCIPLE / FORMULA", currentQ.hint2Formula)
                    }
                    if (hintStageShown >= 3) {
                        HintStepBox("HINT 3 — FIRST STEP", currentQ.step4FirstStep)
                    }
                    if (hintStageShown >= 4) {
                        HintStepBox("FULL SOLUTION (After Attempt)", currentQ.step5FullSolution)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { if (hintStageShown < 4) hintStageShown += 1 },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("practice_next_hint_button")
                        ) {
                            Text(
                                when (hintStageShown) {
                                    0 -> "Get Hint 1"
                                    1 -> "Get Hint 2 (Formula)"
                                    2 -> "Show First Step"
                                    3 -> "Show Full Solution"
                                    else -> "All Hints Shown"
                                }
                            )
                        }
                        OutlinedButton(
                            onClick = { onOpenImStuckForQuestion(currentQ) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🧩 I'm Stuck")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Record Outcome (Adapts difficulty & updates Error Log):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onRecordOutcome(currentQ, "SOLVED", false, "")
                                currentIndex += 1
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GentleSuccessGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("outcome_solved_button")
                        ) {
                            Text("✓ Solved")
                        }
                        OutlinedButton(
                            onClick = {
                                onRecordOutcome(currentQ, "PARTIAL", true, "Needed hint on ${currentQ.focusSkill}")
                                currentIndex += 1
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Partial")
                        }
                        OutlinedButton(
                            onClick = {
                                onRecordOutcome(currentQ, "WRONG", true, "Mistake in ${currentQ.focusSkill}")
                                currentIndex += 1
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("outcome_wrong_button")
                        ) {
                            Text("Wrong (+Error Log)")
                        }
                        TextButton(
                            onClick = {
                                onRecordOutcome(currentQ, "SKIPPED", false, "")
                                if (skipOrEscapeCount >= 1) {
                                    showAntiEscapeModal = true
                                } else {
                                    currentIndex += 1
                                }
                            },
                            modifier = Modifier.testTag("outcome_skip_button")
                        ) {
                            Text("Skip")
                        }
                    }
                }
            }
        }
    }

    // ANTI-ESCAPE STUDY SYSTEM GENTLE INTERVENTION
    if (showAntiEscapeModal) {
        AlertDialog(
            onDismissRequest = { showAntiEscapeModal = false },
            title = { Text("🌱 This looks like a difficult-question moment.") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "You don't need the whole answer.\nJust find:\n" +
                            "1. What is given? (${currentQ.givenPrompt})\n" +
                            "2. What is asked? (${currentQ.askedPrompt})\n" +
                            "3. What concept might apply?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAntiEscapeModal = false
                        hintStageShown = maxOf(1, hintStageShown)
                    }
                ) {
                    Text("TRY AGAIN WITH HINT")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAntiEscapeModal = false
                        currentIndex += 1
                    }
                ) {
                    Text("COME BACK LATER")
                }
            }
        )
    }
}

@Composable
fun ErrorLogSubScreen(
    errorLogs: List<ErrorLogEntity>,
    onClose: () -> Unit,
    onReviewError: (ErrorLogEntity) -> Unit,
    onAddError: (
        subject: String,
        chapter: String,
        questionSummary: String,
        mistakeType: String,
        whyIMadeIt: String,
        correctIdea: String,
        source: String
    ) -> Unit
) {
    BackHandler { onClose() }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    val mistakeTypes = listOf(
        "Concept",
        "Formula",
        "Approach",
        "Calculation",
        "Careless",
        "Interpretation",
        "Memory",
        "Time management"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("error_log_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text("📓 ERROR LOG", style = MaterialTheme.typography.headlineSmall)
                }
                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("log_mistake_button")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Log Mistake")
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Spaced Review Schedule:  24 hours  →  3 days  →  7 days  →  Mastered",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        if (errorLogs.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No errors logged.", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Great. Keep practicing — mistakes you choose to review become useful data.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(errorLogs, key = { it.id }) { err ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatusBadgeChip(label = "${err.subject} • ${err.mistakeType}")
                            StatusBadgeChip(
                                label = err.nextReviewLabel,
                                containerColor = if (err.isResolved) GentleSuccessLight else WarmPeachLight,
                                contentColor = if (err.isResolved) GentleSuccessGreen else WarmPeachHighlight
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${err.chapter} (${err.source} • ${err.dateString})",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Question: ${err.questionSummary}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• Why I made it: ${err.whyIMadeIt}\n• Correct idea: ${err.correctIdea}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (!err.isResolved) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FilledTonalButton(
                                onClick = { onReviewError(err) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Mark Reviewed (Advance 24h → 3d → 7d)")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var subject by rememberSaveable { mutableStateOf("Physics") }
        var chapter by rememberSaveable { mutableStateOf("Rotational Motion") }
        var question by rememberSaveable { mutableStateOf("") }
        var mistakeType by rememberSaveable { mutableStateOf(mistakeTypes.first()) }
        var whyText by rememberSaveable { mutableStateOf("") }
        var correctText by rememberSaveable { mutableStateOf("") }
        var source by rememberSaveable { mutableStateOf("Mission Jeet") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Log Mistake to Error Log") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = chapter,
                        onValueChange = { chapter = it },
                        label = { Text("Chapter") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Question / Problem Summary") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        mistakeTypes.forEach { t ->
                            OutlinedButton(
                                onClick = { mistakeType = t },
                                border = BorderStroke(
                                    if (mistakeType == t) 2.dp else 1.dp,
                                    if (mistakeType == t) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text(t, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = whyText,
                        onValueChange = { whyText = it },
                        label = { Text("Why I made it") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = correctText,
                        onValueChange = { correctText = it },
                        label = { Text("Correct idea / rule to remember") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (question.isNotBlank()) {
                            onAddError(subject, chapter, question, mistakeType, whyText, correctText, source)
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save Mistake")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AsthaAiSubScreen(
    messages: List<AiChatMessage>,
    isLoading: Boolean,
    onClose: () -> Unit,
    onSendPrompt: (String) -> Unit
) {
    BackHandler { onClose() }
    var promptInput by rememberSaveable { mutableStateOf("") }

    val quickPrompts = listOf(
        "What should I do now?",
        "What should I study?",
        "What do I do after this one-shot?",
        "Give me questions.",
        "I am stuck.",
        "I have 20 minutes.",
        "I missed today's plan.",
        "What should I work on for my scholarship profile?",
        "What is my next project action?",
        "How should I prepare for NCSC?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("astha_ai_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("ASTHA AI", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Study coach & personal operating system intelligence",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Prompt Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickPrompts.forEach { qp ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSendPrompt(qp) }
                ) {
                    Text(
                        text = qp,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Conversation List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (msg.isUser) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = msg.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (msg.isUser) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ASTHA AI is thinking...", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text("Ask ASTHA AI (study coach, schedule, NCSC, Krow)...") },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("astha_ai_input")
            )
            IconButton(
                onClick = {
                    if (promptInput.isNotBlank()) {
                        onSendPrompt(promptInput)
                        promptInput = ""
                    }
                },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .testTag("astha_ai_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send to ASTHA AI",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
