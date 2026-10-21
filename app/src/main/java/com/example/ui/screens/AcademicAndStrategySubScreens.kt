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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.AcademicTopicEntity
import com.example.data.AngerOverwhelmPauseEntity
import com.example.data.ProfileEvidenceLedgerEntity
import com.example.data.ScenarioPlanEntity
import com.example.data.ScholarshipStrategyEntity
import com.example.data.ScienceExhibitionScorecardEntity
import com.example.ui.theme.GentleSuccessGreen

// =========================================================================
// 1. CHEMISTRY FULL-MARKS MISSION (PRINCIPAL'S CHALLENGE)
// =========================================================================
@Composable
fun ChemistryMissionSubScreen(
    topics: List<AcademicTopicEntity>,
    onClose: () -> Unit,
    onUpdateTopic: (AcademicTopicEntity) -> Unit,
    onAddTopic: (AcademicTopicEntity) -> Unit
) {
    BackHandler { onClose() }

    val chemTopics = topics.filter { it.subject.startsWith("Chemistry") || it.isChemistryFullMarksFocus }
    var selectedSection by rememberSaveable { mutableStateOf("ALL") }
    var showAddTopicDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("chemistry_mission_screen"),
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
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("close_chemistry_mission_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CHEMISTRY FULL-MARKS MISSION",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Preparation for excellence — not fear of disappointing anyone.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // MISSION BANNER
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
                        Text("🧪", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Principal's 100% Challenge Stance",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Full marks is never promised, but systematic preparation guarantees mastery. We conquer Chemistry through 4 pillars: 1) Flawless Written Definitions & Laws, 2) Precise Reaction Equations with states & conditions, 3) Rigorous Sign & Unit Conventions in Numericals, 4) Error Log Diagnostic Analysis.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { showAddTopicDialog = true },
                            modifier = Modifier.minimumInteractiveComponentSize(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Chapter / Focus")
                        }
                    }
                }
            }
        }

        // SECTION TABS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "ALL" to "All Chapters (${chemTopics.size})",
                    "PHYSICAL" to "Physical (Numericals)",
                    "ORGANIC_INORGANIC" to "Inorganic / Organic (Reactions & Trends)"
                ).forEach { (key, label) ->
                    val isSel = selectedSection == key
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSection = key }
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

        val filteredChem = chemTopics.filter {
            when (selectedSection) {
                "PHYSICAL" -> it.subject.contains("Physical", ignoreCase = true)
                "ORGANIC_INORGANIC" -> !it.subject.contains("Physical", ignoreCase = true)
                else -> true
            }
        }

        if (filteredChem.isEmpty()) {
            item {
                Text(
                    text = "No Chemistry topics added yet. Tap '+ Add Chapter / Focus' above to add your syllabus.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(filteredChem, key = { it.id }) { topic ->
                ChemistryTopicCard(
                    topic = topic,
                    onUpdateTopic = onUpdateTopic
                )
            }
        }
    }

    if (showAddTopicDialog) {
        AddAcademicTopicDialog(
            defaultSubject = "Chemistry Physical",
            isChemistryMissionDefault = true,
            onDismiss = { showAddTopicDialog = false },
            onSave = {
                onAddTopic(it)
                showAddTopicDialog = false
            }
        )
    }
}

