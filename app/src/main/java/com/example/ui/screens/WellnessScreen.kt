package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DailyHabitEntity
import com.example.data.DefaultWellnessData
import com.example.data.HydrationReminderEntity
import com.example.data.MovementRoutine
import com.example.data.SkincareStepEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayIconBadge
import com.example.ui.components.ClayPill
import com.example.ui.components.ClayPillButton
import com.example.ui.theme.ClayCoralAction
import com.example.ui.theme.ClayDeepPurple
import com.example.ui.theme.ClayLavenderCard
import com.example.ui.theme.ClayMintCard
import com.example.ui.theme.ClayPaleLavenderSurface
import com.example.ui.theme.ClayPeach
import com.example.ui.theme.ClayPrimaryLavender
import com.example.ui.theme.ClayPrimaryText
import com.example.ui.theme.ClaySecondaryText
import com.example.ui.theme.ClaySkyBlue
import com.example.ui.theme.ClaySoftMint
import com.example.ui.theme.ClayWarmIvory
import com.example.ui.theme.GentleSuccessGreen
import com.example.viewmodel.ActiveSubScreen
import kotlinx.coroutines.delay

@Composable
fun WellnessScreen(
    preferences: UserPreferencesEntity,
    habits: List<DailyHabitEntity>,
    skincareSteps: List<SkincareStepEntity>,
    hydrationReminders: List<HydrationReminderEntity> = emptyList(),
    onToggleHabit: (DailyHabitEntity) -> Unit,
    onAddCustomHabit: (String, String, String) -> Unit,
    onStartMovementRoutine: (MovementRoutine) -> Unit,
    onToggleSkincareStep: (SkincareStepEntity) -> Unit,
    onAddSkincareStep: (String, String) -> Unit,
    onDeleteSkincareStep: (Int) -> Unit,
    onResetTodaysHabits: (() -> Unit)? = null,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMeditationModal by rememberSaveable { mutableStateOf(false) }
    var showMovementDetailModal by rememberSaveable { mutableStateOf<MovementRoutine?>(null) }
    var selectedRoutineIndex by rememberSaveable { mutableIntStateOf(0) }

    val danceRoutine = MovementRoutine(
        id = "dance_joy_1",
        title = "Expressive Dance Break",
        category = "QUICK RESET",
        durationMinutes = 15,
        difficulty = "Beginner",
        focusTags = "Full Body & Joy",
        description = "Put on your favorite track, feel the rhythm, and shake off exam tension. No choreography rules, just pure joyful expression.",
        steps = listOf(
            com.example.data.ExerciseStep("Music choice", "Put on your favorite upbeat song.", 60, "Listen with delight"),
            com.example.data.ExerciseStep("Warm-up", "Start with gentle shoulder rolls and wrist bounces.", 120, "Inhale calm"),
            com.example.data.ExerciseStep("Freestyle groove", "Dance across the room, letting all physical tension go.", 600, "Breathe freely"),
            com.example.data.ExerciseStep("Cool down", "Deep breaths with arms reaching upward, slow exhale.", 120, "Long slow exhale")
        )
    )

    val currentRoutines = listOf(
        danceRoutine,
        DefaultWellnessData.movementRoutines[0],
        DefaultWellnessData.movementRoutines[1]
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ClayWarmIvory)
            .testTag("wellness_screen_list"),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // -------------------------------------------------------------
        // 1. WELCOMING HEADER
        // -------------------------------------------------------------
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Wellness & Joy",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = ClayPrimaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Rest, move, and recharge without guilt or pressure.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ClaySecondaryText,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // -------------------------------------------------------------
        // 2. DANCE (PROMINENT VISUAL CARD)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wellness_dance_card"),
                shape = RoundedCornerShape(28.dp),
                containerColor = Color.White,
                elevation = 5.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Actual dance visual banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_move_banner_1791381874071),
                            contentDescription = "Joyful Dance",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Top gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Black.copy(alpha = 0.2f),
                                            Color.Black.copy(alpha = 0.55f)
                                        )
                                    )
                                )
                        )
                        // Badge overlay
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ClayPeach,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "BEGINNER FRIENDLY • PURE JOY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B3A14),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Joyful Dance Break",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ClayPaleLavenderSurface
                            ) {
                                Text(
                                    text = "15 min",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayDeepPurple,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Release mental fatigue and refresh your focus through carefree movement.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ClaySecondaryText
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ClayPillButton(
                            text = "Start dancing",
                            icon = Icons.Default.MusicNote,
                            onClick = {
                                onStartMovementRoutine(danceRoutine)
                            },
                            containerColor = ClayCoralAction,
                            height = 46.dp,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "start_dance_button"
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 3. MOVEMENT & MOBILITY (Visual preview & step-by-step)
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
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gentle Movement",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClayPrimaryText
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ClaySoftMint
                        ) {
                            Text(
                                text = "Restorative",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF285E3D),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Physical resets designed to release neck, spine, and eye strain after study.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DefaultWellnessData.movementRoutines.take(2).forEach { routine ->
                        Surface(
                            onClick = { showMovementDetailModal = routine },
                            shape = RoundedCornerShape(18.dp),
                            color = ClayPaleLavenderSurface.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
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
                                    ClayIconBadge(
                                        icon = Icons.Default.SelfImprovement,
                                        containerColor = Color.White,
                                        iconColor = ClayDeepPurple,
                                        size = 38.dp,
                                        iconSize = 20.dp
                                    )
                                    Column {
                                        Text(
                                            text = routine.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ClayPrimaryText
                                        )
                                        Text(
                                            text = "${routine.durationMinutes} min • ${routine.focusTags}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ClaySecondaryText
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = ClaySecondaryText
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 4. MEDITATION & BREATH (Calming card with gentle visual focus)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wellness_meditation_card"),
                shape = RoundedCornerShape(26.dp),
                containerColor = Color.White,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ClayIconBadge(
                                icon = Icons.Default.Spa,
                                containerColor = ClaySoftMint,
                                iconColor = Color(0xFF285E3D),
                                size = 40.dp,
                                iconSize = 22.dp
                            )
                            Column {
                                Text(
                                    text = "Quiet Space Meditation",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayPrimaryText
                                )
                                Text(
                                    text = "Gentle breathing & presence",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ClaySecondaryText
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ClaySkyBlue
                        ) {
                            Text(
                                text = "5 min",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38658D),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Sit comfortably, let your shoulders drop, and follow the soothing rhythm.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ClayButton(
                        text = "Open breathing space",
                        icon = Icons.Default.SelfImprovement,
                        onClick = { showMeditationModal = true },
                        containerColor = Color(0xFF7FA892),
                        contentColor = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "open_meditation_button"
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 5. CREATIVE TIME & RELAXING HOBBIES
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
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ClayIconBadge(
                                icon = Icons.Default.Brush,
                                containerColor = ClayPeach,
                                iconColor = Color(0xFF9A4518),
                                size = 40.dp,
                                iconSize = 22.dp
                            )
                            Column {
                                Text(
                                    text = "Creative Time & Joy",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayPrimaryText
                                )
                                Text(
                                    text = "Drawing, music & offline joy",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ClaySecondaryText
                                )
                            }
                        }

                        TextButton(onClick = { onOpenSubScreen(ActiveSubScreen.HobbiesAndJoySheet) }) {
                            Text("Explore", color = ClayDeepPurple, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Not everything in life has to be about productivity. Carve out guilt-free time for your sketchbook, reading, or family talks.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Drawing", "Music", "Journaling", "Stargazing").forEach { hobbyTag ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ClayPaleLavenderSurface.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hobbyTag,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClayPrimaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 6. GENTLE SELF-CARE (NO APPEARANCE / WEIGHT / METRIC SCORING)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                containerColor = Color.White,
                elevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Evening Rest & Gentle Reset",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Wind down comfortably so your mind rests peacefully tonight.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    skincareSteps.take(3).forEach { step ->
                        Surface(
                            onClick = { onToggleSkincareStep(step) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (step.completedToday) ClaySoftMint.copy(alpha = 0.4f) else ClayPaleLavenderSurface.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = if (step.completedToday) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (step.completedToday) GentleSuccessGreen else ClaySecondaryText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = step.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClayPrimaryText,
                                    textDecoration = if (step.completedToday) TextDecoration.LineThrough else TextDecoration.None
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------
    // DIALOG: MEDITATION & BREATHING SPACE
    // -----------------------------------------------------------------
    if (showMeditationModal) {
        val infiniteTransition = rememberInfiniteTransition(label = "breathe")
        val breathScale by infiniteTransition.animateFloat(
            initialValue = 0.82f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "breath_scale"
        )

        var meditationSecondsLeft by rememberSaveable { mutableIntStateOf(180) }

        LaunchedEffect(meditationSecondsLeft) {
            if (meditationSecondsLeft > 0) {
                delay(1000L)
                meditationSecondsLeft -= 1
            }
        }

        AlertDialog(
            onDismissRequest = { showMeditationModal = false },
            title = {
                Text(
                    text = "Breathing Sanctuary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ClayPrimaryText
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Inhale calmly... exhale gently.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    // Animated Breathing Visual Circle
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ClaySoftMint.copy(alpha = 0.5f),
                            border = BorderStroke(2.dp, ClaySoftMint),
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(breathScale)
                        ) {}

                        Surface(
                            shape = CircleShape,
                            color = ClayPaleLavenderSurface,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${meditationSecondsLeft / 60}:${"%02d".format(meditationSecondsLeft % 60)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayDeepPurple
                                )
                            }
                        }
                    }

                    Text(
                        text = "There is nothing you need to accomplish in this moment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ClaySecondaryText
                    )
                }
            },
            confirmButton = {
                ClayButton(
                    text = "Done & Refreshed",
                    onClick = { showMeditationModal = false },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showMeditationModal = false }) {
                    Text("Skip session", color = ClaySecondaryText)
                }
            },
            shape = RoundedCornerShape(26.dp),
            containerColor = Color.White
        )
    }

    // -----------------------------------------------------------------
    // DIALOG: MOVEMENT STEP-BY-STEP DETAIL
    // -----------------------------------------------------------------
    showMovementDetailModal?.let { routine ->
        AlertDialog(
            onDismissRequest = { showMovementDetailModal = null },
            title = {
                Text(
                    text = routine.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = routine.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Step-by-step guidance:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    routine.steps.forEachIndexed { idx, step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayDeepPurple
                            )
                            Text(
                                text = "${step.name}: ${step.instruction}",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClayPrimaryText
                            )
                        }
                    }
                }
            },
            confirmButton = {
                ClayButton(
                    text = "Start Routine (${routine.durationMinutes}m)",
                    onClick = {
                        val toStart = routine
                        showMovementDetailModal = null
                        onStartMovementRoutine(toStart)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showMovementDetailModal = null }) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(26.dp),
            containerColor = Color.White
        )
    }
}
