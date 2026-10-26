package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.OpportunityEntity
import com.example.data.ProfileAchievementEntity
import com.example.data.ProjectEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.SkeuoCard
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import com.example.viewmodel.ActiveSubScreen
import com.example.viewmodel.BottomNavTab

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    preferences: UserPreferencesEntity,
    achievements: List<ProfileAchievementEntity>,
    opportunities: List<OpportunityEntity>,
    projects: List<ProjectEntity>,
    onUpdatePreferences: ((UserPreferencesEntity) -> UserPreferencesEntity) -> Unit,
    onAddAchievement: (
        category: String,
        title: String,
        dateString: String,
        organization: String,
        role: String,
        whatIDid: String,
        resultSummary: String,
        impactSummary: String,
        evidenceNote: String,
        linkUrl: String
    ) -> Unit,
    onDeleteAchievement: (Int) -> Unit,
    onAddOpportunity: (
        name: String,
        category: String,
        country: String,
        organization: String,
        eligibility: String,
        deadline: String,
        funding: String,
        applicationRequirements: String,
        officialSource: String,
        status: String,
        notes: String,
        isVerified: Boolean
    ) -> Unit,
    onCycleOpportunityStatus: (OpportunityEntity) -> Unit,
    onVerifyOpportunity: (OpportunityEntity) -> Unit,
    onOpenSubScreen: (ActiveSubScreen) -> Unit,
    onNavigateTab: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val subSections = listOf(
        "PORTFOLIO",
        "OPPORTUNITIES",
        "SCHOLARSHIP READINESS"
    )
    var selectedSubSection by rememberSaveable { mutableStateOf("PORTFOLIO") }
    var selectedPortfolioCategory by rememberSaveable { mutableStateOf("ALL") }

    var showEditIdentityDialog by rememberSaveable { mutableStateOf(false) }
    var showAddAchievementDialog by rememberSaveable { mutableStateOf(false) }
    var showAddOpportunityDialog by rememberSaveable { mutableStateOf(false) }

    val portfolioCategories = listOf(
        "ALL",
        "ACADEMICS",
        "PROJECTS",
        "RESEARCH",
        "COMPETITIONS",
        "LEADERSHIP",
        "COMMUNITY IMPACT",
        "TECHNICAL SKILLS",
        "CERTIFICATIONS",
        "AWARDS",
        "SPORTS / ACTIVITIES",
        "COMMUNICATION",
        "ESSAYS",
        "RECOMMENDATIONS",
        "PORTFOLIO"
    )

    val filteredAchievements = if (selectedPortfolioCategory == "ALL") {
        achievements
    } else {
        achievements.filter { it.category == selectedPortfolioCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // STUDENT PORTFOLIO HEADER CARD
        item {
            SkeuoCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.5.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(60.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = preferences.userName.take(1).uppercase().ifBlank { "A" },
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preferences.userName.ifBlank { "Astha" },
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${preferences.studentClass}  •  ${preferences.schoolName}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = { onOpenSubScreen(ActiveSubScreen.SettingsSheet) },
                            modifier = Modifier.testTag("open_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Open Settings",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Subjects: ${preferences.subjectsCsv}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Goals: ${preferences.goalsText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Interests: ${preferences.interestsText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onOpenSubScreen(ActiveSubScreen.CalendarHorizonsSheet("CALENDAR")) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_horizons_button")
                        ) {
                            Text("Life Calendar")
                        }
                        FilledTonalButton(
                            onClick = { onOpenSubScreen(ActiveSubScreen.WeeklyReviewSheet) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_week_review_button")
                        ) {
                            Text("Week Review")
                        }
                        OutlinedButton(
                            onClick = { showEditIdentityDialog = true },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("edit_profile_identity_button")
                        ) {
                            Text("Edit")
                        }
                    }
                }
            }
        }

        // SUB-SECTIONS: PORTFOLIO | OPPORTUNITIES | SCHOLARSHIP READINESS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subSections.forEach { sec ->
                    val isSel = selectedSubSection == sec
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedSubSection = sec }
                            .testTag("profile_subtab_${sec.lowercase().replace(" ", "_")}")
                    ) {
                        Text(
                            text = sec,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        when (selectedSubSection) {
            "OPPORTUNITIES" -> {
                item {
                    SectionHeader(
                        title = "OPPORTUNITIES & SCHOLARSHIPS",
                        subtitle = "Never invent deadlines or funding — verify on official portals",
                        actionText = "+ Save Opportunity",
                        onActionClick = { showAddOpportunityDialog = true }
                    )
                }

                if (opportunities.isEmpty()) {
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
                                Text("No opportunities saved.", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "Save opportunities you may want to explore.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                PrimaryButton(
                                    text = "Save Opportunity",
                                    onClick = { showAddOpportunityDialog = true }
                                )
                            }
                        }
                    }
                } else {
                    items(opportunities, key = { it.id }) { opp ->
                        OpportunityCard(
                            opportunity = opp,
                            onCycleStatus = { onCycleOpportunityStatus(opp) },
                            onVerify = { onVerifyOpportunity(opp) }
                        )
                    }
                }
            }

            "SCHOLARSHIP READINESS" -> {
                item {
                    SectionHeader(
                        title = "SCHOLARSHIP & ADMISSIONS READINESS",
                        subtitle = "Qualitative readiness (STRONG / BUILDING / NEEDS EVIDENCE) — no arbitrary % scores"
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "GLOBAL STRATEGY & CONTINGENCY ENGINES",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Verified opportunities only — official deadlines, family cost ceilings, MEXT 2027/2028 cycle distinction & 3-case scenario contingency.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onOpenSubScreen(ActiveSubScreen.ScholarshipStrategySheet) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).testTag("open_scholarships_strategy_button")
                                ) {
                                    Text("🎓 Scholarships & MEXT", style = MaterialTheme.typography.labelMedium)
                                }
                                FilledTonalButton(
                                    onClick = { onOpenSubScreen(ActiveSubScreen.ScenarioPlanningSheet) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).testTag("open_scenario_planning_button")
                                ) {
                                    Text("🛡️ 3-Scenario Plan", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }

                item {
                    val readinessDomains = listOf(
                        "ACADEMICS",
                        "PROJECTS",
                        "RESEARCH",
                        "LEADERSHIP",
                        "COMMUNITY IMPACT",
                        "COMPETITIONS",
                        "COMMUNICATION",
                        "TECHNICAL SKILLS",
                        "APPLICATION MATERIALS"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        readinessDomains.forEach { domain ->
                            val countInPortfolio = achievements.count { it.category == domain }
                            val projectMatch = if (domain == "PROJECTS") projects.count { it.evidenceCheckedCount >= 4 } else 0
                            val totalEvidence = countInPortfolio + projectMatch

                            val (statusLabel, badgeBg, badgeFg) = when {
                                totalEvidence >= 2 -> Triple("STRONG", GentleSuccessLight, GentleSuccessGreen)
                                totalEvidence == 1 -> Triple("BUILDING", WarmPeachLight, WarmPeachHighlight)
                                else -> Triple("NEEDS EVIDENCE", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            val nextConcreteAdvice = when (domain) {
                                "PROJECTS" -> "Add 1 piece of user feedback or testing evidence to Krow or Smart EcoDrain Shield."
                                "COMPETITIONS" -> "Practice 90-second NCSC explanation and complete your project logbook."
                                "RESEARCH" -> "Document experimental methodology and flow-test results for Smart EcoDrain Shield."
                                "ACADEMICS" -> "Maintain consistent PCM problem sets and weekly Error Log reviews."
                                else -> "Log 1 real documented activity or artifact in your Portfolio when completed."
                            }

                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = domain,
                                            style = MaterialTheme.typography.titleSmall,
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
                                        text = "Next step: $nextConcreteAdvice",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                // PORTFOLIO BUILDER (14 Sections)
                item {
                    SectionHeader(
                        title = "LONG-TERM STUDENT PORTFOLIO",
                        subtitle = "Document real achievements & evidence as you build them",
                        actionText = "+ Add Entry",
                        onActionClick = { showAddAchievementDialog = true }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        portfolioCategories.forEach { cat ->
                            val isSel = selectedPortfolioCategory == cat
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedPortfolioCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                if (filteredAchievements.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(22.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No entries in $selectedPortfolioCategory yet.",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Add real documented evidence when you complete a milestone.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedButton(onClick = { showAddAchievementDialog = true }) {
                                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Real Entry")
                                }
                            }
                        }
                    }
                } else {
                    items(filteredAchievements, key = { it.id }) { ach ->
                        SkeuoCard(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 2.5.dp
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatusBadgeChip(label = ach.category)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = ach.dateString,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        IconButton(
                                            onClick = { onDeleteAchievement(ach.id) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DeleteOutline,
                                                contentDescription = "Delete entry",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = ach.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${ach.role}  •  ${ach.organization}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• What I did: ${ach.whatIDid}\n" +
                                        "• Result: ${ach.resultSummary}\n" +
                                        "• Impact: ${ach.impactSummary}\n" +
                                        "• Evidence: ${ach.evidenceNote}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Edit Student Identity Dialog
    if (showEditIdentityDialog) {
        var name by rememberSaveable { mutableStateOf(preferences.userName) }
        var studentClass by rememberSaveable { mutableStateOf(preferences.studentClass) }
        var school by rememberSaveable { mutableStateOf(preferences.schoolName) }
        var subjects by rememberSaveable { mutableStateOf(preferences.subjectsCsv) }
        var goals by rememberSaveable { mutableStateOf(preferences.goalsText) }
        var interests by rememberSaveable { mutableStateOf(preferences.interestsText) }

        AlertDialog(
            onDismissRequest = { showEditIdentityDialog = false },
            title = { Text("Edit Student Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = studentClass,
                        onValueChange = { studentClass = it },
                        label = { Text("Class") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = school,
                        onValueChange = { school = it },
                        label = { Text("School / Track") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = subjects,
                        onValueChange = { subjects = it },
                        label = { Text("Subjects") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = goals,
                        onValueChange = { goals = it },
                        label = { Text("Goals") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = interests,
                        onValueChange = { interests = it },
                        label = { Text("Interests") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUpdatePreferences {
                            it.copy(
                                userName = name.ifBlank { "Astha" },
                                studentClass = studentClass,
                                schoolName = school,
                                subjectsCsv = subjects,
                                goalsText = goals,
                                interestsText = interests
                            )
                        }
                        showEditIdentityDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditIdentityDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Achievement Dialog
    if (showAddAchievementDialog) {
        var cat by rememberSaveable { mutableStateOf(if (selectedPortfolioCategory == "ALL") "PROJECTS" else selectedPortfolioCategory) }
        var title by rememberSaveable { mutableStateOf("") }
        var date by rememberSaveable { mutableStateOf("2026") }
        var org by rememberSaveable { mutableStateOf("") }
        var role by rememberSaveable { mutableStateOf("") }
        var whatIDid by rememberSaveable { mutableStateOf("") }
        var result by rememberSaveable { mutableStateOf("") }
        var impact by rememberSaveable { mutableStateOf("") }
        var evidence by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddAchievementDialog = false },
            title = { Text("Add Real Portfolio Entry") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cat,
                        onValueChange = { cat = it },
                        label = { Text("Category (e.g., PROJECTS, COMPETITIONS, RESEARCH)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = org,
                        onValueChange = { org = it },
                        label = { Text("Organization") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Your Role") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = whatIDid,
                        onValueChange = { whatIDid = it },
                        label = { Text("What I Did") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = evidence,
                        onValueChange = { evidence = it },
                        label = { Text("Documented Evidence / Artifact") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddAchievement(cat, title, date, org, role, whatIDid, result, impact, evidence, "")
                            showAddAchievementDialog = false
                        }
                    }
                ) {
                    Text("Save Entry")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAchievementDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Opportunity Dialog
    if (showAddOpportunityDialog) {
        var name by rememberSaveable { mutableStateOf("") }
        var cat by rememberSaveable { mutableStateOf("Scholarships") }
        var country by rememberSaveable { mutableStateOf("India") }
        var org by rememberSaveable { mutableStateOf("") }
        var eligibility by rememberSaveable { mutableStateOf("Class 11 / 12") }
        var deadline by rememberSaveable { mutableStateOf("") }
        var source by rememberSaveable { mutableStateOf("") }
        var verified by rememberSaveable { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddOpportunityDialog = false },
            title = { Text("Save Scholarship / Opportunity") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Opportunity Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cat,
                        onValueChange = { cat = it },
                        label = { Text("Category (Scholarships, Competitions, Research...)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = org,
                        onValueChange = { org = it },
                        label = { Text("Organization & Country") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text("Official Deadline (leave blank if unverified)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = source,
                        onValueChange = { source = it },
                        label = { Text("Official Source URL / Portal") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = verified, onCheckedChange = { verified = it })
                        Text("I verified these details on the official portal", style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (name.isNotBlank()) {
                            onAddOpportunity(
                                name,
                                cat,
                                country,
                                org,
                                eligibility,
                                deadline,
                                "Check official portal",
                                "Portfolio & application",
                                source,
                                "Saved",
                                "",
                                verified
                            )
                            showAddOpportunityDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddOpportunityDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun OpportunityCard(
    opportunity: OpportunityEntity,
    onCycleStatus: () -> Unit,
    onVerify: () -> Unit
) {
    SkeuoCard(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 2.5.dp,
        testTag = "opportunity_card_${opportunity.id}"
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
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
                        .clickable { onCycleStatus() }
                ) {
                    Text(
                        text = "${opportunity.status.uppercase()} ↻",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                StatusBadgeChip(
                    label = if (opportunity.isVerified) "✓ Verified" else "⚠️ Unverified",
                    containerColor = if (opportunity.isVerified) GentleSuccessLight else WarmPeachLight,
                    contentColor = if (opportunity.isVerified) GentleSuccessGreen else WarmPeachHighlight
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = opportunity.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${opportunity.category}  •  ${opportunity.organization} (${opportunity.country})",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "• Eligibility: ${opportunity.eligibility}\n" +
                    "• Deadline: ${opportunity.deadline}\n" +
                    "• Requirements: ${opportunity.applicationRequirements}\n" +
                    "• Official Source: ${opportunity.officialSource}\n" +
                    "• Verification: ${opportunity.lastVerifiedDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (opportunity.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Notes: ${opportunity.notes}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (!opportunity.isVerified) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onVerify,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mark Verified After Checking Portal", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
