package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectEntity
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayIconBadge
import com.example.ui.components.ClayPill
import com.example.ui.components.ClayPillButton
import com.example.ui.theme.ClayButter
import com.example.ui.theme.ClayCoralAction
import com.example.ui.theme.ClayCoralLight
import com.example.ui.theme.ClayDeepPurple
import com.example.ui.theme.ClayLavenderCard
import com.example.ui.theme.ClayMintCard
import com.example.ui.theme.ClayPaleLavenderSurface
import com.example.ui.theme.ClayPeach
import com.example.ui.theme.ClayPeachCard
import com.example.ui.theme.ClayPrimaryText
import com.example.ui.theme.ClaySecondaryText
import com.example.ui.theme.ClaySoftMint
import com.example.ui.theme.ClayWarmIvory
import com.example.ui.theme.GentleSuccessGreen

@Composable
fun StartupIdeaIncubatorSubScreen(
    project: ProjectEntity?,
    onClose: () -> Unit,
    onSaveProject: (ProjectEntity) -> Unit,
    onStartFocusSession: (title: String, durationMins: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var appName by rememberSaveable {
        mutableStateOf(project?.name?.ifBlank { "EduFlow AI OS" } ?: "EduFlow AI OS")
    }
    var appTagline by rememberSaveable {
        mutableStateOf(project?.tagline?.ifBlank { "Personal Growth & Edge AI Operating System" } ?: "Personal Growth & Edge AI Operating System")
    }
    var problemStatement by rememberSaveable {
        mutableStateOf(project?.problemStatement?.ifBlank {
            "STEM students suffer from fragmentation across scattered notes, endless YouTube lectures, and decision fatigue on what to do right now."
        } ?: "STEM students suffer from fragmentation across scattered notes, endless YouTube lectures, and decision fatigue on what to do right now.")
    }
    var valueProposition by rememberSaveable {
        mutableStateOf(project?.solutionSummary?.ifBlank {
            "A tactile clay-styled life operating system that unifies morning focus, multi-subject chapter vaults, and intelligent active recall that outperforms Anki with zero guilt."
        } ?: "A tactile clay-styled life operating system that unifies morning focus, multi-subject chapter vaults, and intelligent active recall that outperforms Anki with zero guilt.")
    }
    var targetUsers by rememberSaveable {
        mutableStateOf(project?.targetUser?.ifBlank { "High school & college STEM students aiming for top exams while building real projects" } ?: "High school & college STEM students aiming for top exams while building real projects")
    }
    var techStack by rememberSaveable {
        mutableStateOf(project?.technologyStack?.ifBlank { "Kotlin, Jetpack Compose, Room local-first DB, Offline AI inference & Gemini API" } ?: "Kotlin, Jetpack Compose, Room local-first DB, Offline AI inference & Gemini API")
    }
    var nextAction by rememberSaveable {
        mutableStateOf(project?.nextConcreteAction?.ifBlank { "Build active recall flashcard spaced repetition and test with 3 student peers" } ?: "Build active recall flashcard spaced repetition and test with 3 student peers")
    }

    var showEditDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ClayWarmIvory)
            .testTag("startup_incubator_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // -------------------------------------------------------------
        // 1. TOP NAVIGATION
        // -------------------------------------------------------------
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(42.dp)
                ) {
                    ClayIconBadge(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        containerColor = Color.White,
                        iconColor = ClayPrimaryText,
                        size = 38.dp,
                        iconSize = 20.dp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = ClayCoralLight,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = ClayCoralAction,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "STARTUP STUDIO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = ClayCoralAction
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 2. HERO STARTUP CARD
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                containerColor = ClayLavenderCard,
                borderColor = Color.White,
                elevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White.copy(alpha = 0.9f),
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                text = "HIGH-GROWTH WORKING APP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = ClayDeepPurple,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        IconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Startup Idea",
                                tint = ClayCoralAction
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = appName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = appTagline,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ClaySecondaryText,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Growth Potential Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Viral Utility",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ClaySecondaryText
                                )
                                Text(
                                    text = "Daily Habit",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayCoralAction
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Retention",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ClaySecondaryText
                                )
                                Text(
                                    text = "High Daily",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayDeepPurple
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tech Edge",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ClaySecondaryText
                                )
                                Text(
                                    text = "AI + Local DB",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF285E3D)
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 3. CORE VALUE PROPOSITION
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Why This App Can Become Really Valuable",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = valueProposition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 4. USER PAIN POINT & TARGET AUDIENCE
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "The Pain Point It Solves",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = problemStatement,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Target Users: $targetUsers",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = ClayDeepPurple
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 5. 5-STEP MVP LAUNCH SPRINT CHECKLIST
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "MVP Development & Launch Sprints",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    listOf(
                        "1. Morning Launch & Zero-Decision Flow" to true,
                        "2. Multi-Subject Chapter Vaults (Never Forget Material)" to true,
                        "3. Anki-Superior Active Recall & Spaced Repetition" to true,
                        "4. AI Assistant Revision Drills & Exam Traps" to true,
                        "5. Peer Testing & Play Store Production APK" to false
                    ).forEach { (milestone, isDone) ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isDone) ClaySoftMint.copy(alpha = 0.5f) else ClayPaleLavenderSurface.copy(alpha = 0.4f),
                            border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (isDone) GentleSuccessGreen else ClayCoralAction,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = milestone,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isDone) FontWeight.Bold else FontWeight.Medium,
                                    color = ClayPrimaryText
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 6. IMMEDIATE NEXT SPRINT MOVE (CORAL BUTTON)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                containerColor = ClayPeachCard,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Immediate Builder Move",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9A4518)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = nextAction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ClayPillButton(
                        text = "Start 45m Builder Focus Session",
                        icon = Icons.Default.PlayArrow,
                        onClick = {
                            onStartFocusSession("Build $appName: $nextAction", 45)
                        },
                        containerColor = ClayCoralAction,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // Edit Startup Details Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Startup Concept", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = appName,
                        onValueChange = { appName = it },
                        label = { Text("App Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = appTagline,
                        onValueChange = { appTagline = it },
                        label = { Text("Tagline / Elevator Pitch") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = valueProposition,
                        onValueChange = { valueProposition = it },
                        label = { Text("Value Proposition & Growth Potential") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nextAction,
                        onValueChange = { nextAction = it },
                        label = { Text("Next Concrete Action") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showEditDialog = false
                    val updatedProject = (project ?: ProjectEntity(
                        name = appName,
                        tagline = appTagline,
                        pipelineStage = "MVP Prototype",
                        problemStatement = problemStatement,
                        targetUser = targetUsers,
                        solutionSummary = valueProposition,
                        prototypeDetails = "Working Android application built with Jetpack Compose & Room",
                        technologyStack = techStack,
                        myRole = "Founder & Architect",
                        testingSummary = "Active daily dogfooding",
                        userFeedbackSummary = "Tested in daily revision cycles",
                        impactSummary = "Reduces decision fatigue to zero",
                        githubOrDemoUrl = "",
                        competitionTarget = "Student Startup Incubator",
                        status = "ACTIVE",
                        nextConcreteAction = nextAction
                    )).copy(
                        name = appName,
                        tagline = appTagline,
                        solutionSummary = valueProposition,
                        nextConcreteAction = nextAction
                    )
                    onSaveProject(updatedProject)
                }) {
                    Text("Save", fontWeight = FontWeight.Bold, color = ClayCoralAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = ClaySecondaryText)
                }
            }
        )
    }
}
