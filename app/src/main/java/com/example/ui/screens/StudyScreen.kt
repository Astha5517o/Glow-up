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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.data.ErrorLogEntity
import com.example.data.StudyChapterEntity
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayIconBadge
import com.example.ui.components.ClayPill
import com.example.ui.components.ClayPillButton
import com.example.ui.components.ClaySearchBar
import com.example.ui.components.ClayTileCard
import com.example.ui.theme.ClayButter
import com.example.ui.theme.ClayButterText
import com.example.ui.theme.ClayCoralAction
import com.example.ui.theme.ClayCoralLight
import com.example.ui.theme.ClayDeepPurple
import com.example.ui.theme.ClayLavenderCard
import com.example.ui.theme.ClayMintCard
import com.example.ui.theme.ClayPaleLavenderSurface
import com.example.ui.theme.ClayPeach
import com.example.ui.theme.ClayPeachCard
import com.example.ui.theme.ClayPrimaryLavender
import com.example.ui.theme.ClayPrimaryText
import com.example.ui.theme.ClayRoseCard
import com.example.ui.theme.ClaySecondaryText
import com.example.ui.theme.ClaySkyBlue
import com.example.ui.theme.ClaySkyBlueCard
import com.example.ui.theme.ClaySoftMint
import com.example.ui.theme.ClayWarmIvory

