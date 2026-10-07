package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.DefaultWellnessData
import com.example.data.MovementRoutine
import com.example.ui.components.PrimaryButton
import com.example.ui.components.RoutineCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.GentleSuccessGreen
import kotlinx.coroutines.delay

@Composable
fun MoveScreen(
    movementCompletedToday: Boolean,
    onStartRoutine: (MovementRoutine) -> Unit,
    onLogRestDay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("ALL", "MORNING", "POSTURE", "STRENGTH", "RELAX")
    var selectedCategory by rememberSaveable { mutableStateOf("ALL") }

    val filteredRoutines = if (selectedCategory == "ALL") {
        DefaultWellnessData.movementRoutines
    } else {
        DefaultWellnessData.movementRoutines.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("move_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Subtitle
        item {
            Column {
                Text(
                    text = "Move your way",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Movement is about feeling capable — not changing your body.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Visual Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_move_banner_1791381874071),
                        contentDescription = "Sunlit movement mat illustration",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = if (movementCompletedToday) {
                                "✨ You moved with care today."
                            } else {
                                "🌱 Whenever you're ready, there's a short routine waiting."
                            },
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Rest Day Honor Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🌿 Taking a rest day?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "You're allowed to rest. Rest days build strength and help your body recover.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    FilledTonalButton(
                        onClick = onLogRestDay,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("log_rest_day_button")
                    ) {
                        Text("Honor Rest", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedCategory = category }
                            .minimumInteractiveComponentSize()
                            .testTag("move_category_${category.lowercase()}"),
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Grouped or Filtered Routines
        if (selectedCategory == "ALL") {
            listOf("MORNING", "POSTURE", "STRENGTH", "RELAX").forEach { cat ->
                val catRoutines = DefaultWellnessData.movementRoutines.filter { it.category == cat }
                item(key = "header_$cat") {
                    Spacer(modifier = Modifier.height(4.dp))
                    SectionHeader(
                        title = cat,
                        subtitle = when (cat) {
                            "MORNING" -> "Wake up your joints and energy gently"
                            "POSTURE" -> "Release desk, backpack, and screen tension"
                            "STRENGTH" -> "Feel steady, grounded, and capable in your body"
                            else -> "Slow down, stretch softly, and breathe"
                        }
                    )
                }
                items(catRoutines, key = { it.id }) { routine ->
                    RoutineCard(
                        routine = routine,
                        onStartClick = { onStartRoutine(routine) }
                    )
                }
            }
        } else {
            items(filteredRoutines, key = { it.id }) { routine ->
                RoutineCard(
                    routine = routine,
                    onStartClick = { onStartRoutine(routine) }
                )
            }
        }
    }
}

@Composable
fun TimerScreen(
    routine: MovementRoutine,
    onClose: () -> Unit,
    onFinishRoutine: (MovementRoutine) -> Unit
) {
    BackHandler { onClose() }

    val steps = routine.steps
    var currentStepIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentStep = steps.getOrElse(currentStepIndex) { steps.first() }

    var secondsRemaining by rememberSaveable(currentStepIndex) {
        mutableIntStateOf(currentStep.durationSeconds)
    }
    var isPaused by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentStepIndex, isPaused, secondsRemaining) {
        if (!isPaused && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        } else if (!isPaused && secondsRemaining == 0) {
            if (currentStepIndex < steps.lastIndex) {
                currentStepIndex += 1
            }
        }
    }

    val overallProgress = ((currentStepIndex + 1).toFloat() / steps.size.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val stepProgress = if (currentStep.durationSeconds > 0) {
        (secondsRemaining.toFloat() / currentStep.durationSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 18.dp)
            .testTag("workout_timer_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("close_timer_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Move"
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = routine.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Exercise ${currentStepIndex + 1} of ${steps.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Routine Progress Indicator
        LinearProgressIndicator(
            progress = { overallProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.weight(0.5f))

        // Circular Countdown Timer
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(210.dp)
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(210.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                strokeWidth = 12.dp
            )
            CircularProgressIndicator(
                progress = { stepProgress },
                modifier = Modifier.size(210.dp),
                color = if (isPaused) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                strokeWidth = 12.dp,
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val mins = secondsRemaining / 60
                val secs = secondsRemaining % 60
                Text(
                    text = "%d:%02d".format(mins, secs),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isPaused) "Paused — take your time" else "Breathe steadily",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Exercise Name & Instructions Card
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentStep.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = currentStep.instruction,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "🌬️ ${currentStep.breathCue}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pause / Skip / Finish Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { isPaused = !isPaused },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .minimumInteractiveComponentSize()
                    .testTag("timer_pause_button")
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isPaused) "Resume" else "Pause")
            }

            if (currentStepIndex < steps.lastIndex) {
                OutlinedButton(
                    onClick = { currentStepIndex += 1 },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("timer_skip_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Skip")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onFinishRoutine(routine) },
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GentleSuccessGreen,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .minimumInteractiveComponentSize()
                .testTag("timer_finish_button")
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Finish Session",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