@Composable
fun ChemistryTopicCard(
    topic: AcademicTopicEntity,
    onUpdateTopic: (AcademicTopicEntity) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.dp,
            if (topic.practiceLevel == "MASTERED") GentleSuccessGreen.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chem_topic_card_${topic.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = topic.chapterName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (topic.practiceLevel) {
                        "MASTERED" -> GentleSuccessGreen.copy(alpha = 0.2f)
                        "EXAM_LEVEL" -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = topic.practiceLevel.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.HelpOutline else Icons.Default.Edit,
                        contentDescription = "Expand topic details"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = topic.topicName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 4-STAGE MASTERY CHECKLIST (Watching -> Understanding -> Recall -> Exam practice)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MasteryChip(
                    label = "Concept Understood",
                    checked = topic.isConceptUnderstood,
                    onToggle = { onUpdateTopic(topic.copy(isConceptUnderstood = !topic.isConceptUnderstood)) }
                )
                MasteryChip(
                    label = "Independent Recall",
                    checked = topic.hasIndependentRecall,
                    onToggle = { onUpdateTopic(topic.copy(hasIndependentRecall = !topic.hasIndependentRecall)) }
                )
            }

            if (topic.definitionsAndReactionsNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Core Laws & Reactions: ${topic.definitionsAndReactionsNotes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (expanded) 8 else 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (topic.mistakePatterns.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "⚠️ Mistake Trap: ${topic.mistakePatterns}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = if (expanded) 6 else 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    if (topic.calculationsAndNumericalsNotes.isNotBlank()) {
                        Text(
                            text = "Numericals & Calculations Guide:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = topic.calculationsAndNumericalsNotes,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Practice Level:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("SCHOOL_LEVEL", "EXAM_LEVEL", "MASTERED").forEach { lvl ->
                                val isLvl = topic.practiceLevel == lvl
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isLvl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onUpdateTopic(topic.copy(practiceLevel = lvl)) }
                                ) {
                                    Text(
                                        text = lvl.replace("_", " "),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isLvl) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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

@Composable
fun MasteryChip(label: String, checked: Boolean, onToggle: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (checked) GentleSuccessGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, if (checked) GentleSuccessGreen else MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (checked) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = if (checked) GentleSuccessGreen else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (checked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// =========================================================================
// 2. ACADEMIC EXCELLENCE ENGINE (SUBJECT-BY-SUBJECT & COMPETITIVE OVERLAPS)
// =========================================================================
@Composable
fun AcademicExcellenceSubScreen(
    topics: List<AcademicTopicEntity>,
    onClose: () -> Unit,
    onUpdateTopic: (AcademicTopicEntity) -> Unit,
    onAddTopic: (AcademicTopicEntity) -> Unit,
    onDeleteTopic: (Int) -> Unit
) {
    BackHandler { onClose() }

    var selectedSubject by rememberSaveable { mutableStateOf("ALL") }
    var filterTeacherGuidanceOnly by rememberSaveable { mutableStateOf(false) }
    var showAddTopicDialog by rememberSaveable { mutableStateOf(false) }

    val subjects = listOf("ALL", "Physics", "Chemistry Physical", "Chemistry Organic/Inorganic", "Mathematics", "Computer Science & AI")

    val filteredTopics = topics.filter {
        val matchesSubj = if (selectedSubject == "ALL") true else it.subject.equals(selectedSubject, ignoreCase = true)
        val matchesTeacher = if (filterTeacherGuidanceOnly) it.requiresTeacherGuidance else true
        matchesSubj && matchesTeacher
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("academic_excellence_screen"),
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
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("close_academic_excellence_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ACADEMIC EXCELLENCE ENGINE",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Subject-by-subject syllabus, independent recall & competitive alignment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // COMPETITIVE EXAM ALIGNMENT CALLOUT (JEE / SAT / EJU Overlap)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Class 11–12 Foundation Overlap",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "85% of concepts in CBSE Class 11 PCM form the common denominator for JEE Main, MEXT Written Exam (Math A/B & Chemistry), and SAT Math. Master school NCERT & coaching modules deeply first. Simultaneous exam-specific cramming is avoided.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // SUBJECT SELECTOR
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subj ->
                    val isSel = selectedSubject == subj
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSubject = subj }
                    ) {
                        Text(
                            text = subj,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = filterTeacherGuidanceOnly,
                        onCheckedChange = { filterTeacherGuidanceOnly = it }
                    )
                    Text("Requires Teacher Guidance", style = MaterialTheme.typography.labelMedium)
                }

                Button(
                    onClick = { showAddTopicDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Topic")
                }
            }
        }

        items(filteredTopics, key = { it.id }) { topic ->
            AcademicTopicDetailCard(
                topic = topic,
                onUpdateTopic = onUpdateTopic,
                onDeleteTopic = { onDeleteTopic(topic.id) }
            )
        }
    }

    if (showAddTopicDialog) {
        AddAcademicTopicDialog(
            defaultSubject = if (selectedSubject != "ALL") selectedSubject else "Physics",
            isChemistryMissionDefault = false,
            onDismiss = { showAddTopicDialog = false },
            onSave = {
                onAddTopic(it)
                showAddTopicDialog = false
            }
        )
    }
}

@Composable
fun AcademicTopicDetailCard(
    topic: AcademicTopicEntity,
    onUpdateTopic: (AcademicTopicEntity) -> Unit,
    onDeleteTopic: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
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
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Text(
                        text = topic.subject,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = topic.chapterName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDeleteTopic, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete topic", tint = MaterialTheme.colorScheme.outline)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = topic.topicName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 4-Stage Mastery Progress Indicator
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val stages = listOf(
                    "NOT_STARTED" to "1. Not Started",
                    "LESSON_WATCHED" to "2. Lesson Watched",
                    "SCHOOL_LEVEL" to "3. School Questions",
                    "EXAM_LEVEL" to "4. Exam Questions",
                    "MASTERED" to "5. Mastered"
                )
                stages.forEach { (stgKey, label) ->
                    val isCurrent = topic.practiceLevel == stgKey
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onUpdateTopic(topic.copy(practiceLevel = stgKey)) }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (topic.requiresTeacherGuidance) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ask Teacher: ${topic.teacherGuidanceNotes.ifBlank { "Concept doubt or multi-step question clarification needed." }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. SCIENCE EXHIBITION & INNOVATION ENGINE (SCORECARD & 10 MILESTONES)
// =========================================================================
@Composable
fun ScienceExhibitionSubScreen(
    scorecards: List<ScienceExhibitionScorecardEntity>,
    onClose: () -> Unit,
    onAddScorecard: (ScienceExhibitionScorecardEntity) -> Unit,
    onUpdateScorecard: (ScienceExhibitionScorecardEntity) -> Unit,
    onSelectForExecution: (Int) -> Unit,
    onAdvanceMilestone: (Int) -> Unit,
    onDeleteScorecard: (Int) -> Unit
) {
    BackHandler { onClose() }

    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    val milestones = listOf(
        1 to "1. Problem validation & field observations",
        2 to "2. Existing-solution & patent research",
        3 to "3. Proposed genuine differentiation",
        4 to "4. Design, dimensions & materials budget",
        5 to "5. Functional scale prototype",
        6 to "6. Testing, observations & flow logs",
        7 to "7. Engineering iteration & improvement",
        8 to "8. Formal report, charts & evidence",
        9 to "9. 90-second judge presentation rehearsal",
        10 to "10. Hard judge Q&A practice (weaknesses & limitations)"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("science_exhibition_screen"),
        contentPadding = PaddingValues(20.dp),
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
                        text = "SCIENCE EXHIBITION & INNOVATION",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Score candidates with transparent criteria & evidence — not hype.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // HERO PRINCIPLE
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Controllable Excellence, Not Guaranteed Winning",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "No exhibition idea can promise a national award. We maximize the controllable variables: a validated human problem, sound physics/chemistry principles, a working demonstration, real testing data, honest limitations, and calm explanation under questioning.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Score a New Candidate Idea")
                    }
                }
            }
        }

        item {
            Text(
                text = "PROJECT SCORECARD COMPARISON",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(scorecards, key = { it.id }) { item ->
            ScorecardItemCard(
                card = item,
                milestones = milestones,
                onSelectForExecution = { onSelectForExecution(item.id) },
                onAdvanceMilestone = { onAdvanceMilestone(item.id) },
                onDelete = { onDeleteScorecard(item.id) }
            )
        }
    }

    if (showAddDialog) {
        AddExhibitionIdeaDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                onAddScorecard(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ScorecardItemCard(
    card: ScienceExhibitionScorecardEntity,
    milestones: List<Pair<Int, String>>,
    onSelectForExecution: () -> Unit,
    onAdvanceMilestone: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.dp,
            if (card.isSelectedForExecution) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (card.isSelectedForExecution) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "ACTIVE EXECUTION",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = "Score: ${card.totalScore}/70",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete candidate", tint = MaterialTheme.colorScheme.outline)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = card.ideaTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Differentiation: ${card.genuineDifferentiation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (card.unknownInformation.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Unverified / Needs Research: ${card.unknownInformation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (card.potentialWeaknessesAndQuestions.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Judge Trap: ${card.potentialWeaknessesAndQuestions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // 10 MILESTONES (FOR ACTIVE PROJECT)
            if (card.isSelectedForExecution) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(8.dp))

                val currentLabel = milestones.find { it.first == card.executionMilestone }?.second ?: "Stage ${card.executionMilestone}"
                Text(
                    text = "Current Milestone (${card.executionMilestone}/10):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = currentLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Stop-or-Pivot Checkpoint: ${card.stopOrPivotStatus}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = onAdvanceMilestone,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("Next Milestone (${card.executionMilestone + 1}/10)")
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onSelectForExecution,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
                ) {
                    Text("Select for 10-Step Execution")
                }
            }
        }
    }
}

// =========================================================================
// 4. SCHOLARSHIP & INTERNATIONAL UNIVERSITY STRATEGY (MEXT & FUNDED ROUTES)
// =========================================================================
@Composable
fun ScholarshipStrategySubScreen(
    strategies: List<ScholarshipStrategyEntity>,
    onClose: () -> Unit,
    onAddStrategy: (ScholarshipStrategyEntity) -> Unit,
    onUpdateStrategy: (ScholarshipStrategyEntity) -> Unit,
    onDeleteStrategy: (Int) -> Unit
) {
    BackHandler { onClose() }

    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("scholarship_strategy_screen"),
        contentPadding = PaddingValues(20.dp),
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
                        text = "VERIFIED SCHOLARSHIPS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Zero fake deadlines. Genuine fully funded undergraduate routes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // MEXT APPLICATION YEAR VS ENTRY YEAR CLARITY CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🇯🇵", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "MEXT Cycle Timing Rule",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "IMPORTANT: The year you APPLY is one year before the year you BEGIN university. The 2027 university-entry cycle embassy recommendation in India closed in May/June 2026. The next official cycle (for 2028 university entry) opens in April/May 2027.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Official source: Embassy of Japan in India (in.emb-japan.go.jp). Never assume dates or eligibility without checking official announcements.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VERIFIED OPPORTUNITIES (${strategies.size})",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Opportunity")
                }
            }
        }

        items(strategies, key = { it.id }) { strategy ->
            ScholarshipCard(
                strategy = strategy,
                onUpdate = onUpdateStrategy,
                onDelete = { onDeleteStrategy(strategy.id) }
            )
        }
    }

    if (showAddDialog) {
        AddScholarshipDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                onAddStrategy(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ScholarshipCard(
    strategy: ScholarshipStrategyEntity,
    onUpdate: (ScholarshipStrategyEntity) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
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
                    color = if (strategy.isFullyFunded) GentleSuccessGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = if (strategy.isFullyFunded) "FULL RIDE" else "PARTIAL",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (strategy.isFullyFunded) GentleSuccessGreen else MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strategy.country,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.outline)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = strategy.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Coverage: ${strategy.fundingCoverage}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (strategy.cycleOfficialStatusNotice.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = strategy.cycleOfficialStatusNotice,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Next: ${strategy.nextAction}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(if (expanded) "Less" else "Verified Details")
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text("Official Portal: ${strategy.officialSourceLink}", style = MaterialTheme.typography.bodySmall)
                    Text("Required Exams: ${strategy.requiredExaminations}", style = MaterialTheme.typography.bodySmall)
                    Text("Application Fee: ${strategy.applicationFeeVerified}", style = MaterialTheme.typography.bodySmall)
                    Text("Excludes: ${strategy.excludedCosts}", style = MaterialTheme.typography.bodySmall)
                    Text("Last Verified: ${strategy.lastVerifiedDate}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// =========================================================================
// 5. SCENARIO PLANNING SUB-SCREEN (BEST, EXPECTED, SETBACK)
// =========================================================================
@Composable
fun ScenarioPlanningSubScreen(
    scenarios: List<ScenarioPlanEntity>,
    onClose: () -> Unit,
    onAddScenario: (ScenarioPlanEntity) -> Unit,
    onUpdateScenario: (ScenarioPlanEntity) -> Unit,
    onDeleteScenario: (Int) -> Unit
) {
    BackHandler { onClose() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("scenario_planning_screen"),
        contentPadding = PaddingValues(20.dp),
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
                        text = "3-SCENARIO PLANNING",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Plan for success without being paralyzed by fear of setbacks.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(scenarios, key = { it.id }) { item ->
            ScenarioCardItem(scenario = item)
        }
    }
}

@Composable
fun ScenarioCardItem(scenario: ScenarioPlanEntity) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = scenario.goalCategory,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // BEST CASE
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GentleSuccessGreen.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🌟 BEST CASE:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = GentleSuccessGreen)
                    Text(scenario.bestCaseOutcome, style = MaterialTheme.typography.bodySmall)
                    Text("Action to maximize: ${scenario.bestCaseActionToMaximize}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // EXPECTED CASE
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🎯 EXPECTED / BASELINE CASE:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(scenario.expectedCaseOutcome, style = MaterialTheme.typography.bodySmall)
                    Text("Realistic plan: ${scenario.expectedCaseRealisticPlan}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // SETBACK CASE
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🛡️ SETBACK / WORST CASE (RECOVERY READY):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    Text("Risk: ${scenario.setbackCaseRisk}", style = MaterialTheme.typography.bodySmall)
                    Text("Preventive: ${scenario.setbackPreventiveAction}", style = MaterialTheme.typography.labelSmall)
                    Text("Viable Alternative: ${scenario.setbackRecoveryAlternative}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// =========================================================================
// 6. PROFILE EVIDENCE LEDGER SUB-SCREEN
// =========================================================================
@Composable
fun ProfileEvidenceLedgerSubScreen(
    items: List<ProfileEvidenceLedgerEntity>,
    onClose: () -> Unit,
    onAddItem: (ProfileEvidenceLedgerEntity) -> Unit,
    onDeleteItem: (Int) -> Unit
) {
    BackHandler { onClose() }

    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_evidence_screen"),
        contentPadding = PaddingValues(20.dp),
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
                        text = "PROFILE EVIDENCE LEDGER",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tangible proof of competence — distinct from mere plans.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Record New Evidence Item")
            }
        }

        items(items, key = { it.id }) { entry ->
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
                            color = if (entry.isExternallyVerified) GentleSuccessGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = if (entry.isExternallyVerified) "VERIFIED" else entry.status,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (entry.isExternallyVerified) GentleSuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(entry.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onDeleteItem(entry.id) }, modifier = Modifier.minimumInteractiveComponentSize()) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.outline)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Proof: ${entry.evidenceLinkOrFile}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("What was demonstrated: ${entry.whatWasLearnedAndDemonstrated}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }

    if (showAddDialog) {
        AddProfileEvidenceDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                onAddItem(it)
                showAddDialog = false
            }
        )
    }
}

// =========================================================================
// 7. ANGER & OVERWHELM PAUSE SUB-SCREEN (DE-ESCALATION PROTOCOL)
// =========================================================================
@Composable
fun AngerOverwhelmPauseSubScreen(
    pauses: List<AngerOverwhelmPauseEntity>,
    onClose: () -> Unit,
    onRecordPause: (trigger: String, impulse: String, reality: String, alt1: String, alt2: String, response: String) -> Unit,
    onDeletePause: (Int) -> Unit
) {
    BackHandler { onClose() }

    var step by rememberSaveable { mutableIntStateOf(1) }
    var trigger by rememberSaveable { mutableStateOf("") }
    var impulse by rememberSaveable { mutableStateOf("") }
    var reality by rememberSaveable { mutableStateOf("") }
    var alt1 by rememberSaveable { mutableStateOf("") }
    var alt2 by rememberSaveable { mutableStateOf("") }
    var response by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("anger_overwhelm_screen"),
        contentPadding = PaddingValues(20.dp),
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
                        text = "PAUSE & OVERWHELM RESET",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Separate the trigger from your identity and choose a dignified response.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 4-STEP REFLECTIVE PROTOCOL WIDGET
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Grounded De-Escalation (Step $step of 4)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    when (step) {
                        1 -> {
                            Text("1. What just happened that triggered this anger or overwhelm?", style = MaterialTheme.typography.bodySmall)
                            OutlinedTextField(
                                value = trigger,
                                onValueChange = { trigger = it },
                                label = { Text("The Triggering Event") },
                                placeholder = { Text("e.g. Someone criticized my prototype unfairly or principal set a high target.") },
                                minLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = impulse,
                                onValueChange = { impulse = it },
                                label = { Text("Initial Emotional Impulse") },
                                placeholder = { Text("e.g. Feeling defensive, angry, or wanting to snap.") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        2 -> {
                            Text("2. The Factual Reality Check: What are the plain facts without dramatization?", style = MaterialTheme.typography.bodySmall)
                            OutlinedTextField(
                                value = reality,
                                onValueChange = { reality = it },
                                label = { Text("Factual Reality") },
                                placeholder = { Text("e.g. They expressed their opinion. It does not erase my competence.") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        3 -> {
                            Text("3. Two Alternative Interpretations: Why might this person have said this, or what is a constructive angle?", style = MaterialTheme.typography.bodySmall)
                            OutlinedTextField(
                                value = alt1,
                                onValueChange = { alt1 = it },
                                label = { Text("Alternative Interpretation 1") },
                                placeholder = { Text("e.g. They are under stress themselves or want high standards.") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = alt2,
                                onValueChange = { alt2 = it },
                                label = { Text("Alternative Interpretation 2") },
                                placeholder = { Text("e.g. This is free feedback on where my explanation can be clearer.") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        4 -> {
                            Text("4. Chosen Constructive Response: How will you respond with self-respect and poise?", style = MaterialTheme.typography.bodySmall)
                            OutlinedTextField(
                                value = response,
                                onValueChange = { response = it },
                                label = { Text("My Dignified Response") },
                                placeholder = { Text("e.g. Take 3 deep breaths, thank them for the input, and log the test data.") },
                                minLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (step > 1) {
                            TextButton(onClick = { step-- }) { Text("Back") }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        if (step < 4) {
                            Button(onClick = { step++ }) { Text("Next") }
                        } else {
                            Button(
                                onClick = {
                                    val safeTrig = trigger.ifBlank { "Unspecified pressure / challenge" }
                                    val safeImp = impulse.ifBlank { "Initial friction or overwhelm" }
                                    val safeReal = reality.ifBlank { "Grounded in my own core dignity." }
                                    val safeA1 = alt1.ifBlank { "External expectations reflect high hopes." }
                                    val safeA2 = alt2.ifBlank { "Focus on controllable preparation." }
                                    val safeResp = response.ifBlank { "Deep breath, calm focus, steady action." }
                                    onRecordPause(safeTrig, safeImp, safeReal, safeA1, safeA2, safeResp)
                                    step = 1
                                    trigger = ""
                                    impulse = ""
                                    reality = ""
                                    alt1 = ""
                                    alt2 = ""
                                    response = ""
                                }
                            ) {
                                Text("Save Grounded Stance")
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "PAST GROUNDED PAUSES",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(pauses, key = { it.id }) { item ->
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
                        Text(item.dateString, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onDeletePause(item.id) }, modifier = Modifier.minimumInteractiveComponentSize()) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.outline)
                        }
                    }
                    Text("Trigger: \"${item.triggerEvent}\"", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Reality: ${item.factualRealityCheck}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Chosen Response: ${item.chosenConstructiveResponse}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// Dialog helper for Academic Topic
@Composable
fun AddAcademicTopicDialog(
    defaultSubject: String,
    isChemistryMissionDefault: Boolean,
    onDismiss: () -> Unit,
    onSave: (AcademicTopicEntity) -> Unit
) {
    var subject by rememberSaveable { mutableStateOf(defaultSubject) }
    var chapterName by rememberSaveable { mutableStateOf("") }
    var topicName by rememberSaveable { mutableStateOf("") }
    var definitionsNotes by rememberSaveable { mutableStateOf("") }
    var calculationsNotes by rememberSaveable { mutableStateOf("") }
    var mistakeNotes by rememberSaveable { mutableStateOf("") }
    var teacherGuidance by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Academic Topic", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = chapterName,
                    onValueChange = { chapterName = it },
                    label = { Text("Chapter Name") },
                    placeholder = { Text("e.g. Thermodynamics / Rotational Motion") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = topicName,
                    onValueChange = { topicName = it },
                    label = { Text("Exact Topic Name") },
                    placeholder = { Text("e.g. Hess's Law & Enthalpy of Reaction") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = definitionsNotes,
                    onValueChange = { definitionsNotes = it },
                    label = { Text("Core Laws, Definitions or Equations") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = calculationsNotes,
                    onValueChange = { calculationsNotes = it },
                    label = { Text("Numericals & Calculation Method") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = mistakeNotes,
                    onValueChange = { mistakeNotes = it },
                    label = { Text("Known Mistake Traps") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = teacherGuidance, onCheckedChange = { teacherGuidance = it })
                    Text("Requires Teacher / Coaching Guidance", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val safeSub = subject.ifBlank { "Physics" }
                    val safeCh = chapterName.ifBlank { "General Chapter" }
                    val safeTop = topicName.ifBlank { "Key Concept" }
                    onSave(
                        AcademicTopicEntity(
                            subject = safeSub,
                            chapterName = safeCh,
                            topicName = safeTop,
                            definitionsAndReactionsNotes = definitionsNotes,
                            calculationsAndNumericalsNotes = calculationsNotes,
                            mistakePatterns = mistakeNotes,
                            requiresTeacherGuidance = teacherGuidance,
                            isChemistryFullMarksFocus = isChemistryMissionDefault || safeSub.startsWith("Chemistry")
                        )
                    )
                }
            ) {
                Text("Add Topic")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Dialog helper for Exhibition Candidate
@Composable
fun AddExhibitionIdeaDialog(
    onDismiss: () -> Unit,
    onSave: (ScienceExhibitionScorecardEntity) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var differentiation by rememberSaveable { mutableStateOf("") }
    var existingComparison by rememberSaveable { mutableStateOf("") }
    var weaknesses by rememberSaveable { mutableStateOf("") }
    var unknownInfo by rememberSaveable { mutableStateOf("") }
    var budget by rememberSaveable { mutableStateOf("1500") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Score Science Exhibition Candidate", fontWeight = FontWeight.Bold) },
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
                    label = { Text("Project / Idea Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = existingComparison,
                    onValueChange = { existingComparison = it },
                    label = { Text("Existing Solutions Comparison") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = differentiation,
                    onValueChange = { differentiation = it },
                    label = { Text("What is Genuinely Different?") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unknownInfo,
                    onValueChange = { unknownInfo = it },
                    label = { Text("Unknown / Needs Verification") },
                    placeholder = { Text("Mark unknown facts honestly") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = weaknesses,
                    onValueChange = { weaknesses = it },
                    label = { Text("Potential Weaknesses & Judge Questions") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Estimated Budget (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        ScienceExhibitionScorecardEntity(
                            ideaTitle = title.ifBlank { "Untitled Project Candidate" },
                            existingSolutionsComparison = existingComparison,
                            genuineDifferentiation = differentiation,
                            unknownInformation = unknownInfo,
                            potentialWeaknessesAndQuestions = weaknesses,
                            budgetRequiredInr = budget.toDoubleOrNull() ?: 1500.0
                        )
                    )
                }
            ) {
                Text("Save Scorecard")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Dialog helper for Scholarship
@Composable
fun AddScholarshipDialog(
    onDismiss: () -> Unit,
    onSave: (ScholarshipStrategyEntity) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var coverage by rememberSaveable { mutableStateOf("Full Tuition + Stipend") }
    var isFullRide by rememberSaveable { mutableStateOf(true) }
    var source by rememberSaveable { mutableStateOf("") }
    var deadline by rememberSaveable { mutableStateOf("") }
    var appYear by rememberSaveable { mutableStateOf("2027") }
    var entryYear by rememberSaveable { mutableStateOf("2028") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Verified Opportunity", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Scholarship / University Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = coverage, onValueChange = { coverage = it }, label = { Text("Funding Coverage Details") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = source, onValueChange = { source = it }, label = { Text("Official Source URL") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Application Deadline") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = appYear, onValueChange = { appYear = it }, label = { Text("Application Year") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = entryYear, onValueChange = { entryYear = it }, label = { Text("University Entry Year") }, modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        ScholarshipStrategyEntity(
                            name = name.ifBlank { "International Scholarship Opportunity" },
                            country = country.ifBlank { "International" },
                            degreeSubjectEligibility = "STEM / Undergraduate",
                            nationalityRequirements = "Indian Citizen / Open International",
                            academicRequirements = "High Academic Standing (85%+)",
                            ageAndCompletionRequirements = "17-21 Years; 12 years schooling",
                            languageRequirements = "English proficiency / Relevant language",
                            requiredExaminations = "Document review, Subject exam / SAT / EJU",
                            fundingCoverage = coverage,
                            isFullyFunded = isFullRide,
                            officialSourceLink = source,
                            applicationDeadline = deadline,
                            applicationYear = appYear,
                            universityEntryYear = entryYear
                        )
                    )
                }
            ) {
                Text("Save Opportunity")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Dialog helper for Profile Evidence
@Composable
fun AddProfileEvidenceDialog(
    onDismiss: () -> Unit,
    onSave: (ProfileEvidenceLedgerEntity) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("PROTOTYPE") }
    var proof by rememberSaveable { mutableStateOf("") }
    var demonstrated by rememberSaveable { mutableStateOf("") }
    var isVerified by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Profile Evidence", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Item Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = proof, onValueChange = { proof = it }, label = { Text("Link / Document / File Proof") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = demonstrated, onValueChange = { demonstrated = it }, label = { Text("What Competence Was Demonstrated?") }, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isVerified, onCheckedChange = { isVerified = it })
                    Text("Externally Tested / Verified", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        ProfileEvidenceLedgerEntity(
                            title = title.ifBlank { "Untitled Evidence Item" },
                            category = category,
                            evidenceLinkOrFile = proof,
                            whatWasLearnedAndDemonstrated = demonstrated,
                            isExternallyVerified = isVerified,
                            status = if (isVerified) "EXTERNALLY_VERIFIED" else "COMPLETED"
                        )
                    )
                }
            ) {
                Text("Save to Ledger")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
