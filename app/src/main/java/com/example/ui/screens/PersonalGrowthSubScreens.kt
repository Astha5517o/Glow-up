package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.ConfidenceLabScenario
import com.example.data.DefaultWellnessData
import com.example.data.FinancialExperimentEntity
import com.example.data.FinancialLesson
import com.example.data.SetbackRecoveryEntity
import kotlinx.coroutines.delay

/**
 * 1. SETBACK RECOVERY: "Learn Without Losing Self-Respect"
 * An 8-prompt guided reflective framework to process mistakes, low marks, or competition rejection
 * without collapsing into shame or self-loathing.
 */
@Composable
fun SetbackRecoverySubScreen(
    setbacks: List<SetbackRecoveryEntity>,
    onClose: () -> Unit,
    onRecordSetback: (
        eventTitle: String,
        category: String,
        whatActuallyHappened: String,
        howAmIFeeling: String,
        whatIsStillTrueAboutMe: String,
        whatWasOutOfMyControl: String,
        whatWasInMyControl: String,
        actionableLesson: String,
        gentleNextStep: String
    ) -> Unit,
    onToggleResolved: (SetbackRecoveryEntity) -> Unit,
    onDeleteSetback: (Int) -> Unit
) {
    BackHandler { onClose() }

    var showNewSetbackDialog by rememberSaveable { mutableStateOf(false) }
    var filterResolvedOnly by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val filteredSetbacks = setbacks.filter {
        when (filterResolvedOnly) {
            true -> it.isResolved
            false -> !it.isResolved
            null -> true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("setback_recovery_screen"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP APP BAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("close_setback_screen_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to previous screen"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "LEARN WITH SELF-RESPECT",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Separate what happened from who you are.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // HERO PHILOSOPHY CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Diagnostic, Not a Verdict",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "When an exam goes poorly or a competition rejects your pitch, shame tries to convince you: 'I am not smart enough.' Self-respect says: 'This is diagnostic feedback on my preparation system. I will extract the lesson and protect my dignity.'",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showNewSetbackDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("open_reframe_flow_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reframe a Recent Setback (8 Steps)")
                    }
                }
            }
        }

        // FILTER CHIPS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val chips = listOf(
                    "ALL (${setbacks.size})" to null,
                    "NEEDS GENTLE ACTION (${setbacks.count { !it.isResolved }})" to false,
                    "INTEGRATED & RESOLVED (${setbacks.count { it.isResolved }})" to true
                )
                chips.forEach { (label, filterVal) ->
                    val isSel = filterResolvedOnly == filterVal
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { filterResolvedOnly = filterVal }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        if (filteredSetbacks.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No unresolved setbacks here.",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "You are carrying yourself with poise and quiet resilience.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredSetbacks, key = { it.id }) { item ->
                SetbackCard(
                    setback = item,
                    onToggleResolved = { onToggleResolved(item) },
                    onDelete = { onDeleteSetback(item.id) }
                )
            }
        }
    }

    if (showNewSetbackDialog) {
        EightStepSetbackDialog(
            onDismiss = { showNewSetbackDialog = false },
            onSave = { title, cat, happened, feeling, stillTrue, outControl, inControl, lesson, nextStep ->
                onRecordSetback(title, cat, happened, feeling, stillTrue, outControl, inControl, lesson, nextStep)
                showNewSetbackDialog = false
            }
        )
    }
}

@Composable
fun SetbackCard(
    setback: SetbackRecoveryEntity,
    onToggleResolved: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (setback.isResolved)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (setback.isResolved)
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("setback_card_${setback.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = setback.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = setback.dateString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onToggleResolved,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = if (setback.isResolved) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = "Toggle resolved",
                        tint = if (setback.isResolved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete setback",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = setback.eventTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "CORE TRUTH STILL INTACT:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = setback.whatIsStillTrueAboutMe,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gentle next step: ${setback.gentleNextStep}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = if (expanded) 6 else 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(if (expanded) "Show Less" else "Full Reframe")
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ReframeDetailItem("What Actually Happened", setback.whatActuallyHappened)
                    ReframeDetailItem("Emotional Stance", setback.howAmIFeeling)
                    ReframeDetailItem("Out of My Control", setback.whatWasOutOfMyControl)
                    ReframeDetailItem("In My Control", setback.whatWasInMyControl)
                    ReframeDetailItem("Actionable Lesson", setback.actionableLesson)
                }
            }
        }
    }
}

