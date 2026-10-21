package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.DefaultWellnessData
import com.example.data.ProjectEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuildScreen(
    projects: List<ProjectEntity>,
    preferences: UserPreferencesEntity,
    onUpdateProject: (ProjectEntity) -> Unit,
    onAddProject: (
        name: String,
        tagline: String,
        problemStatement: String,
        targetUser: String,
        solutionSummary: String,
        technologyStack: String,
        nextConcreteAction: String
    ) -> Unit,
    onStartProjectFocus: (ProjectEntity) -> Unit,
    onUpdateNcscSettings: (eventName: String, targetDate: String, projectTitle: String, checklistCsv: String) -> Unit,
    onOpenScienceExhibition: () -> Unit = {},
    onOpenProfileEvidence: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val subTabs = listOf(
        "MY PROJECTS",
        "CURRENT BUILD",
        "EVIDENCE",
        "NCSC MODE",
        "PITCH & PORTFOLIO"
    )
    var selectedSubTab by rememberSaveable { mutableStateOf("MY PROJECTS") }
    var showAddProjectDialog by rememberSaveable { mutableStateOf(false) }
    var editingProjectId by rememberSaveable { mutableStateOf<Int?>(null) }

    val pipelineStages = listOf("IDEA", "VALIDATE", "MVP", "TEST", "ITERATE", "EVIDENCE", "PITCH", "IMPACT")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("build_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HEADER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BUILD",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Projects, startups, NCSC research & real evidence",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { showAddProjectDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("add_project_button")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Project")
                }
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
                subTabs.forEach { tab ->
                    val isSel = selectedSubTab == tab
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSubTab = tab }
                            .testTag("build_tab_${tab.lowercase().replace(" ", "_")}")
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                        )
                    }
                }
            }
        }

        // PIPELINE OVERVIEW BAR
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PIPELINE:  IDEA → VALIDATE → MVP → TEST → ITERATE → EVIDENCE → PITCH → IMPACT",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }

        // SCIENCE EXHIBITION & PROFILE EVIDENCE ENGINES BANNER
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
                        .clickable { onOpenScienceExhibition() }
                        .testTag("banner_science_exhibition")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "🔬 EXHIBITION ENGINE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Scorecard, feasibility & 10 milestones (Krow & EcoDrain)",
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
                        .clickable { onOpenProfileEvidence() }
                        .testTag("banner_profile_evidence")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "📋 EVIDENCE LEDGER",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Prototypes, code & externally verified proof",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        when (selectedSubTab) {
            "NCSC MODE" -> {
                item {
                    NcscModeSection(
                        preferences = preferences,
                        onUpdateNcscSettings = onUpdateNcscSettings,
                        onStartPitchFocus = { missionText ->
                            val ecoProj = projects.find { it.name.contains("EcoDrain", ignoreCase = true) }
                                ?: projects.firstOrNull()
                            if (ecoProj != null) {
                                onStartProjectFocus(ecoProj.copy(nextConcreteAction = missionText))
                            }
                        }
                    )
                }
            }

            "EVIDENCE" -> {
                item {
                    SectionHeader(
                        title = "PROJECT EVIDENCE ENGINE",
                        subtitle = "Every achievement is based strictly on entered evidence — no fake numbers"
                    )
                }
                items(projects, key = { it.id }) { project ->
                    ProjectEvidenceCard(
                        project = project,
                        onUpdateProject = onUpdateProject
                    )
                }
            }

            "PITCH & PORTFOLIO" -> {
                item {
                    SectionHeader(
                        title = "PITCH & PORTFOLIO READINESS",
                        subtitle = "Clear problem, solution, role, and verifiable links"
                    )
                }
                items(projects, key = { it.id }) { project ->
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
                                Text(project.name, style = MaterialTheme.typography.titleLarge)
                                StatusBadgeChip(label = "Evidence: ${project.evidenceStrengthLabel}")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("90-Second Pitch Structure:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1. Problem: ${project.problemStatement}\n" +
                                    "2. Solution & Tech: ${project.solutionSummary} (${project.technologyStack})\n" +
                                    "3. Validation & Testing: ${project.testingSummary}\n" +
                                    "4. Next Milestone: ${project.nextConcreteAction}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            else -> {
                if (projects.isEmpty()) {
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
                                Text("No projects yet.", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "Your next idea could start here.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                PrimaryButton(
                                    text = "ADD PROJECT",
                                    onClick = { showAddProjectDialog = true }
                                )
                            }
                        }
                    }
                } else {
                    items(projects, key = { it.id }) { project ->
                        ProjectDetailCard(
                            project = project,
                            pipelineStages = pipelineStages,
                            onCycleStage = {
                                val nextIdx = (pipelineStages.indexOf(project.pipelineStage) + 1) % pipelineStages.size
                                onUpdateProject(project.copy(pipelineStage = pipelineStages[nextIdx]))
                            },
                            onStartNextAction = { onStartProjectFocus(project) },
                            onEditProject = { editingProjectId = project.id }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Add Project Dialog
    if (showAddProjectDialog) {
        var name by rememberSaveable { mutableStateOf("") }
        var tagline by rememberSaveable { mutableStateOf("") }
        var problem by rememberSaveable { mutableStateOf("") }
        var targetUser by rememberSaveable { mutableStateOf("") }
        var solution by rememberSaveable { mutableStateOf("") }
        var tech by rememberSaveable { mutableStateOf("") }
        var nextAction by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddProjectDialog = false },
            title = { Text("Add Project to BUILD") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Project Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = problem,
                        onValueChange = { problem = it },
                        label = { Text("Problem Statement") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetUser,
                        onValueChange = { targetUser = it },
                        label = { Text("Target User") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = solution,
                        onValueChange = { solution = it },
                        label = { Text("Solution / Prototype") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tech,
                        onValueChange = { tech = it },
                        label = { Text("Technology Stack") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nextAction,
                        onValueChange = { nextAction = it },
                        label = { Text("Concrete Next Action") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (name.isNotBlank()) {
                            onAddProject(name, tagline, problem, targetUser, solution, tech, nextAction)
                            showAddProjectDialog = false
                        }
                    }
                ) {
                    Text("Save Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProjectDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Project Next Action / Feedback Dialog
    if (editingProjectId != null) {
        val proj = projects.find { it.id == editingProjectId }
        if (proj != null) {
            var nextAct by rememberSaveable { mutableStateOf(proj.nextConcreteAction) }
            var feedback by rememberSaveable { mutableStateOf(proj.userFeedbackSummary) }
            var testing by rememberSaveable { mutableStateOf(proj.testingSummary) }
            var impact by rememberSaveable { mutableStateOf(proj.impactSummary) }

            AlertDialog(
                onDismissRequest = { editingProjectId = null },
                title = { Text("Update ${proj.name}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = nextAct,
                            onValueChange = { nextAct = it },
                            label = { Text("Next Concrete Action") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = feedback,
                            onValueChange = { feedback = it },
                            label = { Text("User Feedback Logged") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = testing,
                            onValueChange = { testing = it },
                            label = { Text("Testing Notes") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = impact,
                            onValueChange = { impact = it },
                            label = { Text("Real Documented Impact") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUpdateProject(
                                proj.copy(
                                    nextConcreteAction = nextAct,
                                    userFeedbackSummary = feedback,
                                    testingSummary = testing,
                                    impactSummary = impact
                                )
                            )
                            editingProjectId = null
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingProjectId = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProjectDetailCard(
    project: ProjectEntity,
    pipelineStages: List<String>,
    onCycleStage: () -> Unit,
    onStartNextAction: () -> Unit,
    onEditProject: () -> Unit
) {
    val strengthBg = when (project.evidenceStrengthLabel) {
        "STRONG" -> GentleSuccessLight
        "BUILDING" -> WarmPeachLight
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val strengthFg = when (project.evidenceStrengthLabel) {
        "STRONG" -> GentleSuccessGreen
        "BUILDING" -> WarmPeachHighlight
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_card_${project.name.lowercase().replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onCycleStage() }
                ) {
                    Text(
                        text = "STAGE: ${project.pipelineStage} ↻",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                StatusBadgeChip(
                    label = "Evidence: ${project.evidenceStrengthLabel} (${project.evidenceCheckedCount}/9)",
                    containerColor = strengthBg,
                    contentColor = strengthFg
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = project.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = project.tagline,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Concrete Next Action Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "NEXT CONCRETE ACTION",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = project.nextConcreteAction,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "• Problem: ${project.problemStatement}\n" +
                    "• Target User: ${project.targetUser}\n" +
                    "• Tech & Role: ${project.technologyStack} (${project.myRole})\n" +
                    "• Competition: ${project.competitionTarget}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onStartNextAction,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1.4f)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Start Next Action")
                }

                OutlinedButton(
                    onClick = onEditProject,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Update")
                }
            }
        }
    }
}

@Composable
private fun ProjectEvidenceCard(
    project: ProjectEntity,
    onUpdateProject: (ProjectEntity) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                StatusBadgeChip(
                    label = "Evidence strength: ${project.evidenceStrengthLabel}",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val evidenceItems = listOf(
                Triple("Problem documented", project.evProblemDocumented) { v: Boolean -> project.copy(evProblemDocumented = v) },
                Triple("Prototype exists", project.evPrototypeExists) { v: Boolean -> project.copy(evPrototypeExists = v) },
                Triple("Demo available", project.evDemoAvailable) { v: Boolean -> project.copy(evDemoAvailable = v) },
                Triple("GitHub available", project.evGithubAvailable) { v: Boolean -> project.copy(evGithubAvailable = v) },
                Triple("User feedback", project.evUserFeedback) { v: Boolean -> project.copy(evUserFeedback = v) },
                Triple("Testing evidence", project.evTestingEvidence) { v: Boolean -> project.copy(evTestingEvidence = v) },
                Triple("Presentation", project.evPresentation) { v: Boolean -> project.copy(evPresentation = v) },
                Triple("Impact evidence", project.evImpactEvidence) { v: Boolean -> project.copy(evImpactEvidence = v) },
                Triple("Competition submission", project.evCompetitionSubmission) { v: Boolean -> project.copy(evCompetitionSubmission = v) }
            )

            evidenceItems.forEach { (label, isChecked, updater) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onUpdateProject(updater(!isChecked)) }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onUpdateProject(updater(it)) }
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun NcscModeSection(
    preferences: UserPreferencesEntity,
    onUpdateNcscSettings: (String, String, String, String) -> Unit,
    onStartPitchFocus: (String) -> Unit
) {
    val checkedSet = preferences.ncscChecklistCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "NCSC COMPETITION PREPARATION MODE",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = preferences.ncscProjectTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${preferences.ncscEventName}  •  Target Date: ${preferences.ncscTargetDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Readiness: ${checkedSet.size} of ${DefaultWellnessData.ncscPreparationItems.size} areas prepared",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        SectionHeader(
            title = "DAILY PREPARATION MISSIONS",
            subtitle = "Tap any speaking/pitch mission to launch a 10-minute practice timer"
        )

        DefaultWellnessData.ncscDailyMissions.forEach { mission ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onStartPitchFocus(mission) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "🎙️ $mission",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    FilledTonalButton(
                        onClick = { onStartPitchFocus(mission) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Practice", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        SectionHeader(
            title = "NCSC 12-POINT PREPARATION CHECKLIST",
            subtitle = "Track your project report, model, and speaking readiness"
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DefaultWellnessData.ncscPreparationItems.forEach { item ->
                    val isChecked = item in checkedSet
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val nextSet = if (isChecked) checkedSet - item else checkedSet + item
                                onUpdateNcscSettings(
                                    preferences.ncscEventName,
                                    preferences.ncscTargetDate,
                                    preferences.ncscProjectTitle,
                                    nextSet.joinToString(",")
                                )
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                val nextSet = if (checked) checkedSet + item else checkedSet - item
                                onUpdateNcscSettings(
                                    preferences.ncscEventName,
                                    preferences.ncscTargetDate,
                                    preferences.ncscProjectTitle,
                                    nextSet.joinToString(",")
                                )
                            }
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
