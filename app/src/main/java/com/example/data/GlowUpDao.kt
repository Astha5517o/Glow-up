package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GlowUpDao {
    // User Preferences
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun getUserPreferencesOnce(): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(prefs: UserPreferencesEntity)

    // Daily Habits
    @Query("SELECT * FROM daily_habits WHERE isActive = 1 ORDER BY sortOrder ASC, id ASC")
    fun getActiveHabits(): Flow<List<DailyHabitEntity>>

    @Query("SELECT * FROM daily_habits ORDER BY sortOrder ASC, id ASC")
    suspend fun getAllHabitsOnce(): List<DailyHabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<DailyHabitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: DailyHabitEntity)

    @Update
    suspend fun updateHabit(habit: DailyHabitEntity)

    @Query("DELETE FROM daily_habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)

    @Query("UPDATE daily_habits SET completedToday = 0")
    suspend fun resetAllHabitsForToday()

    @Query("UPDATE daily_habits SET completedToday = :completed, subtitle = :subtitle WHERE category = :category")
    suspend fun updateHabitByCategory(category: String, completed: Boolean, subtitle: String)

    // Skincare Steps
    @Query("SELECT * FROM skincare_steps ORDER BY sortOrder ASC, id ASC")
    fun getSkincareSteps(): Flow<List<SkincareStepEntity>>

    @Query("SELECT * FROM skincare_steps ORDER BY sortOrder ASC, id ASC")
    suspend fun getSkincareStepsOnce(): List<SkincareStepEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkincareSteps(steps: List<SkincareStepEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkincareStep(step: SkincareStepEntity)

    @Update
    suspend fun updateSkincareStep(step: SkincareStepEntity)

    @Query("DELETE FROM skincare_steps WHERE id = :id")
    suspend fun deleteSkincareStep(id: Int)

    @Query("UPDATE skincare_steps SET completedToday = 0")
    suspend fun resetSkincareForToday()

    // Hydration Reminders
    @Query("SELECT * FROM hydration_reminders ORDER BY hour24 ASC, minute ASC")
    fun getHydrationReminders(): Flow<List<HydrationReminderEntity>>

    @Query("SELECT * FROM hydration_reminders ORDER BY hour24 ASC, minute ASC")
    suspend fun getHydrationRemindersOnce(): List<HydrationReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHydrationReminders(reminders: List<HydrationReminderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHydrationReminder(reminder: HydrationReminderEntity)

    @Update
    suspend fun updateHydrationReminder(reminder: HydrationReminderEntity)

    @Query("DELETE FROM hydration_reminders WHERE id = :id")
    suspend fun deleteHydrationReminder(id: Int)

    @Query("UPDATE hydration_reminders SET completedToday = 0")
    suspend fun resetHydrationForToday()

    // Confidence Entries
    @Query("SELECT * FROM confidence_entries ORDER BY timestamp DESC")
    fun getConfidenceEntries(): Flow<List<ConfidenceEntryEntity>>

    @Query("SELECT * FROM confidence_entries ORDER BY timestamp DESC")
    suspend fun getConfidenceEntriesOnce(): List<ConfidenceEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfidenceEntry(entry: ConfidenceEntryEntity)

    // Daily Progress History
    @Query("SELECT * FROM daily_progress_history ORDER BY dateString DESC LIMIT 7")
    fun getRecentProgressHistory(): Flow<List<DailyProgressEntity>>

    @Query("SELECT * FROM daily_progress_history ORDER BY dateString DESC")
    suspend fun getAllProgressHistoryOnce(): List<DailyProgressEntity>

    @Query("SELECT * FROM daily_progress_history WHERE dateString = :dateString")
    suspend fun getProgressForDate(dateString: String): DailyProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyProgress(progress: DailyProgressEntity)

    // Clear all tables for "Delete my data"
    @Query("DELETE FROM user_preferences")
    suspend fun clearUserPreferences()

    @Query("DELETE FROM daily_habits")
    suspend fun clearDailyHabits()

    @Query("DELETE FROM skincare_steps")
    suspend fun clearSkincareSteps()

    @Query("DELETE FROM hydration_reminders")
    suspend fun clearHydrationReminders()

    @Query("DELETE FROM confidence_entries")
    suspend fun clearConfidenceEntries()

    @Query("DELETE FROM daily_progress_history")
    suspend fun clearProgressHistory()
}
