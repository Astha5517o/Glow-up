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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.unit.dp
import com.example.data.MissionJeetLectureEntity
import com.example.data.OsTaskEntity
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MissionJeetSubScreen(
    lectures: List<MissionJeetLectureEntity>,
    onClose: () -> Unit,
    onSaveLecture: (MissionJeetLectureEntity) -> Unit,
    onUpdateLecture: (MissionJeetLectureEntity) -> Unit,
    onStartFocusSession: (OsTaskEntity) -> Unit
) {
    BackHandler { onClose() }

    var selectedSubjectFilter by rememberSaveable { mutableStateOf("All") }
    var lectureToEdit by rememberSaveable { mutableStateOf<MissionJeetLectureEntity?>(null) }
    var showAddLectureDialog by rememberSaveable { mutableStateOf(false) }

    val subjectFilters = listOf(
        "All",
        "Physics",
        "Chemistry Physical",
        "Chemistry Organic/Inorganic",
        "Mathematics",
        "Computer Science / AI"
    )

    val filteredLectures = if (selectedSubjectFilter == "All") {
        lectures
    } else {
        lectures.filter { it.subject.contains(selectedSubjectFilter, ignoreCase = true) }
    }

    val inProgressCount = lectures.count { it.status == "IN_PROGRESS" }
    val completedCount = lectures.count { it.status == "LESSON_COMPLETED" || it.status == "REVIEWED" }
    val totalQuestionsSolved = lectures.sumOf { it.questionsSolvedCount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("mission_jeet_screen"),
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🎯 MISSION JEET TRACKER",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Self-Paced Continuity • Chunks • Active Recall • Questions & Mistake Review",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                FilledTonalButton(
                    onClick = { showAddLectureDialog = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ Lesson")
                }
            }
        }

        // Summary Statistics Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$inProgressCount",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "In Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$completedCount",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = GentleSuccessGreen
                        )
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalQuestionsSolved",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Questions Solved",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Subject Filter Scrollable Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjectFilters.forEach { subject ->
                    val isSelected = selectedSubjectFilter == subject
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSubjectFilter = subject }
                    ) {
                        Text(
                            text = subject,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Lecture Items List
        items(filteredLectures, key = { it.id }) { lecture ->
            LectureCard(
                lecture = lecture,
                onEdit = { lectureToEdit = lecture },
                onStartChunk = {
                    val task = OsTaskEntity(
                        title = "${lecture.subject}: ${lecture.topic}",
                        subtitle = lecture.nextAction,
                        whyItMatters = "Mission Jeet lecture chunk & active understanding.",
                        domain = "ACADEMIC",
                        durationMinutes = 25,
                        scheduledTimeSlot = "Evening",
                        energyRequired = "HIGH",
                        subject = lecture.subject
                    )
                    onStartFocusSession(task)
                }
            )
        }

        if (filteredLectures.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Mission Jeet lectures recorded for this subject yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = { showAddLectureDialog = true }) {
                            Text("+ Add First Lecture")
                        }
                    }
                }
            }
        }
    }

    // Edit/Update Lecture Dialog
    lectureToEdit?.let { currentLecture ->
        var positionInput by rememberSaveable { mutableStateOf(currentLecture.lastWatchedPositionText) }
        var statusInput by rememberSaveable { mutableStateOf(currentLecture.status) }
        var notesInput by rememberSaveable { mutableStateOf(currentLecture.doubtsAndNotes) }
        var sectionsInput by rememberSaveable { mutableStateOf(currentLecture.sectionsCompletedCsv) }
        var confidenceInput by rememberSaveable { mutableStateOf(currentLecture.conceptConfidence) }
        var attemptedInput by rememberSaveable { mutableIntStateOf(currentLecture.questionsAttemptedCount) }
        var solvedInput by rememberSaveable { mutableIntStateOf(currentLecture.questionsSolvedCount) }
        var mistakesInput by rememberSaveable { mutableStateOf(currentLecture.mistakesIdentified) }
        var nextActionInput by rememberSaveable { mutableStateOf(currentLecture.nextAction) }

        AlertDialog(
            onDismissRequest = { lectureToEdit = null },
            title = {
                Text("Update Mission Jeet Progress", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "${currentLecture.subject} • ${currentLecture.chapter}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = positionInput,
                        onValueChange = { positionInput = it },
                        label = { Text("Timestamp / Position (e.g. 42:15 or 1h 10m)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sectionsInput,
                        onValueChange = { sectionsInput = it },
                        label = { Text("Sections completed (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Key formulas, concepts & doubts") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mistakesInput,
                        onValueChange = { mistakesInput = it },
                        label = { Text("Mistakes or weak points identified") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nextActionInput,
                        onValueChange = { nextActionInput = it },
                        label = { Text("Next Action (Concrete next step)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Status:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "NOT_STARTED",
                            "IN_PROGRESS",
                            "LESSON_COMPLETED",
                            "RECALL_PENDING",
                            "PRACTICE_PENDING",
                            "REVIEWED"
                        ).forEach { st ->
                            val sel = statusInput == st
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { statusInput = st }
                            ) {
                                Text(
                                    text = st.replace("_", " "),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Questions Solved / Attempted:")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { if (solvedInput > 0) solvedInput -= 1 }) { Text("-") }
                            Text("$solvedInput", fontWeight = FontWeight.Bold)
                            TextButton(onClick = {
                                solvedInput += 1
                                if (solvedInput > attemptedInput) attemptedInput = solvedInput
                            }) { Text("+") }
                            Text(" / $attemptedInput")
                            TextButton(onClick = { attemptedInput += 1 }) { Text("+Att") }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateLecture(
                            currentLecture.copy(
                                lastWatchedPositionText = positionInput,
                                sectionsCompletedCsv = sectionsInput,
                                doubtsAndNotes = notesInput,
                                mistakesIdentified = mistakesInput,
                                nextAction = nextActionInput,
                                status = statusInput,
                                conceptConfidence = confidenceInput,
                                questionsSolvedCount = solvedInput,
                                questionsAttemptedCount = attemptedInput,
                                recallCompleted = statusInput != "RECALL_PENDING" && statusInput != "NOT_STARTED"
                            )
                        )
                        lectureToEdit = null
                    }
                ) {
                    Text("Save Progress")
                }
            },
            dismissButton = {
                TextButton(onClick = { lectureToEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Lecture Dialog
    if (showAddLectureDialog) {
        var subjectInput by rememberSaveable { mutableStateOf("Physics") }
        var chapterInput by rememberSaveable { mutableStateOf("") }
        var topicInput by rememberSaveable { mutableStateOf("") }
        var titleInput by rememberSaveable { mutableStateOf("") }
        var durationMinutesInput by rememberSaveable { mutableIntStateOf(90) }
        var nextActionInput by rememberSaveable { mutableStateOf("Watch first 25 mins & write active formula notes") }

        AlertDialog(
            onDismissRequest = { showAddLectureDialog = false },
            title = { Text("Add Mission Jeet Lecture", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Subject:", style = MaterialTheme.typography.labelSmall)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Physics", "Chemistry Physical", "Chemistry Organic/Inorganic", "Mathematics", "Computer Science / AI").forEach { s ->
                            val isSel = subjectInput == s
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { subjectInput = s }
                            ) {
                                Text(
                                    text = s,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = chapterInput,
                        onValueChange = { chapterInput = it },
                        label = { Text("Chapter name (e.g. Work, Energy & Power)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text("Topic name (e.g. Work-Energy Theorem)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Lecture Title / Reference (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nextActionInput,
                        onValueChange = { nextActionInput = it },
                        label = { Text("First Concrete Action") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (chapterInput.isNotBlank() && topicInput.isNotBlank()) {
                            onSaveLecture(
                                MissionJeetLectureEntity(
                                    subject = subjectInput,
                                    chapter = chapterInput.trim(),
                                    topic = topicInput.trim(),
                                    lectureTitle = titleInput.ifBlank { "Mission Jeet: $chapterInput • $topicInput" },
                                    totalDurationMinutes = durationMinutesInput,
                                    nextAction = nextActionInput
                                )
                            )
                            showAddLectureDialog = false
                        }
                    }
                ) {
                    Text("Add Lecture")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddLectureDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LectureCard(
    lecture: MissionJeetLectureEntity,
    onEdit: () -> Unit,
    onStartChunk: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadgeChip(
                    label = lecture.subject,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                StatusBadgeChip(
                    label = lecture.status.replace("_", " "),
                    containerColor = when (lecture.status) {
                        "LESSON_COMPLETED", "REVIEWED" -> GentleSuccessLight
                        "IN_PROGRESS" -> WarmPeachLight
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = when (lecture.status) {
                        "LESSON_COMPLETED", "REVIEWED" -> GentleSuccessGreen
                        "IN_PROGRESS" -> WarmPeachHighlight
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Text(
                text = "${lecture.chapter} — ${lecture.topic}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = lecture.lectureTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Watched: ${lecture.lastWatchedPositionText} / ${lecture.totalDurationMinutes}m",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Practice: ${lecture.questionsSolvedCount}/${lecture.questionsAttemptedCount} solved",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (lecture.questionsSolvedCount > 0) GentleSuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (lecture.sectionsCompletedCsv.isNotBlank()) {
                Text(
                    text = "Completed chunks: ${lecture.sectionsCompletedCsv}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (lecture.doubtsAndNotes.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notes: ${lecture.doubtsAndNotes}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (lecture.mistakesIdentified.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = WarmPeachLight.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Mistake review: ${lecture.mistakesIdentified}",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmPeachHighlight,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NEXT ACTION",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = lecture.nextAction,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Update", style = MaterialTheme.typography.labelMedium)
                }
                Button(
                    onClick = onStartChunk,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Start 25m Focus", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
