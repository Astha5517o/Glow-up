package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ConfidenceEntryEntity
import com.example.data.DailyHabitEntity
import com.example.data.DailyProgressEntity
import com.example.data.DefaultWellnessData
import com.example.data.GeneratedRoutineBlock
import com.example.data.GlowUpRepository
import com.example.data.HydrationReminderEntity
import com.example.data.MovementRoutine
import com.example.data.SkincareStepEntity
import com.example.data.UserPreferencesEntity
import com.example.notifications.GlowUpNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class BottomNavTab(val route: String, val label: String) {
    HOME("home", "Home"),
    MOVE("move", "Move"),
    GLOW("glow", "Glow"),
    PROGRESS("progress", "Progress"),
    PROFILE("profile", "Profile")
}

sealed class ActiveSubScreen {
    data object None : ActiveSubScreen()
    data object HydrationSheet : ActiveSubScreen()
    data object SleepSheet : ActiveSubScreen()
    data object ConfidenceCheckInSheet : ActiveSubScreen()
    data object StudyResetSheet : ActiveSubScreen()
    data object DailyRoutineGeneratorSheet : ActiveSubScreen()
    data class ActiveWorkoutTimer(val routine: MovementRoutine) : ActiveSubScreen()
}

data class WeeklyStatsUi(
    val movementCompletedDays: Int = 4,
    val movementTargetDays: Int = 5,
    val hydrationCompletedCount: Int = 7,
    val hydrationTargetCount: Int = 10,
    val skincareCompletedCount: Int = 10,
    val skincareTargetCount: Int = 10,
    val sleepCompletedDays: Int = 6,
    val sleepTargetDays: Int = 7,
    val confidenceCompletedDays: Int = 4,
    val confidenceTargetDays: Int = 7,
    val strongestHabitTitle: String = "✨ Skincare",
    val latestReflectionText: String = "Showed up for myself with patience and kindness."
)