data class SubjectTileData(
    val name: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color,
    val currentFocus: String,
    val nextStep: String
)

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
    onOpenChapterMaterialFolder: (StudyChapterEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedSubjectFilter by rememberSaveable { mutableStateOf("ALL") }

    val subjects = listOf(
        SubjectTileData(
            name = "Physics",
            icon = Icons.Default.Science,
            containerColor = ClayLavenderCard,
            iconColor = ClayDeepPurple,
            currentFocus = "Rotational Motion",
            nextStep = "Torque equilibrium"
        ),
        SubjectTileData(
            name = "Chemistry",
            icon = Icons.Default.Science,
            containerColor = ClayMintCard,
            iconColor = Color(0xFF285E3D),
            currentFocus = "Thermodynamics",
            nextStep = "Le Chatelier's rule"
        ),
        SubjectTileData(
            name = "Mathematics",
            icon = Icons.Default.Calculate,
            containerColor = ClayPeachCard,
            iconColor = Color(0xFF9A4518),
            currentFocus = "Trigonometric Eqs",
            nextStep = "General solutions"
        ),
        SubjectTileData(
            name = "Computer Science",
            icon = Icons.Default.Computer,
            containerColor = ClaySkyBlueCard,
            iconColor = Color(0xFF2B5C88),
            currentFocus = "Data Structures",
            nextStep = "Recursive stack trace"
        ),
        SubjectTileData(
            name = "Artificial Intelligence",
            icon = Icons.Default.Psychology,
            containerColor = ClayButter,
            iconColor = ClayButterText,
            currentFocus = "Edge Vision",
            nextStep = "Inference latency"
        ),
        SubjectTileData(
            name = "Mission Jeet Practise",
            icon = Icons.Default.Quiz,
            containerColor = ClayRoseCard,
            iconColor = Color(0xFF993B47),
            currentFocus = "Class 11 PYQs",
            nextStep = "Top 10 numericals"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ClayWarmIvory)
            .testTag("study_screen_list"),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // -------------------------------------------------------------
        // 1. HEADER (WORKOUTS STYLE IN SCREENSHOT 2)
        // -------------------------------------------------------------
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Study & Practice",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = ClayPrimaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Class 11 PCM, Computer Science & AI",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ClaySecondaryText,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // -------------------------------------------------------------
        // 1.5. HALF-YEARLY EXAM HUB (16 NOV COUNTDOWN & READINESS)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("half_yearly_exam_hub_card"),
                shape = RoundedCornerShape(26.dp),
                containerColor = ClayLavenderCard,
                borderColor = Color.White,
                elevation = 5.dp
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
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ClayCoralLight,
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                text = "⏳ 21 DAYS TO HALF-YEARLY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = ClayCoralAction,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                text = "Starts 16 Nov",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayDeepPurple,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Half-Yearly Preparation Command",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Balanced mastery across Physics, Chemistry, Math & CS. Zero neglect.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4 Subject Readiness Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple("Physics", "75%", ClaySoftMint),
                            Triple("Chemistry", "80%", ClayMintCard),
                            Triple("Maths", "70%", ClayPeachCard),
                            Triple("CS", "85%", ClaySkyBlueCard)
                        ).forEach { (subName, readyPct, bgColor) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = bgColor,
                                border = BorderStroke(1.dp, Color.White),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = readyPct,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ClayPrimaryText
                                    )
                                    Text(
                                        text = subName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ClaySecondaryText,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 2. RECESSED CLAY SEARCH BAR (SCREENSHOT 2)
        // -------------------------------------------------------------
        item {
            ClaySearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Find a subject, lesson or formula"
            )
        }

        // -------------------------------------------------------------
        // 3. PILL FILTER CHIPS ROW (SCREENSHOT 2)
        // -------------------------------------------------------------
        item {
            val filterOptions = listOf(
                "ALL" to "All",
                "PHYSICS" to "Physics",
                "CHEMISTRY" to "Chemistry",
                "MATH" to "Math",
                "CS" to "CS",
                "AI" to "AI"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { (filterKey, filterLabel) ->
                    val isSelected = selectedSubjectFilter == filterKey
                    ClayPill(
                        label = filterLabel,
                        isSelected = isSelected,
                        onClick = { selectedSubjectFilter = filterKey },
                        selectedContainerColor = ClayCoralAction,
                        selectedContentColor = Color.White,
                        unselectedContainerColor = Color.White,
                        unselectedContentColor = ClaySecondaryText,
                        testTag = "study_filter_${filterKey.lowercase()}"
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 4. 2-COLUMN CHUNKY PASTEL CLAY TILES GRID (SCREENSHOT 2 PARITY)
        // -------------------------------------------------------------
        val filteredSubjects = subjects.filter {
            val matchesFilter = selectedSubjectFilter == "ALL" ||
                it.name.contains(selectedSubjectFilter, ignoreCase = true) ||
                (selectedSubjectFilter == "MATH" && it.name.contains("Math", ignoreCase = true)) ||
                (selectedSubjectFilter == "CS" && it.name.contains("Computer", ignoreCase = true)) ||
                (selectedSubjectFilter == "AI" && it.name.contains("Artificial", ignoreCase = true))
            val matchesSearch = searchQuery.isBlank() ||
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.currentFocus.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }

        val subjectPairs = filteredSubjects.chunked(2)

        items(subjectPairs) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val first = pair[0]
                ClayTileCard(
                    title = first.name,
                    subtitle = first.currentFocus,
                    icon = first.icon,
                    containerColor = first.containerColor,
                    iconColor = first.iconColor,
                    buttonText = "Continue",
                    buttonColor = ClayCoralAction,
                    onButtonClick = {
                        val matchingChapter = chapters.firstOrNull {
                            it.subject.equals(first.name, ignoreCase = true)
                        } ?: StudyChapterEntity(
                            subject = first.name,
                            chapterName = first.currentFocus,
                            currentTopic = first.nextStep,
                            weakAreas = "Core derivations"
                        )
                        onStartStudyMission(matchingChapter)
                    },
                    modifier = Modifier.weight(1f),
                    testTag = "subject_tile_${first.name.lowercase().replace(" ", "_")}"
                )

                if (pair.size > 1) {
                    val second = pair[1]
                    ClayTileCard(
                        title = second.name,
                        subtitle = second.currentFocus,
                        icon = second.icon,
                        containerColor = second.containerColor,
                        iconColor = second.iconColor,
                        buttonText = "Continue",
                        buttonColor = ClayCoralAction,
                        onButtonClick = {
                            val matchingChapter = chapters.firstOrNull {
                                it.subject.equals(second.name, ignoreCase = true)
                            } ?: StudyChapterEntity(
                                subject = second.name,
                                chapterName = second.currentFocus,
                                currentTopic = second.nextStep,
                                weakAreas = "Core derivations"
                            )
                            onStartStudyMission(matchingChapter)
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "subject_tile_${second.name.lowercase().replace(" ", "_")}"
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // -------------------------------------------------------------
        // 5. MISSION JEET: CONTINUE LEARNING (MAIN HIGHLIGHT)
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mission_jeet_continue_card"),
                shape = RoundedCornerShape(28.dp),
                containerColor = Color.White,
                borderColor = Color.White.copy(alpha = 0.95f),
                elevation = 5.dp
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
                            color = ClayPaleLavenderSurface,
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                text = "MISSION JEET • NEXT STEP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayDeepPurple,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = "Track Lectures",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClayDeepPurple,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onOpenMissionJeetTracker() }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Physics: Rotational Dynamics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Next: Active recall of torque equilibrium conditions, then 2 numericals.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaySecondaryText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4-Step Sequence Indicator
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ClayPaleLavenderSurface.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(
                                "1. Learn" to true,
                                "2. Recall" to true,
                                "3. Practise" to false,
                                "4. Review" to false
                            ).forEach { (stepName, isCurrentOrDone) ->
                                Text(
                                    text = stepName,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isCurrentOrDone) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isCurrentOrDone) ClayDeepPurple else ClaySecondaryText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Primary Action: Continue Learning with Coral Action Button
                    ClayPillButton(
                        text = "Continue Mission Jeet",
                        icon = Icons.Default.PlayArrow,
                        onClick = {
                            val activeChapter = chapters.firstOrNull() ?: StudyChapterEntity(
                                subject = "Physics",
                                chapterName = "Rotational Motion",
                                currentTopic = "Torque Equilibrium & Angular Momentum",
                                weakAreas = "Center of Mass derivations"
                            )
                            onStartStudyMission(activeChapter)
                        },
                        containerColor = ClayCoralAction,
                        height = 46.dp,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "continue_learning_button"
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 5.5. SPECIAL CHAPTER MATERIAL FOLDERS (NEVER FORGET VAULTS)
        // -------------------------------------------------------------
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Special Chapter Material Folders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ClayPrimaryText
                    )
                    Text(
                        text = "Never Forget Vaults",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ClayDeepPurple
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Each chapter stores your derivations, formula sheets, exam traps & Anki+ recall cards.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ClaySecondaryText
                )
            }
        }

        items(chapters) { ch ->
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChapterMaterialFolder(ch) }
                    .testTag("chapter_folder_${ch.id}"),
                shape = RoundedCornerShape(22.dp),
                containerColor = Color.White,
                borderColor = Color.White,
                elevation = 3.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = when (ch.subject) {
                                "Physics" -> ClayLavenderCard
                                "Chemistry Physical" -> ClayMintCard
                                "Chemistry Organic/Inorganic" -> ClayRoseCard
                                "Mathematics" -> ClayPeachCard
                                else -> ClayPaleLavenderSurface
                            },
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                text = ch.subject.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = ClayDeepPurple,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ClayCoralLight,
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                text = ch.halfYearlyWeightage.ifBlank { "8-10 Marks" },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayCoralAction,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ClayPeachCard.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = Color(0xFF9A4518),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ch.chapterName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Topic: ${ch.currentTopic}",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClaySecondaryText,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    ClayPillButton(
                        text = "Open Chapter Folder & Anki+ Recall",
                        icon = Icons.Default.Folder,
                        onClick = { onOpenChapterMaterialFolder(ch) },
                        containerColor = ClayCoralAction,
                        height = 40.dp,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "open_folder_btn_${ch.id}"
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 6. ACTION SHORTCUTS (3 QUICK STUDY CARDS)
        // -------------------------------------------------------------
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Action 1: Practise Questions
                Surface(
                    onClick = { onOpenPracticeQuestions("Physics") },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.2.dp, Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.Quiz,
                            containerColor = ClaySoftMint,
                            iconColor = Color(0xFF285E3D),
                            size = 38.dp,
                            iconSize = 20.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Practise Qs",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClayPrimaryText
                        )
                    }
                }

                // Action 2: Review Mistakes
                Surface(
                    onClick = onOpenErrorLog,
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.2.dp, Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.ErrorOutline,
                            containerColor = ClayPeach,
                            iconColor = Color(0xFF9A4518),
                            size = 38.dp,
                            iconSize = 20.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mistakes",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClayPrimaryText
                        )
                    }
                }

                // Action 3: Active Recall
                Surface(
                    onClick = onOpenRevisionWithAi,
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.2.dp, Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClayIconBadge(
                            icon = Icons.Default.AutoAwesome,
                            containerColor = ClayPaleLavenderSurface,
                            iconColor = ClayDeepPurple,
                            size = 38.dp,
                            iconSize = 20.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Recall",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClayPrimaryText
                        )
                    }
                }
            }
        }
    }
}
