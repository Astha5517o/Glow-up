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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.DefaultWellnessData
import com.example.data.ErrorLogEntity
import com.example.data.StudyChapterEntity
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.SoftCoralAccent
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import com.example.viewmodel.ActiveSubScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudyScreen(
    chapters: List<StudyChapterEntity>,
    errorLogs: List<ErrorLogEntity>,
    onStartStudyMission: (StudyChapterEntity) -> Unit,
    onOpenOneShotMode: (StudyChapterEntity) -> Unit,
    onOpenPracticeQuestions: (String) -> Unit,
    onOpenErrorLog: () -> Unit,
    onOpenImStuck: () -> Unit,
    onOpenYoutubeMission: () -> Unit,
    onOpenRevisionWithAi: () -> Unit,
    onAddChapter: (subject: String, chapterName: String, currentTopic: String, weakAreas: String) -> Unit,
    onOpenChemistryMission: () -> Unit = {},
    onOpenAcademicExcellence: () -> Unit = {},
    onOpenMissionJeetTracker: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSubjectFilter by rememberSaveable { mutableStateOf("All") }
    var showAddChapterDialog by rememberSaveable { mutableStateOf(false) }
    var targetChapterIndex by rememberSaveable { mutableStateOf(0) }

    val primaryChapter = if (chapters.isNotEmpty()) {
        chapters[targetChapterIndex % chapters.size]
    } else null
    val totalSolved = chapters.sumOf { it.questionsSolved }
    val totalAttempted = chapters.sumOf { it.questionsAttempted }
    val unresolvedMistakesCount = errorLogs.count { !it.isResolved }

    val filteredChapters = if (selectedSubjectFilter == "All") {
        chapters
    } else {
        chapters.filter { it.subject == selectedSubjectFilter }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("study_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // HEADER: STUDY
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "STUDY",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "UNDERSTAND → RECALL → PRACTICE → ANALYZE MISTAKE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledTonalButton(
                    onClick = onOpenImStuck,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("study_im_stuck_button")
                ) {
                    Text("🧩 I'M STUCK", fontWeight = FontWeight.Bold)
                }
            }
        }

        // STRATEGIC ENGINES BANNER (CHEMISTRY FULL-MARKS & ACADEMIC EXCELLENCE)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onOpenChemistryMission() }
                        .testTag("banner_chemistry_mission")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "🎯 CHEMISTRY 100",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Principal's challenge: reactions & numericals",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onOpenAcademicExcellence() }
                        .testTag("banner_academic_excellence")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "📚 ACADEMIC ENGINE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "PCM + CS/AI & competitive overlap (JEE/SAT)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // MISSION JEET LECTURE CONTINUITY
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onOpenMissionJeetTracker() }
                    .testTag("study_mission_jeet_card")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🎯 MISSION JEET LECTURE TRACKER",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Track batch lectures chunk by chunk: active notes, recall, questions attempted & mistake review.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FilledTonalButton(
                        onClick = onOpenMissionJeetTracker,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Track Chunks")
                    }
                }
            }
        }

        // MAIN STUDY TARGET CARD + CONTINUE STUDY + PRACTICE QUESTIONS
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("study_target_card")
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S STUDY TARGET",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatusBadgeChip(
                                label = "${primaryChapter?.subject ?: "Physics"} • 45 MIN",
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (chapters.isNotEmpty()) {
                                            targetChapterIndex = (targetChapterIndex + 1) % chapters.size
                                        }
                                    }
                                    .testTag("switch_study_target_button")
                            ) {
                                Text(
                                    text = "Switch ↻",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = primaryChapter?.chapterName ?: "Rotational Motion & Moment of Inertia",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Topic: ${primaryChapter?.currentTopic ?: "Torque & Angular Momentum"}\nWeak area: ${primaryChapter?.weakAreas ?: "Conservation of angular momentum"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Compact Stats Row (Questions solved & Mistakes to review)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "$totalSolved / $totalAttempted",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Questions solved",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = WarmPeachLight,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onOpenErrorLog() }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "$unresolvedMistakesCount to review",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = WarmPeachHighlight,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Mistakes in Error Log",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmPeachHighlight
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary & Secondary Buttons
                    PrimaryButton(
                        text = "CONTINUE STUDY",
                        onClick = {
                            if (primaryChapter != null) {
                                onStartStudyMission(primaryChapter)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "continue_study_button"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SecondaryButton(
                        text = "PRACTICE QUESTIONS",
                        onClick = { onOpenPracticeQuestions("All") },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "practice_questions_button"
                    )
                }
            }
        }

        // QUICK STUDY MODES: REVISION WITH ASTHA AI | ERROR LOG | ONE-SHOT | YOUTUBE MISSION
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudyToolCard(
                    title = "REVISION",
                    subtitle = "Coach with ASTHA AI",
                    emoji = "✨",
                    onClick = onOpenRevisionWithAi,
                    testTag = "study_tool_revision_ai",
                    modifier = Modifier.weight(1f)
                )
                StudyToolCard(
                    title = "ERROR LOG",
                    subtitle = "$unresolvedMistakesCount mistakes due",
                    emoji = "📓",
                    onClick = onOpenErrorLog,
                    testTag = "study_tool_error_log",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudyToolCard(
                    title = "ONE-SHOT MODE",
                    subtitle = "3-note capture + check",
                    emoji = "🎯",
                    onClick = {
                        primaryChapter?.let { onOpenOneShotMode(it) }
                    },
                    testTag = "study_tool_one_shot",
                    modifier = Modifier.weight(1f)
                )
                StudyToolCard(
                    title = "YOUTUBE MISSION",
                    subtitle = "20m max → 3 ideas → 5 Qs",
                    emoji = "▶️",
                    onClick = onOpenYoutubeMission,
                    testTag = "study_tool_youtube_mission",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // CORE 45-MIN STUDY ENGINE BLUEPRINT
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "CORE STUDY ENGINE — 45 MIN MISSION",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val blocks = listOf(
                        "0–5 min" to "Active recall (write formulas without notes)",
                        "5–10 min" to "Easy warm-up questions (2 questions)",
                        "10–25 min" to "Medium targeted questions (3–5 questions)",
                        "25–38 min" to "Challenge / application problem",
                        "38–45 min" to "Mistake analysis & Error Log entry"
                    )
                    blocks.forEach { (timeRange, desc) ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = timeRange,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(78.dp)
                            )
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // SUBJECTS & CHAPTERS + SUBJECT-SPECIFIC STUDY METHODS
        item {
            SectionHeader(
                title = "SUBJECTS & STUDY HISTORY",
                subtitle = "Subject-specific workflows & understanding checks",
                actionText = "+ Chapter",
                onActionClick = { showAddChapterDialog = true }
            )
            Spacer(modifier = Modifier.height(10.dp))

            val subjects = listOf(
                "All",
                "Physics",
                "Chemistry Physical",
                "Chemistry Organic/Inorganic",
                "Mathematics",
                "Computer Science / AI"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subj ->
                    val isSel = selectedSubjectFilter == subj
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSubjectFilter = subj }
                    ) {
                        Text(
                            text = subj,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Subject-specific workflow banner when a specific subject is selected
        if (selectedSubjectFilter != "All") {
            val methodSteps = DefaultWellnessData.subjectMethods[selectedSubjectFilter].orEmpty()
            if (methodSteps.isNotEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "$selectedSubjectFilter Study Method",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = methodSteps.joinToString("  →  "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }

        items(filteredChapters, key = { it.id }) { chapter ->
            ChapterStudyCard(
                chapter = chapter,
                onStartMission = { onStartStudyMission(chapter) },
                onOpenOneShot = { onOpenOneShotMode(chapter) },
                onPracticeSubject = { onOpenPracticeQuestions(chapter.subject) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showAddChapterDialog) {
        var subjectInput by rememberSaveable { mutableStateOf("Physics") }
        var chapterInput by rememberSaveable { mutableStateOf("") }
        var topicInput by rememberSaveable { mutableStateOf("") }
        var weakInput by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddChapterDialog = false },
            title = { Text("Add Chapter to Study Engine") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Physics", "Chemistry Physical", "Chemistry Organic/Inorganic", "Mathematics", "Computer Science / AI").forEach { s ->
                            OutlinedButton(
                                onClick = { subjectInput = s },
                                border = BorderStroke(
                                    if (subjectInput == s) 2.dp else 1.dp,
                                    if (subjectInput == s) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text(s.take(10), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = chapterInput,
                        onValueChange = { chapterInput = it },
                        label = { Text("Chapter Name (e.g., Gravitation)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text("Current Topic (e.g., Orbital Velocity)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = weakInput,
                        onValueChange = { weakInput = it },
                        label = { Text("Weak area to watch") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (chapterInput.isNotBlank()) {
                            onAddChapter(subjectInput, chapterInput, topicInput, weakInput)
                            showAddChapterDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddChapterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StudyToolCard(
    title: String,
    subtitle: String,
    emoji: String,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .minimumInteractiveComponentSize()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ChapterStudyCard(
    chapter: StudyChapterEntity,
    onStartMission: () -> Unit,
    onOpenOneShot: () -> Unit,
    onPracticeSubject: () -> Unit
) {
    val (statusLabel, badgeBg, badgeFg) = when (chapter.understandingStatus) {
        "GREEN" -> Triple("GREEN: Can explain it", GentleSuccessLight, GentleSuccessGreen)
        "RED" -> Triple("RED: Needs clarification", WarmPeachLight, SoftCoralAccent)
        else -> Triple("YELLOW: Need practice Qs", WarmPeachLight, WarmPeachHighlight)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chapter_card_${chapter.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chapter.subject.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                StatusBadgeChip(
                    label = statusLabel,
                    containerColor = badgeBg,
                    contentColor = badgeFg
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = chapter.chapterName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Topic: ${chapter.currentTopic}\nWeak area: ${chapter.weakAreas}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (chapter.capturedFormula.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Formula recall: ${chapter.capturedFormula}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Solved ${chapter.questionsSolved}/${chapter.questionsAttempted}  •  Partial ${chapter.questionsPartial}  •  Wrong ${chapter.questionsWrong}  •  Adaptive focus: ${chapter.dominantWeakness}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartMission,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("45m Block", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = onOpenOneShot,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.PlayCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("One-Shot", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = onPracticeSubject,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Practice", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