class GlowUpViewModel(
    private val repository: GlowUpRepository,
    private val appContext: Context
) : ViewModel() {

    val preferences: StateFlow<UserPreferencesEntity> = repository.userPreferences
        .combine(MutableStateFlow(UserPreferencesEntity())) { dbPrefs, fallback ->
            dbPrefs ?: fallback
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferencesEntity())

    val habits: StateFlow<List<DailyHabitEntity>> = repository.activeHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val skincareSteps: StateFlow<List<SkincareStepEntity>> = repository.skincareSteps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hydrationReminders: StateFlow<List<HydrationReminderEntity>> = repository.hydrationReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val confidenceEntries: StateFlow<List<ConfidenceEntryEntity>> = repository.confidenceEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressHistory: StateFlow<List<DailyProgressEntity>> = repository.recentProgressHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTab = MutableStateFlow(BottomNavTab.HOME)
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()

    private val _activeSubScreen = MutableStateFlow<ActiveSubScreen>(ActiveSubScreen.None)
    val activeSubScreen: StateFlow<ActiveSubScreen> = _activeSubScreen.asStateFlow()

    private val _exportSummaryDialogText = MutableStateFlow<String?>(null)
    val exportSummaryDialogText: StateFlow<String?> = _exportSummaryDialogText.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    val weeklyStats: StateFlow<WeeklyStatsUi> = combine(
        progressHistory,
        confidenceEntries,
        habits
    ) { history, entries, currentHabits ->
        if (history.isEmpty()) {
            WeeklyStatsUi()
        } else {
            val moveDays = history.count { it.movementCompleted }.coerceIn(0, 5)
            val hydrationCount = (history.sumOf { it.hydrationCompletedCount } +
                if (currentHabits.any { it.category == "HYDRATE" && it.completedToday }) 1 else 0)
                .coerceIn(0, 10)
            val skincareCount = history.sumOf {
                (if (it.morningSkincareCompleted) 1 else 0) + (if (it.eveningSkincareCompleted) 1 else 0)
            }.coerceIn(0, 10)
            val sleepDays = history.count { it.sleepWindDownCompleted }.coerceIn(0, 7)
            val confidenceDays = maxOf(
                history.count { it.confidenceCheckInCompleted },
                entries.size
            ).coerceIn(0, 7)

            val ratios = listOf(
                "✨ Skincare" to (skincareCount / 10f),
                "🧘 Movement" to (moveDays / 5f),
                "😴 Sleep routine" to (sleepDays / 7f),
                "💧 Hydration" to (hydrationCount / 10f),
                "🌸 Confidence" to (confidenceDays / 7f)
            )
            val strongest = ratios.maxByOrNull { it.second }?.first ?: "✨ Skincare"
            val latestProud = entries.firstOrNull { it.proudOfText.isNotBlank() }?.proudOfText
                ?: "Taking time to rest, hydrate, and care for myself consistently."

            WeeklyStatsUi(
                movementCompletedDays = moveDays,
                movementTargetDays = 5,
                hydrationCompletedCount = hydrationCount,
                hydrationTargetCount = 10,
                skincareCompletedCount = skincareCount,
                skincareTargetCount = 10,
                sleepCompletedDays = sleepDays,
                sleepTargetDays = 7,
                confidenceCompletedDays = confidenceDays,
                confidenceTargetDays = 7,
                strongestHabitTitle = strongest,
                latestReflectionText = latestProud
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeeklyStatsUi())

    init {
        GlowUpNotificationHelper.createNotificationChannel(appContext)
        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }
    }

    fun selectTab(tab: BottomNavTab) {
        _activeSubScreen.value = ActiveSubScreen.None
        _selectedTab.value = tab
    }

    fun openSubScreen(subScreen: ActiveSubScreen) {
        _activeSubScreen.value = subScreen
    }

    fun closeSubScreen() {
        _activeSubScreen.value = ActiveSubScreen.None
    }

    fun clearBannerMessage() {
        _statusBannerMessage.value = null
    }

    fun showGentleMessage(msg: String) {
        _statusBannerMessage.value = msg
    }

    fun getDynamicGreeting(prefs: UserPreferencesEntity): String {
        if (prefs.customGreeting.isNotBlank()) {
            return prefs.customGreeting
        }
        val name = prefs.userName.ifBlank { "Astha" }
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 4..11 -> "Good morning, $name ☀️"
            in 12..16 -> "Hope your day is going well, $name 🌱"
            else -> "Wind down gently, $name 🌙"
        }
    }

    fun generateDailyRoutine(prefs: UserPreferencesEntity): List<GeneratedRoutineBlock> {
        val focusSet = prefs.selectedFocusAreas.split(",").map { it.trim() }.toSet()
        val duration = prefs.dailyTimeCommitmentMinutes
        val prefWindow = prefs.freeTimeWindow

        val morningItems = mutableListOf<String>()
        if (prefWindow == "Morning" || prefWindow == "Flexible") {
            morningItems.add("5 min wake-up mobility")
        } else {
            morningItems.add("2 min gentle morning stretch")
        }
        if ("Stay hydrated" in focusSet || focusSet.isEmpty()) {
            morningItems.add("Morning water reminder")
        }
        morningItems.add("Morning skincare & fresh start")

        val studyItems = mutableListOf(
            "Study reset (${prefs.studyResetIntervalMinutes} min screen break)",
            "Hydration reminder"
        )
        if ("Improve posture" in focusSet) {
            studyItems.add("1 min shoulder & posture release")
        }

        val eveningItems = mutableListOf<String>()
        if (prefWindow == "Evening" || prefWindow == "Afternoon" || prefWindow == "Flexible") {
            eveningItems.add("$duration min movement (${if ("Build strength" in focusSet) "Strength & capability" else "Mobility & flow"})")
        } else {
            eveningItems.add("5 min gentle evening stretch")
        }
        eveningItems.add("Evening skincare routine")
        eveningItems.add("Prepare clothes & bag for tomorrow")

        val nightItems = listOf(
            "Wind-down at ${prefs.windDownTime}",
            "Confidence check-in 🌸",
            "Restful sleep by ${prefs.targetBedtime}"
        )

        return listOf(
            GeneratedRoutineBlock("Morning", "☀️", morningItems),
            GeneratedRoutineBlock("During school/study", "📚", studyItems),
            GeneratedRoutineBlock("Evening", "🌆", eveningItems),
            GeneratedRoutineBlock("Night", "🌙", nightItems)
        )
    }

    fun completeOnboarding(
        userName: String,
        focusAreas: Set<String>,
        freeTime: String,
        durationMinutes: Int
    ) {
        viewModelScope.launch {
            repository.completeOnboarding(userName, focusAreas, freeTime, durationMinutes)
            scheduleConfiguredReminders()
            _statusBannerMessage.value = "Your personal routine is ready, ${userName.ifBlank { "Astha" }} ✨"
        }
    }

    fun toggleHabit(habit: DailyHabitEntity) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habit)
        }
    }

    fun addCustomHabit(title: String, subtitle: String, emoji: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addCustomHabit(title, subtitle, emoji)
            _statusBannerMessage.value = "Added \"${title.trim()}\" to your daily care."
        }
    }

    fun deleteHabit(id: Int) {
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
    }

    fun startWorkoutRoutine(routine: MovementRoutine) {
        _activeSubScreen.value = ActiveSubScreen.ActiveWorkoutTimer(routine)
    }

    fun finishWorkoutRoutine(routine: MovementRoutine) {
        viewModelScope.launch {
            repository.markMovementRoutineCompleted(routine.title, routine.durationMinutes)
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "Way to care for yourself! ${routine.title} completed 🧘"
        }
    }

    fun toggleSkincareStep(step: SkincareStepEntity) {
        viewModelScope.launch {
            repository.toggleSkincareStep(step)
        }
    }

    fun addSkincareStep(period: String, title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addSkincareStep(period, title)
            _statusBannerMessage.value = "Added step to your ${period.lowercase()} routine ✨"
        }
    }

    fun deleteSkincareStep(id: Int) {
        viewModelScope.launch {
            repository.deleteSkincareStep(id)
        }
    }

    fun toggleHydrationReminderEnabled(reminder: HydrationReminderEntity) {
        viewModelScope.launch {
            repository.toggleHydrationReminderEnabled(reminder)
            scheduleConfiguredReminders()
        }
    }

    fun toggleHydrationReminderCompleted(reminder: HydrationReminderEntity) {
        viewModelScope.launch {
            repository.toggleHydrationReminderCompleted(reminder)
        }
    }

    fun addHydrationReminder(timeLabel: String, hour24: Int, minute: Int) {
        viewModelScope.launch {
            repository.addHydrationReminder(timeLabel, hour24, minute)
            scheduleConfiguredReminders()
            _statusBannerMessage.value = "Hydration reminder added for $timeLabel 💧"
        }
    }

    fun deleteHydrationReminder(id: Int) {
        viewModelScope.launch {
            repository.deleteHydrationReminder(id)
        }
    }

    fun updatePreferences(transform: (UserPreferencesEntity) -> UserPreferencesEntity) {
        viewModelScope.launch {
            val updated = transform(preferences.value)
            repository.savePreferences(updated)
            scheduleConfiguredReminders()
        }
    }

    fun saveConfidenceCheckIn(
        moodEmoji: String,
        moodLabel: String,
        proudOfText: String,
        helpedFeelGoodText: String
    ) {
        viewModelScope.launch {
            repository.saveConfidenceEntry(moodEmoji, moodLabel, proudOfText, helpedFeelGoodText)
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "Reflection saved. Thank you for taking a moment for yourself 🌸"
        }
    }

    fun markSleepWindDownDone() {
        viewModelScope.launch {
            repository.markSleepWindDownCompleted()
            _statusBannerMessage.value = "Evening wind-down marked complete. Rest well tonight 🌙"
        }
    }

    fun acknowledgeMissedBedtimeSupport() {
        viewModelScope.launch {
            repository.acknowledgeMissedBedtime()
            _statusBannerMessage.value = "Tonight didn't go as planned. That's okay. Tomorrow is another chance. 🌙"
        }
    }

    fun triggerSampleNotification(sample: GlowUpNotificationHelper.GentleNotificationSample) {
        GlowUpNotificationHelper.showNotification(
            context = appContext,
            notificationId = sample.id,
            title = sample.title,
            message = sample.body
        )
        _statusBannerMessage.value = "${sample.title} — ${sample.body}"
    }

    private suspend fun scheduleConfiguredReminders() {
        val prefs = preferences.value
        if (!prefs.notificationsEnabled || !prefs.hydrationRemindersEnabled || prefs.hydrationPaused) {
            return
        }
        hydrationReminders.value.filter { it.isEnabled }.forEach { reminder ->
            GlowUpNotificationHelper.scheduleDailyReminder(
                context = appContext,
                requestCode = 3000 + reminder.id,
                hour24 = reminder.hour24,
                minute = reminder.minute,
                title = "💧 Hydration break",
                message = "Take a moment for some water."
            )
        }
    }

    fun resetTodaysHabits() {
        viewModelScope.launch {
            repository.resetTodaysHabits()
            _statusBannerMessage.value = "Today's habits have been gently reset. Your day is still yours."
        }
    }

    fun exportUserData() {
        viewModelScope.launch {
            _exportSummaryDialogText.value = repository.buildExportDataSummary()
        }
    }

    fun dismissExportDialog() {
        _exportSummaryDialogText.value = null
    }

    fun deleteAllUserData() {
        viewModelScope.launch {
            repository.deleteAllUserData()
            _selectedTab.value = BottomNavTab.HOME
            _activeSubScreen.value = ActiveSubScreen.None
            _statusBannerMessage.value = "All personal data has been cleared and reset to fresh defaults."
        }
    }

    companion object {
        fun provideFactory(
            repository: GlowUpRepository,
            appContext: Context
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GlowUpViewModel(repository, appContext) as T
            }
        }
    }
}
