package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayIconBadge
import com.example.ui.components.ClayPill
import com.example.ui.components.ClayPillButton
import com.example.ui.theme.ClayCoralAction
import com.example.ui.theme.ClayDeepPurple
import com.example.ui.theme.ClayPaleLavenderSurface
import com.example.ui.theme.ClayPeach
import com.example.ui.theme.ClayPrimaryLavender
import com.example.ui.theme.ClayPrimaryText
import com.example.ui.theme.ClaySecondaryText
import com.example.ui.theme.ClaySkyBlue
import com.example.ui.theme.ClaySoftMint
import com.example.ui.theme.ClayWarmIvory
import com.example.ui.theme.GentleSuccessGreen

val StandardProjectStages = listOf("Research", "Prototype", "Test", "Improve", "Present")

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
    onOpenStartupIncubator: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddProjectDialog by rememberSaveable { mutableStateOf(false) }

    // Seed default standout projects if empty
    val displayProjects = if (projects.isEmpty()) {
        listOf(
            ProjectEntity(
                id = 1,
                name = "Krow",
                tagline = "Personal Growth & Edge AI Operating System",
                pipelineStage = "Prototype",
                problemStatement = "High school students face context fragmentation between Class 11 PCM, coding, and real wellbeing.",
                targetUser = "Self & high-performing STEM students",
                solutionSummary = "Tactile clay-styled daily companion that minimizes confusion and executes single concrete moves.",
                prototypeDetails = "Kotlin & Jetpack Compose Android client with offline Room persistence.",
                technologyStack = "Kotlin, Jetpack Compose, Edge ML, Local-first Room DB",
                myRole = "Sole Creator & Lead Architect",
                testingSummary = "Tested in daily revision cycles with 0 frame drops.",
                userFeedbackSummary = "Immediate positive clarity on single focus moves.",
                impactSummary = "Reduces morning decision paralysis to under 30 seconds.",
                githubOrDemoUrl = "https://github.com/astha/krow",
                competitionTarget = "Youth STEM Innovation & Hackathons",
                status = "ACTIVE",
                nextConcreteAction = "Refine tactile clay animations and test offline sync resilience."
            ),
            ProjectEntity(
                id = 2,
                name = "Smart EcoDrain Shield",
                tagline = "Automated Urban Stormwater & Micro-plastic Filter",
                pipelineStage = "Test",
                problemStatement = "Urban drains choke during monsoon rains with solid waste, flooding roads and polluting water tables.",
                targetUser = "Municipal drainage systems & community roads",
                solutionSummary = "Multi-stage mechanical sieve with ultrasonic sensor alerting when blockage crosses 75%.",
                prototypeDetails = "1:5 physical scale acrylic drainage model with ultrasonic sensor & servo latch.",
                technologyStack = "Arduino / ESP32, Ultrasonic Sensors, Mechanical Lever, Recycled Acrylic",
                myRole = "Hardware Designer & Researcher",
                testingSummary = "Demonstrated 85% solid plastic retention in simulated flow test.",
                userFeedbackSummary = "Commended by science department mentors for low installation cost.",
                impactSummary = "Prevents road waterlogging and stops microplastics from entering municipal water bodies.",
                githubOrDemoUrl = "",
                competitionTarget = "NCSC National Children's Science Congress",
                status = "ACTIVE",
                nextConcreteAction = "Log flow rate vs blockage trigger observation in Project Evidence Ledger."
            )
        )
    } else {
        projects
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ClayWarmIvory)
            .testTag("build_screen_list"),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // -------------------------------------------------------------
        // 1. HEADER
        // -------------------------------------------------------------
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Project Workspace",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Build, test, and document meaningful engineering.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ClaySecondaryText,
                        fontWeight = FontWeight.Medium
                    )
                }
                IconButton(
                    onClick = { showAddProjectDialog = true },
                    modifier = Modifier.size(42.dp)
                ) {
                    ClayIconBadge(
                        icon = Icons.Default.Add,
                        containerColor = ClayPaleLavenderSurface,
                        iconColor = ClayDeepPurple,
                        size = 40.dp,
                        iconSize = 22.dp
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 2. HIGH-GROWTH STARTUP STUDIO HERO CARD
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("startup_studio_banner_card"),
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
                        ClayPill(
                            label = "HIGH-GROWTH VENTURE",
                            isSelected = true,
                            onClick = {},
                            selectedContainerColor = ClayCoralAction.copy(alpha = 0.15f),
                            selectedContentColor = ClayCoralAction
                        )
                        ClayPill(
                            label = "NCSC Graduated 🎓",
                            isSelected = true,
                            onClick = {},
                            selectedContainerColor = GentleSuccessGreen.copy(alpha = 0.15f),
                            selectedContentColor = GentleSuccessGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🚀 New Startup Idea Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NCSC is successfully complete! Shift energy into building a working, high-value app that solves real pain points, with MVP sprints and AI Co-Founder.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ClaySecondaryText,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ClayPillButton(
                        text = "Open Startup Incubator",
                        icon = Icons.Default.Lightbulb,
                        containerColor = ClayCoralAction,
                        contentColor = Color.White,
                        onClick = onOpenStartupIncubator,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_startup_incubator_button")
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 3. QUICK SHORTCUTS (STARTUP, NCSC COMPLETED & EVIDENCE)
        // -------------------------------------------------------------
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    onClick = onOpenStartupIncubator,
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 2.5.dp,
                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.Lightbulb,
                            containerColor = ClayCoralAction.copy(alpha = 0.15f),
                            iconColor = ClayCoralAction,
                            size = 36.dp,
                            iconSize = 18.dp
                        )
                        Column {
                            Text(
                                text = "Startup Studio",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Text(
                                text = "Working App MVP",
                                style = MaterialTheme.typography.labelSmall,
                                color = ClaySecondaryText
                            )
                        }
                    }
                }

                Surface(
                    onClick = onOpenScienceExhibition,
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 2.5.dp,
                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.Science,
                            containerColor = ClaySoftMint,
                            iconColor = Color(0xFF285E3D),
                            size = 36.dp,
                            iconSize = 18.dp
                        )
                        Column {
                            Text(
                                text = "NCSC (Archived)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Text(
                                text = "Completed 🏆",
                                style = MaterialTheme.typography.labelSmall,
                                color = GentleSuccessGreen
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 3. PROJECT CARDS
        // -------------------------------------------------------------
        items(displayProjects) { project ->
            val icon = if (project.name.contains("EcoDrain", ignoreCase = true)) {
                Icons.Default.Build
            } else {
                Icons.Default.AutoAwesome
            }
            val badgeColor = if (project.name.contains("EcoDrain", ignoreCase = true)) {
                ClaySoftMint
            } else {
                ClayPaleLavenderSurface
            }
            val badgeIconColor = if (project.name.contains("EcoDrain", ignoreCase = true)) {
                Color(0xFF285E3D)
            } else {
                ClayDeepPurple
            }

            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("project_card_${project.id}"),
                shape = RoundedCornerShape(26.dp),
                containerColor = Color.White,
                elevation = 4.5.dp
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ClayIconBadge(
                                icon = icon,
                                containerColor = badgeColor,
                                iconColor = badgeIconColor,
                                size = 44.dp,
                                iconSize = 24.dp
                            )
                            Column {
                                Text(
                                    text = project.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayPrimaryText
                                )
                                Text(
                                    text = project.tagline,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ClaySecondaryText,
                                    maxLines = 1
                                )
                            }
                        }

                        // Current Stage Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ClaySkyBlue
                        ) {
                            Text(
                                text = project.pipelineStage.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2B5C88),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = project.solutionSummary.ifBlank { project.problemStatement },
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stage Progress Bar (Research -> Prototype -> Test -> Improve -> Present)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StandardProjectStages.forEach { stageName ->
                            val isCurrent = project.pipelineStage.equals(stageName, ignoreCase = true)
                            val isPast = StandardProjectStages.indexOf(stageName) <= StandardProjectStages.indexOfFirst {
                                it.equals(project.pipelineStage, ignoreCase = true)
                            }
                            Surface(
                                onClick = {
                                    onUpdateProject(project.copy(pipelineStage = stageName))
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCurrent) ClayDeepPurple else if (isPast) ClayPaleLavenderSurface else Color(0xFFF5F1EB),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                            ) {}
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Next Concrete Action
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ClayPaleLavenderSurface.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "NEXT CONCRETE ACTION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayDeepPurple
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = project.nextConcreteAction.ifBlank { "Test prototype in real environment." },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = ClayPrimaryText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ClayPillButton(
                        text = "Work on ${project.name}",
                        icon = Icons.Default.PlayArrow,
                        onClick = { onStartProjectFocus(project) },
                        containerColor = ClayCoralAction,
                        height = 46.dp,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "work_on_project_button_${project.id}"
                    )
                }
            }
        }
    }

    // -----------------------------------------------------------------
    // DIALOG: ADD PROJECT
    // -----------------------------------------------------------------
    if (showAddProjectDialog) {
        var nameInput by rememberSaveable { mutableStateOf("") }
        var taglineInput by rememberSaveable { mutableStateOf("") }
        var summaryInput by rememberSaveable { mutableStateOf("") }
        var actionInput by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddProjectDialog = false },
            title = {
                Text(
                    text = "New Engineering Project",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Project Name") },
                        placeholder = { Text("e.g. Smart EcoDrain") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = taglineInput,
                        onValueChange = { taglineInput = it },
                        label = { Text("Tagline / Problem") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = summaryInput,
                        onValueChange = { summaryInput = it },
                        label = { Text("Solution Summary") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = actionInput,
                        onValueChange = { actionInput = it },
                        label = { Text("First Actionable Step") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                ClayButton(
                    text = "Create Project",
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            onAddProject(
                                nameInput.trim(),
                                taglineInput.trim(),
                                taglineInput.trim(),
                                "General",
                                summaryInput.trim(),
                                "Hardware / Software",
                                actionInput.trim().ifBlank { "Define problem specification" }
                            )
                            showAddProjectDialog = false
                        }
                    },
                    enabled = nameInput.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showAddProjectDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(26.dp),
            containerColor = Color.White
        )
    }
}
