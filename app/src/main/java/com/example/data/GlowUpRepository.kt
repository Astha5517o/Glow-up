package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GlowUpRepository(private val dao: GlowUpDao) {

    val userPreferences: Flow<UserPreferencesEntity?> = dao.getUserPreferences()
    val activeHabits: Flow<List<DailyHabitEntity>> = dao.getActiveHabits()
    val skincareSteps: Flow<List<SkincareStepEntity>> = dao.getSkincareSteps()
    val hydrationReminders: Flow<List<HydrationReminderEntity>> = dao.getHydrationReminders()
    val confidenceEntries: Flow<List<ConfidenceEntryEntity>> = dao.getConfidenceEntries()
    val recentProgressHistory: Flow<List<DailyProgressEntity>> = dao.getRecentProgressHistory()

    fun todayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    suspend fun initializeDefaultsIfNeeded() {
        val today = todayDateString()
        var prefs = dao.getUserPreferencesOnce()
        if (prefs == null) {
            prefs = UserPreferencesEntity(lastActiveDate = today)
            dao.saveUserPreferences(prefs)
        }

        val existingHabits = dao.getAllHabitsOnce()
        if (existingHabits.isEmpty()) {
            dao.insertHabits(
                DefaultWellnessData.defaultHabits(
                    moveDuration = prefs.dailyTimeCommitmentMinutes,
                    windDownTime = prefs.windDownTime
                )
            )
        }

        val existingSkincare = dao.getSkincareStepsOnce()
        if (existingSkincare.isEmpty()) {
            dao.insertSkincareSteps(DefaultWellnessData.defaultSkincareSteps())
        }

        val existingHydration = dao.getHydrationRemindersOnce()
        if (existingHydration.isEmpty()) {
            dao.insertHydrationReminders(DefaultWellnessData.defaultHydrationReminders())
        }

        // Seed 6 days of gentle historical progress if history is empty so the Weekly Progress tab looks meaningful right away while remaining 100% reactive to today's real actions
        val history = dao.getAllProgressHistoryOnce()
        if (history.isEmpty()) {
            val cal = java.util.Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            for (daysAgo in 6 downTo 1) {
                cal.time = Date()
                cal.add(java.util.Calendar.DAY_OF_YEAR, -daysAgo)
                val dateStr = sdf.format(cal.time)
                dao.saveDailyProgress(
                    DailyProgressEntity(
                        dateString = dateStr,
                        movementCompleted = daysAgo % 2 != 0,
                        hydrationCompletedCount = if (daysAgo % 2 == 0) 1 else 1,
                        morningSkincareCompleted = true,
                        eveningSkincareCompleted = daysAgo != 3,
                        sleepWindDownCompleted = daysAgo != 4,
                        confidenceCheckInCompleted = daysAgo <= 4
                    )
                )
            }
            dao.saveDailyProgress(DailyProgressEntity(dateString = today))
        }

        // Daily reset check if date changed
        if (prefs.lastActiveDate.isNotEmpty() && prefs.lastActiveDate != today) {
            dao.resetAllHabitsForToday()
            dao.resetSkincareForToday()
            dao.resetHydrationForToday()
            dao.saveUserPreferences(prefs.copy(lastActiveDate = today))
            if (dao.getProgressForDate(today) == null) {
                dao.saveDailyProgress(DailyProgressEntity(dateString = today))
            }
        }
    }

    suspend fun savePreferences(prefs: UserPreferencesEntity) {
        dao.saveUserPreferences(prefs)
    }

    suspend fun completeOnboarding(
        userName: String,
        focusAreas: Set<String>,
        freeTime: String,
        durationMinutes: Int
    ) {
        val current = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val updated = current.copy(
            userName = userName.ifBlank { "Astha" },
            onboardingCompleted = true,
            selectedFocusAreas = focusAreas.joinToString(","),
            freeTimeWindow = freeTime,
            dailyTimeCommitmentMinutes = durationMinutes,
            lastActiveDate = todayDateString()
        )
        dao.saveUserPreferences(updated)

        // Update Move habit subtitle with chosen duration
        val habits = dao.getAllHabitsOnce()
        habits.find { it.category == "MOVE" }?.let { moveHabit ->
            dao.updateHabit(moveHabit.copy(subtitle = "$durationMinutes min mobility"))
        }
    }

    suspend fun toggleHabitCompletion(habit: DailyHabitEntity) {
        val newStatus = !habit.completedToday
        val updatedSubtitle = when (habit.category) {
            "GLOW" -> if (newStatus) "Morning skincare complete" else "Morning & evening care"
            "HYDRATE" -> if (newStatus) "Hydration break complete" else "Next reminder in 42 min"
            "MOVE" -> if (newStatus) "Movement complete for today" else habit.subtitle
            "SLEEP" -> if (newStatus) "Wind-down complete" else habit.subtitle
            "CONFIDENCE" -> if (newStatus) "Evening check-in complete" else "Evening check-in"
            else -> habit.subtitle
        }
        dao.updateHabit(habit.copy(completedToday = newStatus, subtitle = updatedSubtitle))
        syncTodayProgress(habit.category, newStatus)
    }

    private suspend fun syncTodayProgress(category: String, completed: Boolean) {
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        val updated = when (category) {
            "MOVE" -> current.copy(movementCompleted = completed)
            "HYDRATE" -> current.copy(
                hydrationCompletedCount = if (completed) maxOf(1, current.hydrationCompletedCount + 1) else current.hydrationCompletedCount
            )
            "GLOW" -> current.copy(morningSkincareCompleted = completed)
            "SLEEP" -> current.copy(sleepWindDownCompleted = completed)
            "CONFIDENCE" -> current.copy(confidenceCheckInCompleted = completed)
            else -> current
        }
        dao.saveDailyProgress(updated)
    }

    suspend fun addCustomHabit(title: String, subtitle: String, emoji: String) {
        val habits = dao.getAllHabitsOnce()
        val nextOrder = (habits.maxOfOrNull { it.sortOrder } ?: 5) + 1
        dao.insertHabit(
            DailyHabitEntity(
                category = "CUSTOM",
                title = title.trim(),
                subtitle = subtitle.trim().ifEmpty { "Daily self-care" },
                emoji = emoji.ifEmpty { "🌿" },
                completedToday = false,
                sortOrder = nextOrder
            )
        )
    }

    suspend fun deleteHabit(id: Int) {
        dao.deleteHabit(id)
    }

    suspend fun markMovementRoutineCompleted(routineTitle: String, durationMinutes: Int) {
        val habits = dao.getAllHabitsOnce()
        habits.find { it.category == "MOVE" }?.let { moveHabit ->
            dao.updateHabit(
                moveHabit.copy(
                    completedToday = true,
                    subtitle = "$durationMinutes min • $routineTitle complete"
                )
            )
        }
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(movementCompleted = true))
    }

    suspend fun toggleSkincareStep(step: SkincareStepEntity) {
        val updated = step.copy(completedToday = !step.completedToday)
        dao.updateSkincareStep(updated)

        val allSteps = dao.getSkincareStepsOnce()
        val morningSteps = allSteps.filter { it.period == "MORNING" }
        val eveningSteps = allSteps.filter { it.period == "EVENING" }

        val morningDone = morningSteps.isNotEmpty() && morningSteps.count { it.completedToday } >= (morningSteps.size / 2).coerceAtLeast(1)
        val eveningDone = eveningSteps.isNotEmpty() && eveningSteps.count { it.completedToday } >= (eveningSteps.size / 2).coerceAtLeast(1)

        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(
            current.copy(
                morningSkincareCompleted = morningDone,
                eveningSkincareCompleted = eveningDone
            )
        )

        if (morningDone || eveningDone) {
            val statusText = when {
                morningDone && eveningDone -> "Morning & evening skincare complete"
                morningDone -> "Morning skincare complete"
                else -> "Evening skincare complete"
            }
            dao.updateHabitByCategory("GLOW", true, statusText)
        }
    }

    suspend fun addSkincareStep(period: String, title: String) {
        val all = dao.getSkincareStepsOnce().filter { it.period == period }
        val nextOrder = (all.maxOfOrNull { it.sortOrder } ?: 0) + 1
        dao.insertSkincareStep(
            SkincareStepEntity(
                period = period,
                title = title.trim(),
                completedToday = false,
                sortOrder = nextOrder
            )
        )
    }

    suspend fun deleteSkincareStep(id: Int) {
        dao.deleteSkincareStep(id)
    }

    suspend fun toggleHydrationReminderEnabled(reminder: HydrationReminderEntity) {
        dao.updateHydrationReminder(reminder.copy(isEnabled = !reminder.isEnabled))
    }

    suspend fun toggleHydrationReminderCompleted(reminder: HydrationReminderEntity) {
        val newDone = !reminder.completedToday
        dao.updateHydrationReminder(reminder.copy(completedToday = newDone))

        val all = dao.getHydrationRemindersOnce()
        val completedCount = all.count { it.completedToday }
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(hydrationCompletedCount = completedCount))

        if (completedCount > 0) {
            dao.updateHabitByCategory(
                "HYDRATE",
                true,
                "$completedCount hydration break${if (completedCount > 1) "s" else ""} enjoyed today"
            )
        }
    }

    suspend fun addHydrationReminder(timeLabel: String, hour24: Int, minute: Int) {
        dao.insertHydrationReminder(
            HydrationReminderEntity(
                timeLabel = timeLabel,
                hour24 = hour24,
                minute = minute,
                isEnabled = true,
                completedToday = false
            )
        )
    }

    suspend fun deleteHydrationReminder(id: Int) {
        dao.deleteHydrationReminder(id)
    }

    suspend fun saveConfidenceEntry(
        moodEmoji: String,
        moodLabel: String,
        proudOfText: String,
        helpedFeelGoodText: String
    ) {
        val today = todayDateString()
        dao.insertConfidenceEntry(
            ConfidenceEntryEntity(
                dateString = today,
                moodEmoji = moodEmoji,
                moodLabel = moodLabel,
                proudOfText = proudOfText.trim(),
                helpedFeelGoodText = helpedFeelGoodText.trim()
            )
        )
        dao.updateHabitByCategory("CONFIDENCE", true, "Checked in: $moodEmoji $moodLabel")
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(confidenceCheckInCompleted = true))
    }

    suspend fun markSleepWindDownCompleted() {
        val today = todayDateString()
        dao.updateHabitByCategory("SLEEP", true, "Wind-down routine complete 🌙")
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(sleepWindDownCompleted = true))
    }

    suspend fun acknowledgeMissedBedtime() {
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(missedBedtimeAcknowledged = true))
    }

    suspend fun resetTodaysHabits() {
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        dao.resetAllHabitsForToday()
        dao.resetSkincareForToday()
        dao.resetHydrationForToday()
        dao.updateHabitByCategory("HYDRATE", false, "Next reminder in 42 min")
        dao.updateHabitByCategory("MOVE", false, "${prefs.dailyTimeCommitmentMinutes} min mobility")
        dao.updateHabitByCategory("GLOW", false, "Morning & evening care")
        dao.updateHabitByCategory("SLEEP", false, "Wind-down at ${prefs.windDownTime}")
        dao.updateHabitByCategory("CONFIDENCE", false, "Evening check-in")
        dao.saveDailyProgress(DailyProgressEntity(dateString = todayDateString()))
    }

    suspend fun buildExportDataSummary(): String {
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val habits = dao.getAllHabitsOnce()
        val entries = dao.getConfidenceEntriesOnce()
        val history = dao.getAllProgressHistoryOnce()

        return buildString {
            appendLine("=== GlowUp Personal Wellness Export ===")
            appendLine("Take care of yourself. Feel good in yourself.")
            appendLine("Name: ${prefs.userName}")
            appendLine("Focus Areas: ${prefs.selectedFocusAreas}")
            appendLine("Preferred Time: ${prefs.freeTimeWindow} (${prefs.dailyTimeCommitmentMinutes} min)")
            appendLine("Sleep Schedule: Wake ${prefs.wakeTime} | Wind-down ${prefs.windDownTime} | Bedtime ${prefs.targetBedtime}")
            appendLine("Next Week Focus: ${prefs.nextWeekFocus}")
            appendLine()
            appendLine("--- Today's Habits ---")
            habits.forEach { h ->
                appendLine("${if (h.completedToday) "[x]" else "[ ]"} ${h.emoji} ${h.title} (${h.subtitle})")
            }
            appendLine()
            appendLine("--- Confidence Reflections (${entries.size}) ---")
            entries.take(10).forEach { e ->
                appendLine("${e.dateString} - ${e.moodEmoji} ${e.moodLabel}")
                if (e.proudOfText.isNotBlank()) appendLine("  Proud of: ${e.proudOfText}")
                if (e.helpedFeelGoodText.isNotBlank()) appendLine("  Helped me feel good: ${e.helpedFeelGoodText}")
            }
            appendLine()
            appendLine("--- Weekly Progress Days Logged: ${history.size} ---")
        }
    }

    suspend fun deleteAllUserData() {
        dao.clearUserPreferences()
        dao.clearDailyHabits()
        dao.clearSkincareSteps()
        dao.clearHydrationReminders()
        dao.clearConfidenceEntries()
        dao.clearProgressHistory()
        initializeDefaultsIfNeeded()
    }
}
