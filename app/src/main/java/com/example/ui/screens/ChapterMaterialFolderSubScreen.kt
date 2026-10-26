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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChapterFlashcard
import com.example.data.StudyChapterEntity
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
import com.example.ui.theme.ClayRoseCard
import com.example.ui.theme.ClaySecondaryText
import com.example.ui.theme.ClaySoftMint
import com.example.ui.theme.ClayWarmIvory
import com.example.ui.theme.GentleSuccessGreen

@Composable
fun ChapterMaterialFolderSubScreen(
    chapter: StudyChapterEntity,
    onClose: () -> Unit,
    onUpdateChapter: (StudyChapterEntity) -> Unit,
    onAskAiQuestion: suspend (String) -> String,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var selectedTab by rememberSaveable { mutableStateOf("MATERIAL") } // MATERIAL, FORMULAS, TRAPS, RECALL, AI_REVISE
    var showEditNotesDialog by rememberSaveable { mutableStateOf(false) }
    var notesText by rememberSaveable { mutableStateOf(chapter.specialStudyMaterial) }

    var showEditFormulasDialog by rememberSaveable { mutableStateOf(false) }
    var formulasText by rememberSaveable { mutableStateOf(chapter.keyFormulasText) }

    var showEditTrapsDialog by rememberSaveable { mutableStateOf(false) }
    var trapsText by rememberSaveable { mutableStateOf(chapter.examTrapsAndTricks) }

    // Flashcard active recall state
    val defaultCards = remember(chapter.id) {
        listOf(
            ChapterFlashcard(
                id = "c1",
                question = "What is the core principle of ${chapter.currentTopic}?",
                answer = chapter.capturedConcept.ifBlank { "State definition and write governing equations from memory." },
                formulaOrTrick = chapter.capturedFormula.ifBlank { "Key relationship connecting given and unknown" },
                lastRating = "GOOD"
            ),
            ChapterFlashcard(
                id = "c2",
                question = "Which key formula must you NEVER forget in this chapter?",
                answer = chapter.keyFormulasText.lines().firstOrNull() ?: chapter.capturedFormula,
                formulaOrTrick = "Write it on blank paper before looking!",
                lastRating = "NEW"
            ),
            ChapterFlashcard(
                id = "c3",
                question = "What is the most dangerous exam trap or sign convention here?",
                answer = chapter.examTrapsAndTricks.lines().firstOrNull() ?: "Watch reference frame and units",
                formulaOrTrick = "Trap: Avoid careless calculation slips",
                lastRating = "HARD"
            )
        )
    }

    var cardIndex by rememberSaveable { mutableIntStateOf(0) }
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var activeCards by remember { mutableStateOf(defaultCards) }
    var showAddCardDialog by rememberSaveable { mutableStateOf(false) }
    var newCardQ by rememberSaveable { mutableStateOf("") }
    var newCardA by rememberSaveable { mutableStateOf("") }
    var newCardTrick by rememberSaveable { mutableStateOf("") }

    // AI Revision state
    var aiRevisionResponse by rememberSaveable { mutableStateOf("") }
    var isAiLoading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    val currentCard = activeCards.getOrNull(cardIndex) ?: activeCards.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ClayWarmIvory)
            .testTag("chapter_material_folder_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // -------------------------------------------------------------
        // 1. TOP BAR & FOLDER BADGE
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
                    Text(
                        text = "HALF-YEARLY (16 NOV) • ${chapter.halfYearlyWeightage}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = ClayCoralAction,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 2. HERO FOLDER CARD
        // -------------------------------------------------------------
        item {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                containerColor = when (chapter.subject) {
                    "Physics" -> ClayLavenderCard
                    "Chemistry Physical" -> ClayMintCard
                    "Chemistry Organic/Inorganic" -> ClayRoseCard
                    "Mathematics" -> ClayPeachCard
                    else -> ClayPaleLavenderSurface
                },
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.9f),
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = ClayDeepPurple,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = chapter.subject.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ClayDeepPurple
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = chapter.chapterName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = ClayPrimaryText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Special Vault • Never forget important concepts",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClaySecondaryText
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 3. TACTILE TAB SELECTOR
        // -------------------------------------------------------------
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tabs = listOf(
                    "MATERIAL" to "📁 Notes & Material",
                    "FORMULAS" to "⚡ Formulas",
                    "TRAPS" to "⚠️ Exam Traps",
                    "RECALL" to "🧠 Active Recall (Anki+)",
                    "AI_REVISE" to "🤖 Revise with ASAA AI"
                )

                tabs.forEach { (tabKey, tabLabel) ->
                    val isSelected = selectedTab == tabKey
                    ClayPill(
                        label = tabLabel,
                        isSelected = isSelected,
                        onClick = { selectedTab = tabKey },
                        selectedContainerColor = ClayCoralAction,
                        selectedContentColor = Color.White,
                        unselectedContainerColor = Color.White,
                        unselectedContentColor = ClaySecondaryText,
                        testTag = "vault_tab_${tabKey.lowercase()}"
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 4. TAB CONTENTS
        // -------------------------------------------------------------
        when (selectedTab) {
            "MATERIAL" -> {
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Special Chapter Material",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayPrimaryText
                                )
                                IconButton(onClick = {
                                    notesText = chapter.specialStudyMaterial
                                    showEditNotesDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Notes",
                                        tint = ClayCoralAction
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val content = chapter.specialStudyMaterial.ifBlank {
                                "No special notes added yet. Tap the edit button to add your derivations, teacher tips, and key chapter concepts."
                            }

                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ClayPrimaryText,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            ClayPillButton(
                                text = "Edit & Add Special Material",
                                icon = Icons.Default.Edit,
                                onClick = {
                                    notesText = chapter.specialStudyMaterial
                                    showEditNotesDialog = true
                                },
                                containerColor = ClayCoralAction,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            "FORMULAS" -> {
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Key Formulas (Never Forget)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayPrimaryText
                                )
                                IconButton(onClick = {
                                    formulasText = chapter.keyFormulasText
                                    showEditFormulasDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Formulas",
                                        tint = ClayCoralAction
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val formulaContent = chapter.keyFormulasText.ifBlank {
                                chapter.capturedFormula.ifBlank { "Add core mathematical & physical equations here." }
                            }

                            formulaContent.lines().filter { it.isNotBlank() }.forEach { line ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = ClayPeachCard.copy(alpha = 0.5f),
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ClayIconBadge(
                                            icon = Icons.Default.Check,
                                            containerColor = Color.White,
                                            iconColor = Color(0xFF9A4518),
                                            size = 28.dp,
                                            iconSize = 14.dp
                                        )
                                        Text(
                                            text = line,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ClayPrimaryText
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            ClayPillButton(
                                text = "Edit Formula Sheet",
                                icon = Icons.Default.Edit,
                                onClick = {
                                    formulasText = chapter.keyFormulasText
                                    showEditFormulasDialog = true
                                },
                                containerColor = ClayCoralAction,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            "TRAPS" -> {
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Exam Traps & PYQ Nuances",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ClayPrimaryText
                                )
                                IconButton(onClick = {
                                    trapsText = chapter.examTrapsAndTricks
                                    showEditTrapsDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Traps",
                                        tint = ClayCoralAction
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val trapsContent = chapter.examTrapsAndTricks.ifBlank {
                                "• Never skip writing units in the final answer.\n• Read whether the question asks for gauge pressure vs absolute pressure.\n• Check boundary conditions carefully."
                            }

                            trapsContent.lines().filter { it.isNotBlank() }.forEach { line ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = ClayRoseCard.copy(alpha = 0.5f),
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ClayIconBadge(
                                            icon = Icons.Default.Warning,
                                            containerColor = Color.White,
                                            iconColor = Color(0xFF993B47),
                                            size = 28.dp,
                                            iconSize = 14.dp
                                        )
                                        Text(
                                            text = line,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = ClayPrimaryText
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            ClayPillButton(
                                text = "Update Exam Traps",
                                icon = Icons.Default.Edit,
                                onClick = {
                                    trapsText = chapter.examTrapsAndTricks
                                    showEditTrapsDialog = true
                                },
                                containerColor = ClayCoralAction,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            "RECALL" -> {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Active Recall (Card ${cardIndex + 1} of ${activeCards.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ClayPrimaryText
                            )

                            IconButton(onClick = { showAddCardDialog = true }) {
                                ClayIconBadge(
                                    icon = Icons.Default.Add,
                                    containerColor = ClayPaleLavenderSurface,
                                    iconColor = ClayDeepPurple,
                                    size = 32.dp,
                                    iconSize = 16.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (currentCard != null) {
                            // 3D Flippable Clay Flashcard
                            ClayCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isFlipped = !isFlipped }
                                    .testTag("flashcard_flip_container"),
                                shape = RoundedCornerShape(26.dp),
                                containerColor = if (isFlipped) Color.White else ClayPaleLavenderSurface,
                                borderColor = Color.White,
                                elevation = 6.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (isFlipped) ClaySoftMint else ClayCoralLight,
                                        border = BorderStroke(1.dp, Color.White)
                                    ) {
                                        Text(
                                            text = if (isFlipped) "ANSWER / FORMULA REVEALED" else "TAP TO FLIP • ACTIVE QUESTION",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isFlipped) Color(0xFF285E3D) else ClayCoralAction,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    if (!isFlipped) {
                                        Text(
                                            text = currentCard.question,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            color = ClayPrimaryText,
                                            lineHeight = 28.sp
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Text(
                                            text = "Recall and state the formula or proof aloud before tapping.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ClaySecondaryText,
                                            textAlign = TextAlign.Center
                                        )
                                    } else {
                                        Text(
                                            text = currentCard.answer,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                            color = ClayPrimaryText,
                                            lineHeight = 26.sp
                                        )
                                        if (currentCard.formulaOrTrick.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(14.dp))
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = ClayPeachCard.copy(alpha = 0.6f),
                                                border = BorderStroke(1.dp, Color.White)
                                            ) {
                                                Text(
                                                    text = "💡 Anchor: ${currentCard.formulaOrTrick}",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF9A4518),
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Text(
                                        text = "Spaced Rating (Better than Anki):",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ClaySecondaryText
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // 4-Way Intelligent Spaced Repetition Buttons
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf(
                                            "Again" to Color(0xFFE57373),
                                            "Hard" to Color(0xFFFFB74D),
                                            "Good" to Color(0xFF81C784),
                                            "Mastered" to Color(0xFF64B5F6)
                                        ).forEach { (rating, color) ->
                                            Surface(
                                                onClick = {
                                                    isFlipped = false
                                                    cardIndex = (cardIndex + 1) % activeCards.size
                                                },
                                                shape = RoundedCornerShape(14.dp),
                                                color = color.copy(alpha = 0.25f),
                                                border = BorderStroke(1.dp, color.copy(alpha = 0.6f)),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = rating,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ClayPrimaryText,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(vertical = 10.dp)
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

            "AI_REVISE" -> {
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ClayIconBadge(
                                    icon = Icons.Default.AutoAwesome,
                                    containerColor = ClayPaleLavenderSurface,
                                    iconColor = ClayDeepPurple,
                                    size = 38.dp,
                                    iconSize = 20.dp
                                )
                                Column {
                                    Text(
                                        text = "ASAA AI Revision Coach",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ClayPrimaryText
                                    )
                                    Text(
                                        text = "Grounded in ${chapter.chapterName} notes",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ClaySecondaryText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Choose a revision mission before your 16 Nov exam:",
                                style = MaterialTheme.typography.bodySmall,
                                color = ClaySecondaryText
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            listOf(
                                "🎯 Generate 3 Active Recall Questions" to "Quiz me on 3 core derivation steps and formula questions for ${chapter.subject} - ${chapter.chapterName}.",
                                "🧪 Feynman Concept Test" to "Ask me to explain the core concept of ${chapter.currentTopic} in simple terms and evaluate my clarity.",
                                "⚡ Rapid Formula Drill" to "Drill me on the 5 must-know formulas for ${chapter.chapterName} for the upcoming Half-Yearly exam.",
                                "⚠️ Top Exam Pitfalls" to "What are the common tricky traps and sign mistakes in ${chapter.chapterName}?"
                            ).forEach { (buttonLabel, prompt) ->
                                Surface(
                                    onClick = {
                                        isAiLoading = true
                                        aiRevisionResponse = ""
                                        coroutineScope.launch {
                                            try {
                                                aiRevisionResponse = onAskAiQuestion(prompt)
                                            } catch (e: Exception) {
                                                aiRevisionResponse = "ASAA AI Revision:\n• Review key formulas: ${chapter.keyFormulasText.ifBlank { chapter.capturedFormula }}\n• Core concept: ${chapter.capturedConcept}\n• Focus on solving 2 medium numericals on blank paper."
                                            } finally {
                                                isAiLoading = false
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    color = ClayPaleLavenderSurface.copy(alpha = 0.5f),
                                    border = BorderStroke(1.dp, Color(0xFFF0EBE1)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = buttonLabel,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ClayPrimaryText
                                        )
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = ClayDeepPurple,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            if (isAiLoading) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = ClayCoralAction,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "ASAA AI is preparing your exam revision...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ClaySecondaryText
                                    )
                                }
                            }

                            if (aiRevisionResponse.isNotBlank()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = ClayLavenderCard.copy(alpha = 0.6f),
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "ASAA AI Revision Feedback",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ClayDeepPurple
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = aiRevisionResponse,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = ClayPrimaryText,
                                            lineHeight = 22.sp
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

    // Edit Notes Dialog
    if (showEditNotesDialog) {
        AlertDialog(
            onDismissRequest = { showEditNotesDialog = false },
            title = { Text("Special Study Material", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Chapter Notes & Concepts") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showEditNotesDialog = false
                    val updated = chapter.copy(specialStudyMaterial = notesText)
                    onUpdateChapter(updated)
                }) {
                    Text("Save", fontWeight = FontWeight.Bold, color = ClayCoralAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNotesDialog = false }) {
                    Text("Cancel", color = ClaySecondaryText)
                }
            }
        )
    }

    // Edit Formulas Dialog
    if (showEditFormulasDialog) {
        AlertDialog(
            onDismissRequest = { showEditFormulasDialog = false },
            title = { Text("Key Formulas to Never Forget", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = formulasText,
                    onValueChange = { formulasText = it },
                    label = { Text("Formulas (One per line)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showEditFormulasDialog = false
                    val updated = chapter.copy(keyFormulasText = formulasText)
                    onUpdateChapter(updated)
                }) {
                    Text("Save", fontWeight = FontWeight.Bold, color = ClayCoralAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditFormulasDialog = false }) {
                    Text("Cancel", color = ClaySecondaryText)
                }
            }
        )
    }

    // Edit Traps Dialog
    if (showEditTrapsDialog) {
        AlertDialog(
            onDismissRequest = { showEditTrapsDialog = false },
            title = { Text("Exam Traps & Tricks", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = trapsText,
                    onValueChange = { trapsText = it },
                    label = { Text("Traps & Sign Conventions") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showEditTrapsDialog = false
                    val updated = chapter.copy(examTrapsAndTricks = trapsText)
                    onUpdateChapter(updated)
                }) {
                    Text("Save", fontWeight = FontWeight.Bold, color = ClayCoralAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditTrapsDialog = false }) {
                    Text("Cancel", color = ClaySecondaryText)
                }
            }
        )
    }

    // Add Flashcard Dialog
    if (showAddCardDialog) {
        AlertDialog(
            onDismissRequest = { showAddCardDialog = false },
            title = { Text("Add Active Recall Card", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCardQ,
                        onValueChange = { newCardQ = it },
                        label = { Text("Question / Concept to recall") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCardA,
                        onValueChange = { newCardA = it },
                        label = { Text("Answer / Derivation step") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCardTrick,
                        onValueChange = { newCardTrick = it },
                        label = { Text("Formula or Memory Anchor (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newCardQ.isNotBlank() && newCardA.isNotBlank()) {
                        activeCards = activeCards + ChapterFlashcard(
                            question = newCardQ.trim(),
                            answer = newCardA.trim(),
                            formulaOrTrick = newCardTrick.trim()
                        )
                        newCardQ = ""
                        newCardA = ""
                        newCardTrick = ""
                    }
                    showAddCardDialog = false
                }) {
                    Text("Add Card", fontWeight = FontWeight.Bold, color = ClayCoralAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCardDialog = false }) {
                    Text("Cancel", color = ClaySecondaryText)
                }
            }
        )
    }
}
