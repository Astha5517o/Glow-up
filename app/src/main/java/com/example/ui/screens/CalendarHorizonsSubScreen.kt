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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.CalendarEventEntity
import com.example.data.DeadlinePreparationSuggestion
import com.example.data.DefaultWellnessData
import com.example.data.EndOfDayReflectionEntity
import com.example.data.EventPrepMilestoneEntity
import com.example.data.LongTermGoalEntity
import com.example.data.MonthlyGoalEntity
import com.example.data.OsTaskEntity
import com.example.data.SpecialDayPlanEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadgeChip
import com.example.ui.theme.GentleSuccessGreen
import com.example.ui.theme.GentleSuccessLight
import com.example.ui.theme.WarmPeachHighlight
import com.example.ui.theme.WarmPeachLight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val ALL_EVENT_CATEGORIES = listOf(
    "ACADEMICS",
    "EXAMS / TESTS",
    "SCHOOL",
    "COACHING",
    "COMPETITIONS",
    "NCSC",
    "HACKATHONS",
    "SCHOLARSHIPS",
    "APPLICATIONS",
    "PROJECTS",
    "RESEARCH",
    "REVISION",
    "REST / RECOVERY",
    "FAMILY TIME",
    "FRIENDS / SOCIAL",
    "PERSONAL",
    "WELLNESS",
    "TRAVEL",
    "OTHER"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarHorizonsSubScreen(
    events: List<CalendarEventEntity>,
    specialDayPlans: List<SpecialDayPlanEntity>,
    eventPrepMilestones: List<EventPrepMilestoneEntity>,
    monthlyGoals: List<MonthlyGoalEntity>,
    longTermGoals: List<LongTermGoalEntity>,
    deadlineSuggestions: List<DeadlinePreparationSuggestion>,
    preferences: UserPreferencesEntity,
    initialTab: String = "CALENDAR",
    onClose: () -> Unit,
    onAddCalendarEvent: (
        title: String,
        dateString: String,
        endDateString: String,
        startTime: String,
        endTime: String,
        isAllDay: Boolean,
        category: String,
        priority: String,
        location: String,
        notes: String,
        repeatRule: String,
        reminderEnabled: Boolean,
        reminderPreference: String,
        linkedProjectOrGoal: String,
        beforeEventChecklistCsv: String
    ) -> Unit,
    onUpdateCalendarEvent: (CalendarEventEntity) -> Unit,
    onDeleteCalendarEvent: (Int) -> Unit,
    onSaveSpecialDayPlan: (
        dateString: String,
        dayType: String,
        customFocusNote: String,
        customStructureItemsCsv: String,
        conflictOverrideApproved: Boolean
    ) -> Unit,
    onDeleteSpecialDayPlan: (String) -> Unit,
    onHandleEventPrepMilestoneAction: (EventPrepMilestoneEntity, String) -> Unit,
    onAcceptDeadlineSuggestion: (DeadlinePreparationSuggestion) -> Unit,
    onDismissDeadlineSuggestion: (DeadlinePreparationSuggestion) -> Unit,
    onAddMonthlyGoal: (
        domain: String,
        title: String,
        weeklyTarget: String,
        todaySmallestStep: String
    ) -> Unit,
    onAdvanceMonthlyGoal: (MonthlyGoalEntity) -> Unit,
    onUpdateMonthlyGoal: (MonthlyGoalEntity) -> Unit,
    onDeleteMonthlyGoal: (Int) -> Unit,
    onUpdateLongTermGoal: (LongTermGoalEntity) -> Unit,
    onSaveMonthlyReview: (
        whatBuilt: String,
        whatLearned: String,
        whatEvidence: String,
        whatContinue: String,
        whatStop: String
    ) -> Unit,
    onSaveWeeklyPlan: (
        mon: String,
        tue: String,
        wed: String,
        thu: String,
        fri: String,
        sat: String,
        sun: String
    ) -> Unit,
    onStartGoalActionFocus: (title: String, step: String, mins: Int) -> Unit,
    onOpenCloseTodayGently: () -> Unit
) {
    BackHandler { onClose() }

    val mainTabs = listOf(
        "CALENDAR",
        "PLAN MY DAY",
        "MONTHLY GOALS",
        "LONG-TERM VISION",
        "WEEK & MONTH REVIEW"
    )
    var selectedMainTab by rememberSaveable { mutableStateOf(initialTab) }
    // Default calendar view is MONTH as required
    var selectedCalendarViewMode by rememberSaveable { mutableStateOf("MONTH") } // MONTH, WEEK, DAY, UPCOMING, PLAN MY DAY

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    var selectedDateString by rememberSaveable { mutableStateOf(todayStr) }
    var displayedMonthOffset by rememberSaveable { mutableIntStateOf(0) }

    var showAddOrEditEventDialog by rememberSaveable { mutableStateOf(false) }
    var editingEventId by rememberSaveable { mutableStateOf<Int?>(null) }
    var inspectedEventId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAddMonthlyGoalDialog by rememberSaveable { mutableStateOf(false) }
    var editingLongTermGoalId by rememberSaveable { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("calendar_horizons_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP BAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(
                            text = "ASTHA OS LIFE CALENDAR",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "I made time for what mattered to me.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                OutlinedButton(
                    onClick = onOpenCloseTodayGently,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close Today", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // MAIN TAB SWITCHER
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mainTabs.forEach { tab ->
                    val isSel = selectedMainTab == tab
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedMainTab = tab }
                            .testTag("horizon_tab_${tab.lowercase().replace(" ", "_")}")
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        when (selectedMainTab) {
            "CALENDAR" -> {
                // CALENDAR VIEW MODES: MONTH (default), WEEK, DAY, UPCOMING, PLAN MY DAY
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("MONTH", "WEEK", "DAY", "UPCOMING", "PLAN MY DAY").forEach { mode ->
                                val isSel = selectedCalendarViewMode == mode
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSel) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            if (mode == "PLAN MY DAY") {
                                                selectedMainTab = "PLAN MY DAY"
                                            } else {
                                                selectedCalendarViewMode = mode
                                            }
                                        }
                                        .testTag("calendar_view_${mode.lowercase().replace(" ", "_")}")
                                ) {
                                    Text(
                                        text = mode,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                editingEventId = null
                                showAddOrEditEventDialog = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("add_calendar_event_button")
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Event")
                        }
                    }
                }

                // INTERACTIVE MONTH / WEEK CALENDAR WITH PREV/NEXT MONTH & TODAY BUTTON
                if (selectedCalendarViewMode == "MONTH" || selectedCalendarViewMode == "WEEK") {
                    item {
                        InteractiveLifeCalendarGridCard(
                            events = events,
                            specialDayPlans = specialDayPlans,
                            isMonthView = selectedCalendarViewMode == "MONTH",
                            todayDateString = todayStr,
                            selectedDateString = selectedDateString,
                            monthOffset = displayedMonthOffset,
                            onPreviousMonth = { displayedMonthOffset -= 1 },
                            onNextMonth = { displayedMonthOffset += 1 },
                            onJumpToToday = {
                                displayedMonthOffset = 0
                                selectedDateString = todayStr
                            },
                            onSelectDate = { clickedDate ->
                                selectedDateString = clickedDate
                            }
                        )
                    }
                }

                // SELECTED-DATE PANEL (Shows what's happening on selectedDateString, Special Day Type, and quick actions)
                item {
                    SelectedDateInspectorPanel(
                        selectedDateString = selectedDateString,
                        todayDateString = todayStr,
                        eventsOnSelectedDate = events.filter { isDateWithinEventSpan(selectedDateString, it) },
                        specialDayPlan = specialDayPlans.find { it.dateString == selectedDateString },
                        onOpenPlanMyDay = { selectedMainTab = "PLAN MY DAY" },
                        onAddEventForDate = {
                            editingEventId = null
                            showAddOrEditEventDialog = true
                        },
                        onInspectEvent = { ev -> inspectedEventId = ev.id },
                        onEditEvent = { ev ->
                            editingEventId = ev.id
                            showAddOrEditEventDialog = true
                        },
                        onDeleteEvent = onDeleteCalendarEvent,
                        onStartPrep = { ev ->
                            onStartGoalActionFocus(
                                "Prep: ${ev.title}",
                                ev.locationOrNotes.ifBlank { "Complete 1 focused 20-minute preparation step for ${ev.title}" },
                                20
                            )
                        }
                    )
                }

                // EVENT PREPARATION INTELLIGENCE (BEFORE / DURING / AFTER + Accept / Postpone / Dismiss without duplicating)
                val activeMilestones = eventPrepMilestones.filter { it.status == "SUGGESTED" || it.status == "ACCEPTED" }
                if (activeMilestones.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "EVENT PREPARATION INTELLIGENCE",
                            subtitle = "Break big events into small steps — never wait until the night before"
                        )
                    }
                    items(activeMilestones.take(4), key = { it.id }) { milestone ->
                        EventPrepMilestoneCard(
                            milestone = milestone,
                            onAccept = { onHandleEventPrepMilestoneAction(milestone, "ACCEPT") },
                            onStartNow = {
                                onHandleEventPrepMilestoneAction(milestone, "ACCEPT")
                                onStartGoalActionFocus(
                                    milestone.stepTitle,
                                    milestone.stepInstruction,
                                    milestone.durationMinutes
                                )
                            },
                            onComplete = { onHandleEventPrepMilestoneAction(milestone, "COMPLETE") },
                            onPostpone = { onHandleEventPrepMilestoneAction(milestone, "POSTPONE") },
                            onDismiss = { onHandleEventPrepMilestoneAction(milestone, "DISMISS") }
                        )
                    }
                }

                // UPCOMING / FILTERED EVENTS LIST
                val displayedEvents = when (selectedCalendarViewMode) {
                    "DAY" -> events.filter { isDateWithinEventSpan(selectedDateString, it) }
                    "WEEK" -> events.take(6)
                    else -> events
                }

                item {
                    SectionHeader(
                        title = if (selectedCalendarViewMode == "DAY") "EVENTS ON $selectedDateString" else "ALL UPCOMING & MULTI-DAY EVENTS",
                        subtitle = "Tap any event for Before / During / After checklists, rescheduling & notes"
                    )
                }

                items(displayedEvents, key = { it.id }) { ev ->
                    CalendarEventCard(
                        event = ev,
                        todayDateString = todayStr,
                        onInspect = { inspectedEventId = ev.id },
                        onEdit = {
                            editingEventId = ev.id
                            showAddOrEditEventDialog = true
                        },
                        onDelete = { onDeleteCalendarEvent(ev.id) },
                        onStartPrep = {
                            onStartGoalActionFocus(
                                "Prep: ${ev.title}",
                                ev.locationOrNotes.ifBlank { "Complete 1 focused 20-minute preparation step for ${ev.title}" },
                                20
                            )
                        }
                    )
                }
            }

            "PLAN MY DAY" -> {
                item {
                    MyDayMyWayPlannerSection(
                        selectedDateString = selectedDateString,
                        todayDateString = todayStr,
                        onChangeDate = { selectedDateString = it },
                        currentPlan = specialDayPlans.find { it.dateString == selectedDateString },
                        eventsOnDate = events.filter { isDateWithinEventSpan(selectedDateString, it) },
                        upcomingCriticalEvents = events.filter {
                            val diff = calculateDaysBetween(selectedDateString, it.dateString)
                            it.priority == "HIGH" && diff in 0..3
                        },
                        onSaveSpecialDayPlan = onSaveSpecialDayPlan,
                        onDeleteSpecialDayPlan = onDeleteSpecialDayPlan,
                        onStartSuggestedBlock = { title, desc, mins ->
                            onStartGoalActionFocus(title, desc, mins)
                        }
                    )
                }
            }

            "MONTHLY GOALS" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "THIS MONTH'S FOCUS — OCTOBER 2026",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "3–5 realistic monthly priorities connected directly to your weekly targets and today's actions.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { showAddMonthlyGoalDialog = true },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("+ Goal")
                                }
                            }
                        }
                    }
                }

                items(monthlyGoals, key = { it.id }) { goal ->
                    MonthlyGoalCard(
                        goal = goal,
                        onAdvanceStep = { onAdvanceMonthlyGoal(goal) },
                        onCycleDecision = {
                            val decisions = listOf("MUST CONTINUE", "SHOULD CONTINUE", "CAN POSTPONE", "CAN DROP")
                            val next = decisions[(decisions.indexOf(goal.reviewDecision) + 1) % decisions.size]
                            onUpdateMonthlyGoal(goal.copy(reviewDecision = next))
                        },
                        onStartSmallestStep = {
                            onStartGoalActionFocus(
                                "${goal.domain}: ${goal.title}",
                                goal.todaySmallestStep,
                                20
                            )
                        },
                        onDelete = { onDeleteMonthlyGoal(goal.id) }
                    )
                }
            }

            "LONG-TERM VISION" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "LONG-TERM STUDENT VISION (1–3 YEARS)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Big goals never stay abstract. Every Class 11 → Class 12 → Post-School vision connects down to your Monthly Focus, Weekly Target, and Today's Action.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                items(longTermGoals, key = { it.id }) { ltGoal ->
                    LongTermGoalHierarchyCard(
                        goal = ltGoal,
                        onEdit = { editingLongTermGoalId = ltGoal.id },
                        onStartTodayAction = {
                            onStartGoalActionFocus(
                                "${ltGoal.area}: ${ltGoal.todayAction}",
                                "Monthly Focus: ${ltGoal.currentMonthlyFocus} • Weekly: ${ltGoal.weeklyTarget}",
                                25
                            )
                        }
                    )
                }
            }

            "WEEK & MONTH REVIEW" -> {
                item {
                    WeeklyBalancePlannerCard(
                        preferences = preferences,
                        events = events,
                        onSaveWeeklyPlan = onSaveWeeklyPlan
                    )
                }
                item {
                    MonthlyReviewAndAdjustmentCard(
                        preferences = preferences,
                        monthlyGoals = monthlyGoals,
                        onSaveMonthlyReview = onSaveMonthlyReview
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add or Edit Calendar Event Dialog (Full multi-day, all 19 categories, reminders, prep tasks, incomplete-date prompt)
    if (showAddOrEditEventDialog) {
        val existingEvent = events.find { it.id == editingEventId }
        AddOrEditCalendarEventDialog(
            initialEvent = existingEvent,
            defaultDateString = selectedDateString,
            onDismiss = { showAddOrEditEventDialog = false },
            onSaveNew = { title, startD, endD, startT, endT, allDay, cat, prio, loc, notes, rep, remEn, remPref, linkedGoal, checklist ->
                onAddCalendarEvent(
                    title, startD, endD, startT, endT, allDay, cat, prio, loc, notes, rep, remEn, remPref, linkedGoal, checklist
                )
                showAddOrEditEventDialog = false
            },
            onSaveEdited = { updated ->
                onUpdateCalendarEvent(updated)
                showAddOrEditEventDialog = false
            }
        )
    }

    // Event Before / During / After Detail & Checklist Modal
    if (inspectedEventId != null) {
        val ev = events.find { it.id == inspectedEventId }
        if (ev != null) {
            EventLifecycleDetailDialog(
                event = ev,
                onDismiss = { inspectedEventId = null },
                onUpdateEvent = onUpdateCalendarEvent,
                onStartPrepBlock = { stepTitle ->
                    inspectedEventId = null
                    onStartGoalActionFocus(
                        "${ev.title}: $stepTitle",
                        ev.locationOrNotes.ifBlank { "Focused 20-minute preparation step" },
                        20
                    )
                }
            )
        }
    }

    // Add Monthly Goal Dialog
    if (showAddMonthlyGoalDialog) {
        var domain by rememberSaveable { mutableStateOf("PROJECT") }
        var title by rememberSaveable { mutableStateOf("") }
        var weeklyTarget by rememberSaveable { mutableStateOf("") }
        var smallestStep by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddMonthlyGoalDialog = false },
            title = { Text("Add Monthly Goal (October)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("ACADEMICS", "PROJECT", "COMPETITION", "PROFILE", "SKILLS", "WELLNESS").forEach { dom ->
                            val sel = domain == dom
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { domain = dom }
                            ) {
                                Text(
                                    text = dom,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Monthly Goal") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = weeklyTarget,
                        onValueChange = { weeklyTarget = it },
                        label = { Text("Weekly Milestone Target") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = smallestStep,
                        onValueChange = { smallestStep = it },
                        label = { Text("Smallest Next Step Today") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddMonthlyGoal(domain, title, weeklyTarget, smallestStep)
                            showAddMonthlyGoalDialog = false
                        }
                    }
                ) {
                    Text("Add Monthly Goal")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMonthlyGoalDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Long-Term Goal Dialog
    if (editingLongTermGoalId != null) {
        val target = longTermGoals.find { it.id == editingLongTermGoalId }
        if (target != null) {
            var vision by rememberSaveable { mutableStateOf(target.visionStatement) }
            var monthly by rememberSaveable { mutableStateOf(target.currentMonthlyFocus) }
            var weekly by rememberSaveable { mutableStateOf(target.weeklyTarget) }
            var todayAct by rememberSaveable { mutableStateOf(target.todayAction) }

            AlertDialog(
                onDismissRequest = { editingLongTermGoalId = null },
                title = { Text("Update ${target.area} Hierarchy") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = vision,
                            onValueChange = { vision = it },
                            label = { Text("Long-Term Vision (Class 11–12)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = monthly,
                            onValueChange = { monthly = it },
                            label = { Text("Monthly Goal") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = weekly,
                            onValueChange = { weekly = it },
                            label = { Text("Weekly Target") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = todayAct,
                            onValueChange = { todayAct = it },
                            label = { Text("Today's Concrete Action") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUpdateLongTermGoal(
                                target.copy(
                                    visionStatement = vision,
                                    currentMonthlyFocus = monthly,
                                    weeklyTarget = weekly,
                                    todayAction = todayAct
                                )
                            )
                            editingLongTermGoalId = null
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingLongTermGoalId = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun InteractiveLifeCalendarGridCard(
    events: List<CalendarEventEntity>,
    specialDayPlans: List<SpecialDayPlanEntity>,
    isMonthView: Boolean,
    todayDateString: String,
    selectedDateString: String,
    monthOffset: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onJumpToToday: () -> Unit,
    onSelectDate: (String) -> Unit
) {
    val cal = Calendar.getInstance().apply {
        add(Calendar.MONTH, monthOffset)
    }
    val year = cal.get(Calendar.YEAR)
    val monthZeroIndexed = cal.get(Calendar.MONTH)
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val monthTitle = SimpleDateFormat("MMMM yyyy", Locale.US).format(cal.time)

    val firstOfMonthCal = (cal.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    // Sunday = 0 .. Saturday = 6
    val leadingEmptyCells = (firstOfMonthCal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY).coerceIn(0, 6)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header with Previous / Next Month & Today button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPreviousMonth,
                        modifier = Modifier.size(36.dp).testTag("calendar_prev_month")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous month")
                    }
                    Text(
                        text = monthTitle.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = onNextMonth,
                        modifier = Modifier.size(36.dp).testTag("calendar_next_month")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next month")
                    }
                }

                OutlinedButton(
                    onClick = onJumpToToday,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("calendar_today_button")
                ) {
                    Text("Today", style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Weekday header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { wd ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = wd,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dayCells: List<Int?> = if (isMonthView) {
                List(leadingEmptyCells) { null } + (1..daysInMonth).toList()
            } else {
                val selDay = selectedDateString.split("-").lastOrNull()?.toIntOrNull() ?: 9
                val start = (selDay - 3).coerceAtLeast(1)
                val end = (start + 6).coerceAtMost(daysInMonth)
                (start..end).toList()
            }

            val rows = dayCells.chunked(7)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                rows.forEach { weekRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        weekRow.forEach { dayNum ->
                            if (dayNum == null) {
                                Spacer(modifier = Modifier.weight(1f).height(46.dp))
                            } else {
                                val cellDateStr = String.format(
                                    Locale.US,
                                    "%04d-%02d-%02d",
                                    year,
                                    monthZeroIndexed + 1,
                                    dayNum
                                )
                                val isToday = cellDateStr == todayDateString
                                val isSelected = cellDateStr == selectedDateString
                                val matchingEvents = events.filter { isDateWithinEventSpan(cellDateStr, it) }
                                val hasMultiDayEvent = matchingEvents.any { it.isMultiDay }
                                val hasSpecialDayPlan = specialDayPlans.any { it.dateString == cellDateStr }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        isToday -> MaterialTheme.colorScheme.secondaryContainer
                                        hasMultiDayEvent -> WarmPeachLight
                                        matchingEvents.isNotEmpty() -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                        hasSpecialDayPlan -> GentleSuccessLight
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                    },
                                    border = if (isToday && !isSelected) {
                                        BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                                    } else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onSelectDate(cellDateStr) }
                                        .testTag("calendar_day_$cellDateStr")
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize().padding(vertical = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = dayNum.toString(),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected || isToday || matchingEvents.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                                            color = when {
                                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                                hasMultiDayEvent -> WarmPeachHighlight
                                                matchingEvents.isNotEmpty() -> MaterialTheme.colorScheme.onPrimaryContainer
                                                else -> MaterialTheme.colorScheme.onSurface
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                            if (matchingEvents.isNotEmpty()) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                            else if (hasMultiDayEvent) WarmPeachHighlight
                                                            else MaterialTheme.colorScheme.primary
                                                        )
                                                )
                                            }
                                            if (hasSpecialDayPlan) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                            else GentleSuccessGreen
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        repeat(7 - weekRow.size) {
                            Spacer(modifier = Modifier.weight(1f).height(46.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● Event / Exam",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "● Multi-Day (e.g. NCSC)",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmPeachHighlight
                )
                Text(
                    text = "● My Day, My Way",
                    style = MaterialTheme.typography.labelSmall,
                    color = GentleSuccessGreen
                )
            }
        }
    }
}

@Composable
private fun SelectedDateInspectorPanel(
    selectedDateString: String,
    todayDateString: String,
    eventsOnSelectedDate: List<CalendarEventEntity>,
    specialDayPlan: SpecialDayPlanEntity?,
    onOpenPlanMyDay: () -> Unit,
    onAddEventForDate: () -> Unit,
    onInspectEvent: (CalendarEventEntity) -> Unit,
    onEditEvent: (CalendarEventEntity) -> Unit,
    onDeleteEvent: (Int) -> Unit,
    onStartPrep: (CalendarEventEntity) -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("selected_date_panel")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (selectedDateString == todayDateString) {
                            "SELECTED DATE • TODAY ($selectedDateString)"
                        } else {
                            "SELECTED DATE • $selectedDateString"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = specialDayPlan?.let { "My Day, My Way: ${it.dayType}" }
                            ?: "No custom day type set yet — tap 'Plan This Day' to shape it.",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = if (specialDayPlan != null) GentleSuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = onOpenPlanMyDay,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Plan Day", style = MaterialTheme.typography.labelSmall)
                    }
                    OutlinedButton(
                        onClick = onAddEventForDate,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("+ Event", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            if (specialDayPlan != null && specialDayPlan.customStructureItemsCsv.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = GentleSuccessLight.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "PLANNED RHYTHM FOR ${specialDayPlan.dayType}:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GentleSuccessGreen
                        )
                        specialDayPlan.customStructureItemsCsv.split(",").filter { it.isNotBlank() }.forEach { item ->
                            Text(
                                text = "• ${item.trim()}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            if (eventsOnSelectedDate.isEmpty()) {
                Text(
                    text = "No scheduled events on $selectedDateString. Space is open for your chosen rhythm.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    eventsOnSelectedDate.forEach { ev ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onInspectEvent(ev) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        StatusBadgeChip(label = ev.category)
                                        if (ev.isMultiDay) {
                                            StatusBadgeChip(
                                                label = "${ev.dateString} → ${ev.effectiveEndDate}",
                                                containerColor = WarmPeachLight,
                                                contentColor = WarmPeachHighlight
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = ev.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${ev.startTime} – ${ev.endTime}" +
                                            if (ev.location.isNotBlank()) " • ${ev.location}" else "",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Row {
                                    IconButton(onClick = { onEditEvent(ev) }) {
                                        Icon(Icons.Filled.Edit, contentDescription = "Edit event", modifier = Modifier.size(18.dp))
                                    }
                                    FilledTonalButton(
                                        onClick = { onInspectEvent(ev) },
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("Checklist", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyDayMyWayPlannerSection(
    selectedDateString: String,
    todayDateString: String,
    onChangeDate: (String) -> Unit,
    currentPlan: SpecialDayPlanEntity?,
    eventsOnDate: List<CalendarEventEntity>,
    upcomingCriticalEvents: List<CalendarEventEntity>,
    onSaveSpecialDayPlan: (
        dateString: String,
        dayType: String,
        customFocusNote: String,
        customStructureItemsCsv: String,
        conflictOverrideApproved: Boolean
    ) -> Unit,
    onDeleteSpecialDayPlan: (String) -> Unit,
    onStartSuggestedBlock: (title: String, desc: String, mins: Int) -> Unit
) {
    val templates = DefaultWellnessData.dayTypeTemplates
    var chosenType by rememberSaveable(selectedDateString, currentPlan?.dayType) {
        mutableStateOf(currentPlan?.dayType ?: "BALANCED DAY")
    }
    val activeTemplate = templates.find { it.typeName == chosenType } ?: templates.first()
    var customFocusNote by rememberSaveable(selectedDateString, chosenType) {
        mutableStateOf(currentPlan?.customFocusNote?.ifBlank { activeTemplate.tagline } ?: activeTemplate.tagline)
    }
    var customItemsText by rememberSaveable(selectedDateString, chosenType) {
        mutableStateOf(
            currentPlan?.customStructureItemsCsv?.ifBlank { activeTemplate.defaultStructure.joinToString(", ") }
                ?: activeTemplate.defaultStructure.joinToString(", ")
        )
    }
    var dateInput by rememberSaveable(selectedDateString) { mutableStateOf(selectedDateString) }

    val isLightDay = chosenType in listOf(
        "REST / RESET DAY",
        "FAMILY TIME DAY",
        "FRIENDS / SOCIAL DAY",
        "OUTDOOR / EXPERIENCE DAY"
    )
    val hasConflict = isLightDay && (eventsOnDate.any { it.priority == "HIGH" } || upcomingCriticalEvents.isNotEmpty())

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "MY DAY, MY WAY — SPECIAL DAY PLANNER",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Designate any day as a Study Day, Project Day, Competition Prep Day, Family Time Day, Rest Day, or Balanced Day. ASTHA OS automatically adapts your daily load and YOUR NEXT MOVE.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = dateInput,
                        onValueChange = {
                            dateInput = it
                            if (it.length == 10) onChangeDate(it)
                        },
                        label = { Text("Date to Plan (YYYY-MM-DD)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = {
                            dateInput = todayDateString
                            onChangeDate(todayDateString)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Today")
                    }
                }
            }
        }

        // Conflict Detection Card (When a Rest/Family day overlaps with an urgent Exam or NCSC)
        if (hasConflict) {
            val conflictEvent = (eventsOnDate + upcomingCriticalEvents).first()
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmPeachLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "CALM SCHEDULE CONFLICT DETECTED",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = WarmPeachHighlight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You selected $chosenType for $dateInput, and '${conflictEvent.title}' (${conflictEvent.dateString}) is right around the corner.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Suggested compromise: Do 1 early 15-minute prep block in the morning so the rest of your $chosenType stays 100% protected and guilt-free.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val blendedItems = "1 early 15-min prep for ${conflictEvent.title}," +
                                    activeTemplate.defaultStructure.joinToString(",")
                                customItemsText = blendedItems
                                onSaveSpecialDayPlan(
                                    dateInput,
                                    chosenType,
                                    "15-min early prep + protected $chosenType",
                                    blendedItems,
                                    true
                                )
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Use 15m Early Prep + Free Day")
                        }
                        OutlinedButton(
                            onClick = {
                                onSaveSpecialDayPlan(
                                    dateInput,
                                    chosenType,
                                    customFocusNote,
                                    customItemsText,
                                    true
                                )
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Keep 100% Rest/Family")
                        }
                    }
                }
            }
        }

        // 13 Day Type Selector Chips
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "CHOOSE DAY TYPE FOR $dateInput",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    templates.forEach { tpl ->
                        val isSel = chosenType == tpl.typeName
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    chosenType = tpl.typeName
                                    customFocusNote = tpl.tagline
                                    customItemsText = tpl.defaultStructure.joinToString(", ")
                                }
                                .testTag("day_type_${tpl.typeName.lowercase().replace(" ", "_")}")
                        ) {
                            Text(
                                text = tpl.typeName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = activeTemplate.typeName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Recommended Load: ${activeTemplate.recommendedLoadLabel}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        activeTemplate.defaultStructure.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = customFocusNote,
                    onValueChange = { customFocusNote = it },
                    label = { Text("Your Intention for This Day") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customItemsText,
                    onValueChange = { customItemsText = it },
                    label = { Text("Day Structure (comma-separated blocks)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveSpecialDayPlan(
                                dateInput,
                                chosenType,
                                customFocusNote,
                                customItemsText,
                                false
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("save_special_day_button")
                    ) {
                        Text("Save '$chosenType'")
                    }
                    if (currentPlan != null) {
                        OutlinedButton(
                            onClick = { onDeleteSpecialDayPlan(dateInput) },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventPrepMilestoneCard(
    milestone: EventPrepMilestoneEntity,
    onAccept: () -> Unit,
    onStartNow: () -> Unit,
    onComplete: () -> Unit,
    onPostpone: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadgeChip(label = "${milestone.phase} EVENT • ${milestone.eventTitle}")
                StatusBadgeChip(
                    label = "${milestone.durationMinutes} min",
                    containerColor = GentleSuccessLight,
                    contentColor = GentleSuccessGreen
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = milestone.stepTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = milestone.stepInstruction,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (milestone.status != "ACCEPTED") {
                    Button(
                        onClick = onAccept,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Accept to Today")
                    }
                } else {
                    FilledTonalButton(
                        onClick = onComplete,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("✓ Mark Done")
                    }
                }
                OutlinedButton(
                    onClick = onStartNow,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Start (${milestone.durationMinutes}m)")
                }
                TextButton(onClick = onPostpone) {
                    Text("Postpone")
                }
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
private fun CalendarEventCard(
    event: CalendarEventEntity,
    todayDateString: String,
    onInspect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStartPrep: () -> Unit
) {
    val daysDiff = calculateDaysBetween(todayDateString, event.dateString)
    val isOngoingToday = isDateWithinEventSpan(todayDateString, event)
    val calmCountdown = when {
        isOngoingToday -> "Happening Today • ${event.startTime}"
        daysDiff < 0 -> "Completed / Past"
        daysDiff == 1 -> "Tomorrow • ${event.startTime}"
        else -> "In $daysDiff days"
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onInspect() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadgeChip(label = "${event.category} • ${event.priority}")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadgeChip(
                        label = calmCountdown,
                        containerColor = if (daysDiff in 0..3 || isOngoingToday) WarmPeachLight else GentleSuccessLight,
                        contentColor = if (daysDiff in 0..3 || isOngoingToday) WarmPeachHighlight else GentleSuccessGreen
                    )
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit event",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "Delete event",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            val dateRangeLabel = if (event.isMultiDay) {
                "${event.dateString} → ${event.effectiveEndDate} (Multi-day)"
            } else {
                event.dateString
            }
            Text(
                text = "$dateRangeLabel  •  ${event.startTime} – ${event.endTime}" +
                    if (event.location.isNotBlank()) "  •  ${event.location}" else "",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (event.locationOrNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = event.locationOrNotes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (event.linkedProjectOrGoal.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Linked Goal/Project: ${event.linkedProjectOrGoal}  •  Status: ${event.attendanceOrCompletionStatus}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onInspect,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Before / During / After")
                }
                FilledTonalButton(
                    onClick = onStartPrep,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Prep Now (20m)")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EventLifecycleDetailDialog(
    event: CalendarEventEntity,
    onDismiss: () -> Unit,
    onUpdateEvent: (CalendarEventEntity) -> Unit,
    onStartPrepBlock: (String) -> Unit
) {
    var phaseTab by rememberSaveable { mutableStateOf("BEFORE") } // BEFORE, DURING, AFTER
    var status by rememberSaveable { mutableStateOf(event.attendanceOrCompletionStatus) }
    var beforeCsv by rememberSaveable { mutableStateOf(event.beforeEventChecklistCsv) }
    var duringNotes by rememberSaveable { mutableStateOf(event.duringEventNotes) }
    var afterReflection by rememberSaveable { mutableStateOf(event.afterEventReflection) }
    var newChecklistItem by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = if (event.isMultiDay) "${event.dateString} → ${event.effectiveEndDate}" else event.dateString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Status selector
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("PLANNED", "PREPARING", "ATTENDED", "COMPLETED", "RESCHEDULED").forEach { st ->
                        val sel = status == st
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { status = st }
                        ) {
                            Text(
                                text = st,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Phase Switcher: BEFORE / DURING / AFTER
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("BEFORE", "DURING", "AFTER").forEach { ph ->
                        val sel = phaseTab == ph
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (sel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).clickable { phaseTab = ph }
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    text = "$ph EVENT",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                when (phaseTab) {
                    "BEFORE" -> {
                        Text(
                            text = "Preparation Checklist & Packing List:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        val items = beforeCsv.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        items.forEach { step ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• $step",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    TextButton(onClick = { onStartPrepBlock(step) }) {
                                        Text("Start 20m", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedTextField(
                                value = newChecklistItem,
                                onValueChange = { newChecklistItem = it },
                                label = { Text("Add prep/packing step") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    if (newChecklistItem.isNotBlank()) {
                                        beforeCsv = (items + newChecklistItem.trim()).joinToString(",")
                                        newChecklistItem = ""
                                    }
                                }
                            ) {
                                Text("Add")
                            }
                        }
                    }
                    "DURING" -> {
                        Text(
                            text = "During Event — Quick Notes, Judge Questions & Reminders:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = duringNotes,
                            onValueChange = { duringNotes = it },
                            label = { Text("Capture questions, observations, or mentor feedback") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "AFTER" -> {
                        Text(
                            text = "After Event — Reflection, Mistakes to Review & Portfolio Evidence:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = afterReflection,
                            onValueChange = { afterReflection = it },
                            label = { Text("What went well? What evidence or certificate should be saved?") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdateEvent(
                        event.copy(
                            attendanceOrCompletionStatus = status,
                            beforeEventChecklistCsv = beforeCsv,
                            duringEventNotes = duringNotes,
                            afterEventReflection = afterReflection
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Save Event Notes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddOrEditCalendarEventDialog(
    initialEvent: CalendarEventEntity?,
    defaultDateString: String,
    onDismiss: () -> Unit,
    onSaveNew: (
        title: String,
        dateString: String,
        endDateString: String,
        startTime: String,
        endTime: String,
        isAllDay: Boolean,
        category: String,
        priority: String,
        location: String,
        notes: String,
        repeatRule: String,
        reminderEnabled: Boolean,
        reminderPreference: String,
        linkedProjectOrGoal: String,
        beforeEventChecklistCsv: String
    ) -> Unit,
    onSaveEdited: (CalendarEventEntity) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(initialEvent?.title ?: "") }
    var startDateStr by rememberSaveable { mutableStateOf(initialEvent?.dateString ?: defaultDateString) }
    var endDateStr by rememberSaveable { mutableStateOf(initialEvent?.endDateString?.ifBlank { initialEvent.dateString } ?: defaultDateString) }
    var isAllDay by rememberSaveable { mutableStateOf(initialEvent?.isAllDay ?: false) }
    var startTime by rememberSaveable { mutableStateOf(initialEvent?.startTime ?: "9:00 AM") }
    var endTime by rememberSaveable { mutableStateOf(initialEvent?.endTime ?: "10:30 AM") }
    var category by rememberSaveable { mutableStateOf(initialEvent?.category ?: "ACADEMICS") }
    var priority by rememberSaveable { mutableStateOf(initialEvent?.priority ?: "HIGH") }
    var location by rememberSaveable { mutableStateOf(initialEvent?.location ?: "") }
    var notes by rememberSaveable { mutableStateOf(initialEvent?.locationOrNotes ?: "") }
    var repeatRule by rememberSaveable { mutableStateOf(initialEvent?.repeatRule ?: "None") }
    var reminderEnabled by rememberSaveable { mutableStateOf(initialEvent?.reminderEnabled ?: true) }
    var reminderPref by rememberSaveable { mutableStateOf(initialEvent?.reminderPreference ?: "Day before & 1 hour before") }
    var linkedGoal by rememberSaveable { mutableStateOf(initialEvent?.linkedProjectOrGoal ?: "") }
    var dateValidationPrompt by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initialEvent == null) "Add Life Calendar Event" else "Edit / Reschedule Event")
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Event Title (e.g., NCSC / Physics Test / Family Outing)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = startDateStr,
                            onValueChange = {
                                startDateStr = it
                                dateValidationPrompt = null
                            },
                            label = { Text("Start Date (YYYY-MM-DD)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = endDateStr,
                            onValueChange = {
                                endDateStr = it
                                dateValidationPrompt = null
                            },
                            label = { Text("End Date (Multi-day)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (dateValidationPrompt != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dateValidationPrompt!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("All-Day Event", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = isAllDay, onCheckedChange = { isAllDay = it })
                    }
                }
                if (!isAllDay) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = startTime,
                                onValueChange = { startTime = it },
                                label = { Text("Start Time") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = endTime,
                                onValueChange = { endTime = it },
                                label = { Text("End Time") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                item {
                    Text("Category:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ALL_EVENT_CATEGORIES.forEach { cat ->
                            val sel = category == cat
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable { category = cat }
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
                item {
                    Text("Importance & Repeat:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("HIGH", "MEDIUM", "NORMAL").forEach { prio ->
                            val sel = priority == prio
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (sel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable { priority = prio }
                            ) {
                                Text(
                                    text = prio,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("None", "Daily", "Weekly", "Monthly").forEach { rep ->
                            val sel = repeatRule == rep
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable { repeatRule = rep }
                            ) {
                                Text(
                                    text = rep,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = linkedGoal,
                        onValueChange = { linkedGoal = it },
                        label = { Text("Linked Project or Goal (e.g. Smart EcoDrain Shield / Krow)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes / Syllabus / Key Details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable Calm Reminders", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = reminderEnabled, onCheckedChange = { reminderEnabled = it })
                    }
                    if (reminderEnabled) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "Day before & 1 hour before",
                                "On event day",
                                "At chosen time",
                                "For prep milestones"
                            ).forEach { pref ->
                                val sel = reminderPref == pref
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { reminderPref = pref }
                                ) {
                                    Text(
                                        text = pref,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
                    if (!dateRegex.matches(startDateStr.trim())) {
                        dateValidationPrompt = "Please specify the complete year, month, and day (YYYY-MM-DD, e.g., 2026-10-11) so ASTHA OS doesn't guess the month or year."
                        return@Button
                    }
                    val cleanEnd = endDateStr.trim().ifBlank { startDateStr.trim() }
                    if (!dateRegex.matches(cleanEnd)) {
                        dateValidationPrompt = "Please specify the complete end date (YYYY-MM-DD, e.g., 2026-10-13)."
                        return@Button
                    }
                    if (title.isNotBlank()) {
                        if (initialEvent == null) {
                            onSaveNew(
                                title,
                                startDateStr.trim(),
                                cleanEnd,
                                startTime,
                                endTime,
                                isAllDay,
                                category,
                                priority,
                                location,
                                notes,
                                repeatRule,
                                reminderEnabled,
                                reminderPref,
                                linkedGoal,
                                ""
                            )
                        } else {
                            onSaveEdited(
                                initialEvent.copy(
                                    title = title.trim(),
                                    dateString = startDateStr.trim(),
                                    endDateString = cleanEnd,
                                    startTime = if (isAllDay) "All Day" else startTime,
                                    endTime = if (isAllDay) "All Day" else endTime,
                                    isAllDay = isAllDay,
                                    category = category,
                                    priority = priority,
                                    location = location.trim(),
                                    locationOrNotes = notes.trim(),
                                    repeatRule = repeatRule,
                                    reminderEnabled = reminderEnabled,
                                    reminderPreference = reminderPref,
                                    linkedProjectOrGoal = linkedGoal.trim()
                                )
                            )
                        }
                    }
                }
            ) {
                Text(if (initialEvent == null) "Save Event" else "Update Event")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun WeeklyBalancePlannerCard(
    preferences: UserPreferencesEntity,
    events: List<CalendarEventEntity>,
    onSaveWeeklyPlan: (String, String, String, String, String, String, String) -> Unit
) {
    var mon by rememberSaveable { mutableStateOf(preferences.weeklyPlanMon) }
    var tue by rememberSaveable { mutableStateOf(preferences.weeklyPlanTue) }
    var wed by rememberSaveable { mutableStateOf(preferences.weeklyPlanWed) }
    var thu by rememberSaveable { mutableStateOf(preferences.weeklyPlanThu) }
    var fri by rememberSaveable { mutableStateOf(preferences.weeklyPlanFri) }
    var sat by rememberSaveable { mutableStateOf(preferences.weeklyPlanSat) }
    var sun by rememberSaveable { mutableStateOf(preferences.weeklyPlanSun) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "WEEKLY BALANCE PLANNER",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Balance fixed commitments, study sessions, project building, event prep, wellness, and family/rest across the week.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = mon, onValueChange = { mon = it }, label = { Text("Monday") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = tue, onValueChange = { tue = it }, label = { Text("Tuesday") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = wed, onValueChange = { wed = it }, label = { Text("Wednesday") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = thu, onValueChange = { thu = it }, label = { Text("Thursday") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = fri, onValueChange = { fri = it }, label = { Text("Friday") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = sat, onValueChange = { sat = it }, label = { Text("Saturday") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = sun, onValueChange = { sun = it }, label = { Text("Sunday (Family & Rest Protected)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = "Save Weekly Balance Plan",
                onClick = { onSaveWeeklyPlan(mon, tue, wed, thu, fri, sat, sun) }
            )
        }
    }
}

@Composable
private fun MonthlyGoalCard(
    goal: MonthlyGoalEntity,
    onAdvanceStep: () -> Unit,
    onCycleDecision: () -> Unit,
    onStartSmallestStep: () -> Unit,
    onDelete: () -> Unit
) {
    val progressFraction = (goal.progressStepsDone.toFloat() / goal.progressStepsTotal.coerceAtLeast(1)).coerceIn(0f, 1f)

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
                StatusBadgeChip(label = goal.domain)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onCycleDecision() }
                    ) {
                        Text(
                            text = goal.reviewDecision,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "Delete monthly goal",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = goal.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Weekly Target: ${goal.weeklyTarget}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Smallest Next Step Today: ${goal.todaySmallestStep}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${goal.progressStepsDone} / ${goal.progressStepsTotal} milestones done",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onAdvanceStep,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+1 Step")
                    }
                    Button(
                        onClick = onStartSmallestStep,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Start Today's Step")
                    }
                }
            }
        }
    }
}

@Composable
private fun LongTermGoalHierarchyCard(
    goal: LongTermGoalEntity,
    onEdit: () -> Unit,
    onStartTodayAction: () -> Unit
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
                StatusBadgeChip(label = goal.area)
                TextButton(onClick = onEdit) {
                    Text("Edit Hierarchy")
                }
            }
            Text(
                text = goal.visionStatement,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            HierarchyStepRow("1. LONG-TERM (Class 11 → 12)", "${goal.class11Milestone} → ${goal.class12Milestone}")
            HierarchyStepRow("2. THIS MONTH'S GOAL", goal.currentMonthlyFocus)
            HierarchyStepRow("3. THIS WEEK'S TARGET", goal.weeklyTarget)
            HierarchyStepRow("4. TODAY'S ACTION", goal.todayAction, highlight = true)

            Spacer(modifier = Modifier.height(12.dp))
            FilledTonalButton(
                onClick = onStartTodayAction,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Do Today's Action (${goal.todayAction})")
            }
        }
    }
}

@Composable
private fun HierarchyStepRow(label: String, value: String, highlight: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun MonthlyReviewAndAdjustmentCard(
    preferences: UserPreferencesEntity,
    monthlyGoals: List<MonthlyGoalEntity>,
    onSaveMonthlyReview: (
        whatBuilt: String,
        whatLearned: String,
        whatEvidence: String,
        whatContinue: String,
        whatStop: String
    ) -> Unit
) {
    var whatBuilt by rememberSaveable { mutableStateOf(preferences.monthReviewWhatBuilt) }
    var whatLearned by rememberSaveable { mutableStateOf(preferences.monthReviewWhatLearned) }
    var whatEvidence by rememberSaveable { mutableStateOf(preferences.monthReviewWhatEvidence) }
    var whatContinue by rememberSaveable { mutableStateOf(preferences.monthReviewContinue) }
    var whatStop by rememberSaveable { mutableStateOf(preferences.monthReviewStop) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "CALM MONTHLY REVIEW & GOAL ADJUSTMENT",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "When a month gets busy with exams or health, adjust goals without guilt. Keep what matters, postpone what can wait.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = whatBuilt,
                onValueChange = { whatBuilt = it },
                label = { Text("1. What did I build or improve this month?") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = whatLearned,
                onValueChange = { whatLearned = it },
                label = { Text("2. What did I learn across subjects & projects?") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = whatEvidence,
                onValueChange = { whatEvidence = it },
                label = { Text("3. What real evidence did I create?") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = whatContinue,
                onValueChange = { whatContinue = it },
                label = { Text("4. What should continue next month?") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = whatStop,
                onValueChange = { whatStop = it },
                label = { Text("5. What should stop or simplify?") },
                modifier = Modifier.fillMaxWidth()
            )

            PrimaryButton(
                text = "Save Monthly Reflection",
                onClick = {
                    onSaveMonthlyReview(whatBuilt, whatLearned, whatEvidence, whatContinue, whatStop)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CloseTodayGentlySubScreen(
    todayDateString: String,
    activeDayType: String,
    unfinishedTasks: List<OsTaskEntity>,
    existingReflection: EndOfDayReflectionEntity?,
    onClose: () -> Unit,
    onHandleUnfinishedTask: (OsTaskEntity, String) -> Unit,
    onSaveReflection: (
        meaningfulToday: String,
        learnedOrExperienced: String,
        gladMadeTimeFor: String,
        carryIntoTomorrow: String,
        letGoOfForNow: String,
        quickTagSelected: String,
        tomorrowFirstStep: String
    ) -> Unit
) {
    BackHandler { onClose() }

    var meaningful by rememberSaveable { mutableStateOf(existingReflection?.meaningfulToday ?: "") }
    var learned by rememberSaveable { mutableStateOf(existingReflection?.learnedOrExperienced ?: "") }
    var gladTimeFor by rememberSaveable { mutableStateOf(existingReflection?.gladMadeTimeFor ?: "") }
    var carryTomorrow by rememberSaveable { mutableStateOf(existingReflection?.carryIntoTomorrow ?: "") }
    var letGoOf by rememberSaveable { mutableStateOf(existingReflection?.letGoOfForNow ?: "") }
    var quickTag by rememberSaveable { mutableStateOf(existingReflection?.quickTagSelected ?: "") }
    var tomorrowFirst by rememberSaveable { mutableStateOf(existingReflection?.tomorrowFirstStep ?: "") }

    val quickTags = listOf(
        "I rested.",
        "I spent time with my parents.",
        "I explored something interesting.",
        "I completed my revision.",
        "I had an ordinary day, and that's okay."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("close_today_gently_screen"),
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
                Column {
                    Text(
                        text = "CLOSE TODAY GENTLY",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "“I made time for what mattered to me.” • $todayDateString ($activeDayType)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Quick 1-Tap Reflection Tags (For low-energy or social/rest days)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ONE-TAP DAY CLOSURE (NO PRESSURE)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Even on a quiet, family, or rest day, you can close today in one tap:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickTags.forEach { tag ->
                            val sel = quickTag == tag
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        quickTag = tag
                                        if (meaningful.isBlank()) meaningful = tag
                                    }
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Calm Unfinished Task Triage (Reschedule / Keep in Backlog / Drop / Decide Tomorrow)
        if (unfinishedTasks.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "UNFINISHED TASKS — LET GO WITHOUT GUILT",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Choose what happens to unfinished items so your mind is clear tonight:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        unfinishedTasks.take(4).forEach { task ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf("RESCHEDULE", "KEEP IN BACKLOG", "DECIDE TOMORROW", "DROP").forEach { dec ->
                                            OutlinedButton(
                                                onClick = { onHandleUnfinishedTask(task, dec) },
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(dec, style = MaterialTheme.typography.labelSmall)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5 Calm Reflection Prompts + Tomorrow's First Step
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "MEANINGFUL DAY REFLECTION",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = meaningful,
                        onValueChange = { meaningful = it },
                        label = { Text("What felt meaningful today?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = learned,
                        onValueChange = { learned = it },
                        label = { Text("What did I learn, build, or experience?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = gladTimeFor,
                        onValueChange = { gladTimeFor = it },
                        label = { Text("What am I glad I made time for?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = carryTomorrow,
                        onValueChange = { carryTomorrow = it },
                        label = { Text("What should I carry into tomorrow?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = letGoOf,
                        onValueChange = { letGoOf = it },
                        label = { Text("What can I let go of for now?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tomorrowFirst,
                        onValueChange = { tomorrowFirst = it },
                        label = { Text("Tomorrow's #1 Clear First Step (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PrimaryButton(
                        text = "Complete Evening Closure",
                        onClick = {
                            onSaveReflection(
                                meaningful,
                                learned,
                                gladTimeFor,
                                carryTomorrow,
                                letGoOf,
                                quickTag,
                                tomorrowFirst
                            )
                        },
                        testTag = "save_close_today_button"
                    )
                }
            }
        }
    }
}

fun isDateWithinEventSpan(targetDateString: String, event: CalendarEventEntity): Boolean {
    val start = event.dateString
    val end = event.effectiveEndDate
    return targetDateString >= start && targetDateString <= end
}

fun calculateDaysBetween(startDateString: String, endDateString: String): Int {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val d1 = sdf.parse(startDateString) ?: return 0
        val d2 = sdf.parse(endDateString) ?: return 0
        val diffMs = d2.time - d1.time
        (diffMs / (1000L * 60 * 60 * 24)).toInt()
    } catch (_: Exception) {
        0
    }
}
