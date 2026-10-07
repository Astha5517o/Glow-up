package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Astha",
    val customGreeting: String = "",
    val onboardingCompleted: Boolean = false,
    val selectedFocusAreas: String = "Move more,Stay hydrated,Build a skincare routine,Sleep better,Feel more confident",
    val freeTimeWindow: String = "Evening",
    val dailyTimeCommitmentMinutes: Int = 10,
    val wakeTime: String = "7:00 AM",
    val targetBedtime: String = "10:00 PM",
    val windDownTime: String = "9:30 PM",
    val studyResetEnabled: Boolean = true,
    val studyResetIntervalMinutes: Int = 45,
    val hydrationRemindersEnabled: Boolean = true,
    val hydrationPaused: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val morningNotificationEnabled: Boolean = true,
    val movementNotificationEnabled: Boolean = true,
    val skincareNotificationEnabled: Boolean = true,
    val sleepNotificationEnabled: Boolean = true,
    val confidenceNotificationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val nextWeekFocus: String = "Be kinder to myself",
    val lastActiveDate: String = ""
)

@Entity(tableName = "daily_habits")
data class DailyHabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // HYDRATE, MOVE, GLOW, SLEEP, CONFIDENCE, CUSTOM
    val title: String,
    val subtitle: String,
    val emoji: String,
    val completedToday: Boolean = false,
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)

@Entity(tableName = "skincare_steps")
data class SkincareStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val period: String, // MORNING, EVENING
    val title: String,
    val completedToday: Boolean = false,
    val sortOrder: Int = 0
)

@Entity(tableName = "hydration_reminders")
data class HydrationReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timeLabel: String, // e.g., "8:00 AM"
    val hour24: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val completedToday: Boolean = false
)

@Entity(tableName = "confidence_entries")
data class ConfidenceEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateString: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val moodEmoji: String,
    val moodLabel: String, // Really good, Good, Okay, Difficult
    val proudOfText: String,
    val helpedFeelGoodText: String
)

@Entity(tableName = "daily_progress_history")
data class DailyProgressEntity(
    @PrimaryKey val dateString: String, // YYYY-MM-DD
    val movementCompleted: Boolean = false,
    val hydrationCompletedCount: Int = 0,
    val morningSkincareCompleted: Boolean = false,
    val eveningSkincareCompleted: Boolean = false,
    val sleepWindDownCompleted: Boolean = false,
    val confidenceCheckInCompleted: Boolean = false,
    val missedBedtimeAcknowledged: Boolean = false
)

data class ExerciseStep(
    val name: String,
    val instruction: String,
    val durationSeconds: Int,
    val breathCue: String
)

data class MovementRoutine(
    val id: String,
    val title: String,
    val category: String, // MORNING, POSTURE, STRENGTH, RELAX
    val durationMinutes: Int,
    val difficulty: String, // Beginner, Gentle, All Levels
    val focusTags: String, // e.g. "Mobility • Posture"
    val description: String,
    val steps: List<ExerciseStep>
)

data class SleepTimelineItem(
    val time: String,
    val title: String,
    val subtitle: String,
    val isCompleted: Boolean
)

data class GeneratedRoutineBlock(
    val periodTitle: String,
    val emoji: String,
    val items: List<String>
)