@Composable
fun ReframeDetailItem(title: String, body: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 8-step gentle reframe dialog
 */
@Composable
fun EightStepSetbackDialog(
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        category: String,
        whatHappened: String,
        feeling: String,
        stillTrue: String,
        outControl: String,
        inControl: String,
        lesson: String,
        nextStep: String
    ) -> Unit
) {
    var step by rememberSaveable { mutableIntStateOf(1) }

    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("ACADEMIC") }
    var whatHappened by rememberSaveable { mutableStateOf("") }
    var feeling by rememberSaveable { mutableStateOf("") }
    var stillTrue by rememberSaveable { mutableStateOf("") }
    var outControl by rememberSaveable { mutableStateOf("") }
    var inControl by rememberSaveable { mutableStateOf("") }
    var lesson by rememberSaveable { mutableStateOf("") }
    var nextStep by rememberSaveable { mutableStateOf("") }

    val categories = listOf("ACADEMIC", "COMPETITION", "PROJECT", "SOCIAL / SPEECH", "ENERGY")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Reframing Step $step of 8",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = when (step) {
                        1 -> "What was the event or setback?"
                        2 -> "What actually happened (the facts)?"
                        3 -> "How are you feeling right now?"
                        4 -> "What is STILL TRUE about your ability?"
                        5 -> "What was out of your control?"
                        6 -> "What was in your control?"
                        7 -> "What is the single actionable lesson?"
                        8 -> "A tiny, gentle 15-minute next step"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (step) {
                    1 -> {
                        Text(
                            text = "Name the situation calmly. Example: 'Physics quiz marks were 12/20' or 'Junior AI project didn't advance to round 2'.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Setback Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("setback_title_input")
                        )
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSel = category == cat
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { category = cat }
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

                    2 -> {
                        Text(
                            text = "Describe strictly the facts without calling yourself names. What were the questions, the feedback, or the specific errors?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = whatHappened,
                            onValueChange = { whatHappened = it },
                            label = { Text("What Actually Happened") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    3 -> {
                        Text(
                            text = "Honor your emotional state. Disappointment, anxiety, or feeling stung are normal human responses. Validate it without letting it take over.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = feeling,
                            onValueChange = { feeling = it },
                            label = { Text("How am I feeling?") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    4 -> {
                        Text(
                            text = "The Self-Respect Anchor: What is unshakably true about your intelligence, capability, discipline, and worth as a human student?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = stillTrue,
                            onValueChange = { stillTrue = it },
                            label = { Text("What is STILL true about me?") },
                            placeholder = { Text("e.g. I am a dedicated builder; one test does not erase my curiosity or capacity.") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    5 -> {
                        Text(
                            text = "Release what wasn't yours to control. Harsh grading, surprise questions, bad audio in the presentation room, or someone else's mood.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = outControl,
                            onValueChange = { outControl = it },
                            label = { Text("Out of my control") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    6 -> {
                        Text(
                            text = "Take ownership of the controllable variables: Did I skip drawing a diagram? Did I procrastinate practice? Did I rush the proof?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = inControl,
                            onValueChange = { inControl = it },
                            label = { Text("In my control") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    7 -> {
                        Text(
                            text = "Synthesize this into ONE clear engineering or study rule for the future.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = lesson,
                            onValueChange = { lesson = it },
                            label = { Text("Actionable lesson") },
                            placeholder = { Text("e.g. Always write out sign conventions in kinematics before multiplying.") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    8 -> {
                        Text(
                            text = "Choose ONE tiny, gentle 15-minute action that proves you are moving forward with dignity.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = nextStep,
                            onValueChange = { nextStep = it },
                            label = { Text("Gentle 15-minute next step") },
                            placeholder = { Text("e.g. Re-solve the two missed numericals slowly in the Error Log.") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (step < 8) {
                Button(
                    onClick = {
                        if (step == 1 && title.isBlank()) {
                            title = "Recent challenge or test reflection"
                        }
                        step++
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Next Step")
                }
            } else {
                Button(
                    onClick = {
                        val safeTitle = title.ifBlank { "Reflected setback" }
                        val safeHappened = whatHappened.ifBlank { "Encountered friction or missed expected target" }
                        val safeFeeling = feeling.ifBlank { "Disappointed initially, but centered" }
                        val safeStillTrue = stillTrue.ifBlank { "I have deep potential and discipline when I focus on systems." }
                        val safeOutControl = outControl.ifBlank { "External timing and question phrasing" }
                        val safeInControl = inControl.ifBlank { "Preparation structure and error review" }
                        val safeLesson = lesson.ifBlank { "Build systematic practice and check assumptions." }
                        val safeNextStep = nextStep.ifBlank { "Review key concept for 15 minutes calmly." }

                        onSave(safeTitle, category, safeHappened, safeFeeling, safeStillTrue, safeOutControl, safeInControl, safeLesson, safeNextStep)
                    },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("save_setback_reframe_button")
                ) {
                    Text("Integrate with Self-Respect")
                }
            }
        },
        dismissButton = {
            if (step > 1) {
                TextButton(
                    onClick = { step-- },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Previous")
                }
            } else {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}

/**
 * 2. CONFIDENCE LAB: Speaking up, handling doubts, pitching projects, and body language
 */
@Composable
fun ConfidenceLabSubScreen(
    scenarios: List<ConfidenceLabScenario> = DefaultWellnessData.confidenceLabScenarios,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    var selectedScenario by remember { mutableStateOf<ConfidenceLabScenario?>(null) }
    var isPracticingOutLoud by rememberSaveable { mutableStateOf(false) }
    var practiceTimerSeconds by rememberSaveable { mutableIntStateOf(30) }
    var timerRunning by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(timerRunning, practiceTimerSeconds) {
        if (timerRunning && practiceTimerSeconds > 0) {
            delay(1000L)
            practiceTimerSeconds--
        } else if (practiceTimerSeconds == 0) {
            timerRunning = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("confidence_lab_screen"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP APP BAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("close_confidence_lab_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CONFIDENCE LAB",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real speaking scripts, body cues & posture for teenage life.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // HERO MINDSET
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Confidence is a Practiced Muscle",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You don't need to wait until you 'feel fearless' to speak up. Fear is just energy. When you have pre-rehearsed phrases, upright posture, and slow vocal pace, you project quiet, undeniable authority.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // BODY LANGUAGE THREE ESSENTIALS
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3 NON-VERBAL SUPERPOWERS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    NonVerbalRule(
                        icon = "🧘",
                        title = "The 2-Second Breathe-Before-Speaking Rule",
                        desc = "When asked a question or asked to pitch, inhale slowly and pause for 2 full seconds before uttering your first word. Rushing sounds defensive; pausing sounds thoughtful."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NonVerbalRule(
                        icon = "👁️",
                        title = "Triangular Eye Contact",
                        desc = "Alternate eye contact between the left eye, right eye, and bridge of the nose. Never look down at your shoes while delivering an answer."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NonVerbalRule(
                        icon = "🛡️",
                        title = "Open Chest & Grounded Feet",
                        desc = "Keep both feet flat on the floor, shoulders rolled gently back, and avoid fidgeting with pens or sleeves."
                    )
                }
            }
        }

        item {
            Text(
                text = "INTERACTIVE SITUATION PLAYBOOK",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(scenarios, key = { it.id }) { item ->
            ScenarioCard(
                scenario = item,
                onClick = {
                    selectedScenario = item
                    practiceTimerSeconds = 30
                    timerRunning = false
                }
            )
        }
    }

    selectedScenario?.let { scenario ->
        AlertDialog(
            onDismissRequest = {
                selectedScenario = null
                timerRunning = false
            },
            title = {
                Column {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = scenario.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = scenario.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "The Situation:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = scenario.contextDescription,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Common Trap to Avoid:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = scenario.commonMistakeOrTrap,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Exact Word-for-Word Script:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scenario.recommendedScript,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Text(
                        text = "Posture & Physical Cue: ${scenario.bodyLanguageCue}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // TIMED REHEARSAL WIDGET
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "30-Second Out-Loud Practice",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Say the script out loud now with unhurried breathing.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "${practiceTimerSeconds}s",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (practiceTimerSeconds < 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                                Button(
                                    onClick = {
                                        if (practiceTimerSeconds == 0) practiceTimerSeconds = 30
                                        timerRunning = !timerRunning
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = if (timerRunning) Icons.Default.Check else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (timerRunning) "Pause" else "Start Timer")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedScenario = null
                        timerRunning = false
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Done Practice")
                }
            }
        )
    }
}

@Composable
fun NonVerbalRule(icon: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(text = icon, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ScenarioCard(
    scenario: ConfidenceLabScenario,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("scenario_card_${scenario.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = scenario.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Difficulty: ${scenario.difficulty}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = scenario.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"${scenario.recommendedScript}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            FilledTonalButton(
                onClick = onClick,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text("Rehearse")
            }
        }
    }
}

/**
 * 3. FINANCIAL LITERACY & VALUE CREATION SUB-SCREEN
 * Real student wealth foundations, 50/30/20 student budgeting, and skills-to-income experiment log
 */
@Composable
fun FinancialLiteracySubScreen(
    experiments: List<FinancialExperimentEntity>,
    lessons: List<FinancialLesson> = DefaultWellnessData.financialLessons,
    onClose: () -> Unit,
    onAddExperiment: (
        title: String,
        skillUsed: String,
        stage: String,
        potential: String,
        actualEarned: Double,
        clientOrPlatform: String,
        whatLearned: String
    ) -> Unit,
    onDeleteExperiment: (Int) -> Unit
) {
    BackHandler { onClose() }

    var selectedTab by rememberSaveable { mutableStateOf("LESSONS") }
    var showAddExperimentDialog by rememberSaveable { mutableStateOf(false) }

    // Budget Calculator state
    var monthlyInflowText by rememberSaveable { mutableStateOf("2000") }
    val monthlyInflow = monthlyInflowText.toDoubleOrNull() ?: 0.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("financial_literacy_screen"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP APP BAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("close_financial_screen_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "FINANCIAL FOUNDATIONS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Value creation, high-leverage skills & student budgeting.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // TABS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tabs = listOf(
                    "LESSONS" to "Core Concepts",
                    "EXPERIMENTS" to "Value Log (${experiments.size})",
                    "BUDGET" to "50/30/20 Calculator"
                )
                tabs.forEach { (tabKey, label) ->
                    val isSel = selectedTab == tabKey
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedTab = tabKey }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        when (selectedTab) {
            "LESSONS" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Money is an Echo of Value",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "As a student, you build financial leverage not by penny-pinching or falling for get-rich-quick scams, but by developing rare capabilities: technical problem solving, clean writing, and reliability. When you create value for people, income follows naturally.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                items(lessons, key = { it.id }) { lesson ->
                    FinancialLessonCard(lesson = lesson)
                }
            }

            "EXPERIMENTS" -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EARNING & VALUE EXPERIMENTS",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { showAddExperimentDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("add_financial_experiment_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Log Experiment")
                        }
                    }
                }

                val totalEarned = experiments.sumOf { it.actualEarned }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total Value Realized",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "₹${totalEarned.toInt()}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Text(
                                    text = "${experiments.count { it.experimentStage == "DELIVERED" }} Delivered",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                if (experiments.isEmpty()) {
                    item {
                        Text(
                            text = "No experiments logged yet. Try tutoring a junior, setting up a website for a local shop, or entering an NCSC grant competition.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(experiments, key = { it.id }) { exp ->
                        FinancialExperimentCard(
                            experiment = exp,
                            onDelete = { onDeleteExperiment(exp.id) }
                        )
                    }
                }
            }

            "BUDGET" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "STUDENT 50/30/20 ALLOCATION",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Enter any money you receive (pocket money, gifts, prize awards, or tutoring income) to see your safe, guilt-free distribution:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            OutlinedTextField(
                                value = monthlyInflowText,
                                onValueChange = { monthlyInflowText = it },
                                label = { Text("Total Monthly Inflow (₹)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("monthly_inflow_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            val savings50 = monthlyInflow * 0.50
                            val learning30 = monthlyInflow * 0.30
                            val joy20 = monthlyInflow * 0.20

                            BudgetRow(
                                pct = "50%",
                                title = "Emergency & Tools Fund",
                                amount = "₹${savings50.toInt()}",
                                desc = "Hardware, reference books, laptop fund, future college emergency reserve. Kept safe in a zero-balance student bank account."
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            BudgetRow(
                                pct = "30%",
                                title = "Learning & Prototyping",
                                amount = "₹${learning30.toInt()}",
                                desc = "Sensors, domain names, API credits, stationery, coding materials, competition transport."
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            BudgetRow(
                                pct = "20%",
                                title = "Guilt-Free Joy & Family Treats",
                                amount = "₹${joy20.toInt()}",
                                desc = "Snacks with friends, anime merchandise, treating your parents to chai/sweets. Zero guilt."
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddExperimentDialog) {
        AddFinancialExperimentDialog(
            onDismiss = { showAddExperimentDialog = false },
            onSave = { title, skill, stage, pot, actual, client, learned ->
                onAddExperiment(title, skill, stage, pot, actual, client, learned)
                showAddExperimentDialog = false
            }
        )
    }
}

@Composable
fun BudgetRow(pct: String, title: String, amount: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = pct,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = amount,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FinancialLessonCard(lesson: FinancialLesson) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Text(
                        text = lesson.tag,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = lesson.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lesson.coreConcept,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Teen Takeaway:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = lesson.teenTakeaway,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Action: ${lesson.actionableExercise}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FinancialExperimentCard(
    experiment: FinancialExperimentEntity,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (experiment.experimentStage) {
                        "DELIVERED" -> MaterialTheme.colorScheme.primaryContainer
                        "IN_PROGRESS" -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = experiment.experimentStage,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = experiment.skillUsed,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete experiment",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = experiment.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Target / Client: ${experiment.clientOrPlatform} • Potential: ${experiment.estimatedEarningPotential}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Actual Earned: ₹${experiment.actualEarned.toInt()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Value Insight: \"${experiment.whatLearnedAboutValue}\"",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AddFinancialExperimentDialog(
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        skillUsed: String,
        stage: String,
        potential: String,
        actualEarned: Double,
        clientOrPlatform: String,
        whatLearned: String
    ) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var skillUsed by rememberSaveable { mutableStateOf("") }
    var stage by rememberSaveable { mutableStateOf("IDEA") }
    var potential by rememberSaveable { mutableStateOf("") }
    var actualEarnedText by rememberSaveable { mutableStateOf("0") }
    var clientOrPlatform by rememberSaveable { mutableStateOf("") }
    var whatLearned by rememberSaveable { mutableStateOf("") }

    val stages = listOf("IDEA", "IN_PROGRESS", "DELIVERED")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Log Value & Earning Experiment", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Experiment Title") },
                    placeholder = { Text("e.g. Science tutoring for Class 9 neighbor") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = skillUsed,
                    onValueChange = { skillUsed = it },
                    label = { Text("Skill Used") },
                    placeholder = { Text("e.g. Teaching / Web dev / Writing") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Stage",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stages.forEach { st ->
                        val isSel = stage == st
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { stage = st }
                        ) {
                            Text(
                                text = st,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = clientOrPlatform,
                    onValueChange = { clientOrPlatform = it },
                    label = { Text("Target Client or Platform") },
                    placeholder = { Text("e.g. Local print shop / NCSC / Peer") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = potential,
                    onValueChange = { potential = it },
                    label = { Text("Estimated Potential") },
                    placeholder = { Text("e.g. ₹1,000 / prize grant") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = actualEarnedText,
                    onValueChange = { actualEarnedText = it },
                    label = { Text("Actual Earned (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = whatLearned,
                    onValueChange = { whatLearned = it },
                    label = { Text("What did you learn about human value?") },
                    placeholder = { Text("e.g. People value reliability and clear explanations.") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val safeTitle = title.ifBlank { "Untitled Experiment" }
                    val safeSkill = skillUsed.ifBlank { "Problem Solving" }
                    val safePot = potential.ifBlank { "₹0" }
                    val safeEarned = actualEarnedText.toDoubleOrNull() ?: 0.0
                    val safeClient = clientOrPlatform.ifBlank { "Direct" }
                    val safeLearned = whatLearned.ifBlank { "Learned the importance of solving concrete problems." }

                    onSave(safeTitle, safeSkill, stage, safePot, safeEarned, safeClient, safeLearned)
                },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text("Save Experiment")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text("Cancel")
            }
        }
    )
}
