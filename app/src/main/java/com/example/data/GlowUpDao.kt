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

    // OS Tasks (Today's Top 3 + Priority Tasks)
    @Query("SELECT * FROM os_tasks ORDER BY isCompleted ASC, topThreeSlot ASC, sortOrder ASC, id ASC")
    fun getAllTasks(): Flow<List<OsTaskEntity>>

    @Query("SELECT * FROM os_tasks ORDER BY isCompleted ASC, topThreeSlot ASC, sortOrder ASC, id ASC")
    suspend fun getAllTasksOnce(): List<OsTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<OsTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: OsTaskEntity)

    @Update
    suspend fun updateTask(task: OsTaskEntity)

    @Query("DELETE FROM os_tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    @Query("DELETE FROM os_tasks")
    suspend fun clearAllTasks()

    // Timeline Blocks
    @Query("SELECT * FROM timeline_blocks ORDER BY sortOrder ASC, id ASC")
    fun getTimelineBlocks(): Flow<List<TimelineBlockEntity>>

    @Query("SELECT * FROM timeline_blocks ORDER BY sortOrder ASC, id ASC")
    suspend fun getTimelineBlocksOnce(): List<TimelineBlockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineBlocks(blocks: List<TimelineBlockEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineBlock(block: TimelineBlockEntity)

    @Update
    suspend fun updateTimelineBlock(block: TimelineBlockEntity)

    @Query("DELETE FROM timeline_blocks WHERE id = :id")
    suspend fun deleteTimelineBlock(id: Int)

    @Query("DELETE FROM timeline_blocks")
    suspend fun clearTimelineBlocks()

    // Study Chapters
    @Query("SELECT * FROM study_chapters ORDER BY lastStudiedTimestamp DESC, id ASC")
    fun getStudyChapters(): Flow<List<StudyChapterEntity>>

    @Query("SELECT * FROM study_chapters ORDER BY lastStudiedTimestamp DESC, id ASC")
    suspend fun getStudyChaptersOnce(): List<StudyChapterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyChapters(chapters: List<StudyChapterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyChapter(chapter: StudyChapterEntity)

    @Update
    suspend fun updateStudyChapter(chapter: StudyChapterEntity)

    @Query("DELETE FROM study_chapters")
    suspend fun clearStudyChapters()

    // Error Log
    @Query("SELECT * FROM error_log_entries ORDER BY isResolved ASC, createdTimestamp DESC")
    fun getErrorLogs(): Flow<List<ErrorLogEntity>>

    @Query("SELECT * FROM error_log_entries ORDER BY isResolved ASC, createdTimestamp DESC")
    suspend fun getErrorLogsOnce(): List<ErrorLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertErrorLogs(entries: List<ErrorLogEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertErrorLog(entry: ErrorLogEntity)

    @Update
    suspend fun updateErrorLog(entry: ErrorLogEntity)

    @Query("DELETE FROM error_log_entries WHERE id = :id")
    suspend fun deleteErrorLog(id: Int)

    @Query("DELETE FROM error_log_entries")
    suspend fun clearErrorLogs()

    // Projects (BUILD section)
    @Query("SELECT * FROM projects ORDER BY id ASC")
    fun getProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects ORDER BY id ASC")
    suspend fun getProjectsOnce(): List<ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: Int)

    @Query("DELETE FROM projects")
    suspend fun clearProjects()

    // Profile Achievements
    @Query("SELECT * FROM profile_achievements ORDER BY id DESC")
    fun getProfileAchievements(): Flow<List<ProfileAchievementEntity>>

    @Query("SELECT * FROM profile_achievements ORDER BY id DESC")
    suspend fun getProfileAchievementsOnce(): List<ProfileAchievementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileAchievements(items: List<ProfileAchievementEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileAchievement(item: ProfileAchievementEntity)

    @Query("DELETE FROM profile_achievements WHERE id = :id")
    suspend fun deleteProfileAchievement(id: Int)

    @Query("DELETE FROM profile_achievements")
    suspend fun clearProfileAchievements()

    // Opportunities / Scholarships
    @Query("SELECT * FROM opportunities ORDER BY id DESC")
    fun getOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities ORDER BY id DESC")
    suspend fun getOpportunitiesOnce(): List<OpportunityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(items: List<OpportunityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(item: OpportunityEntity)

    @Update
    suspend fun updateOpportunity(item: OpportunityEntity)

    @Query("DELETE FROM opportunities WHERE id = :id")
    suspend fun deleteOpportunity(id: Int)

    @Query("DELETE FROM opportunities")
    suspend fun clearOpportunities()

    // Brain Dump
    @Query("SELECT * FROM brain_dump_entries ORDER BY timestamp DESC")
    fun getBrainDumps(): Flow<List<BrainDumpEntity>>

    @Query("SELECT * FROM brain_dump_entries ORDER BY timestamp DESC")
    suspend fun getBrainDumpsOnce(): List<BrainDumpEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrainDump(entry: BrainDumpEntity)

    @Query("DELETE FROM brain_dump_entries WHERE id = :id")
    suspend fun deleteBrainDump(id: Int)

    @Query("DELETE FROM brain_dump_entries")
    suspend fun clearBrainDumps()

    // Distraction Logs
    @Query("SELECT * FROM distraction_logs ORDER BY timestamp DESC")
    fun getDistractionLogs(): Flow<List<DistractionLogEntity>>

    @Query("SELECT * FROM distraction_logs ORDER BY timestamp DESC")
    suspend fun getDistractionLogsOnce(): List<DistractionLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistractionLog(log: DistractionLogEntity)

    @Query("DELETE FROM distraction_logs")
    suspend fun clearDistractionLogs()

    // Daily Habits (Wellness)
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

    // Clear all tables
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

    // Calendar Events
    @Query("SELECT * FROM calendar_events ORDER BY dateString ASC, startTime ASC, id ASC")
    fun getCalendarEvents(): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events ORDER BY dateString ASC, startTime ASC, id ASC")
    suspend fun getCalendarEventsOnce(): List<CalendarEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarEvents(events: List<CalendarEventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarEvent(event: CalendarEventEntity)

    @Update
    suspend fun updateCalendarEvent(event: CalendarEventEntity)

    @Query("DELETE FROM calendar_events WHERE id = :id")
    suspend fun deleteCalendarEvent(id: Int)

    @Query("DELETE FROM calendar_events")
    suspend fun clearCalendarEvents()

    // Monthly Goals
    @Query("SELECT * FROM monthly_goals ORDER BY isCompleted ASC, id ASC")
    fun getMonthlyGoals(): Flow<List<MonthlyGoalEntity>>

    @Query("SELECT * FROM monthly_goals ORDER BY isCompleted ASC, id ASC")
    suspend fun getMonthlyGoalsOnce(): List<MonthlyGoalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyGoals(goals: List<MonthlyGoalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyGoal(goal: MonthlyGoalEntity)

    @Update
    suspend fun updateMonthlyGoal(goal: MonthlyGoalEntity)

    @Query("DELETE FROM monthly_goals WHERE id = :id")
    suspend fun deleteMonthlyGoal(id: Int)

    @Query("DELETE FROM monthly_goals")
    suspend fun clearMonthlyGoals()

    // Long-Term Goals (My Big Picture)
    @Query("SELECT * FROM long_term_goals ORDER BY id ASC")
    fun getLongTermGoals(): Flow<List<LongTermGoalEntity>>

    @Query("SELECT * FROM long_term_goals ORDER BY id ASC")
    suspend fun getLongTermGoalsOnce(): List<LongTermGoalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLongTermGoals(goals: List<LongTermGoalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLongTermGoal(goal: LongTermGoalEntity)

    @Update
    suspend fun updateLongTermGoal(goal: LongTermGoalEntity)

    @Query("DELETE FROM long_term_goals")
    suspend fun clearLongTermGoals()

    // Recent Wins
    @Query("SELECT * FROM recent_wins ORDER BY timestamp DESC LIMIT 12")
    fun getRecentWins(): Flow<List<RecentWinEntity>>

    @Query("SELECT * FROM recent_wins ORDER BY timestamp DESC")
    suspend fun getRecentWinsOnce(): List<RecentWinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentWins(wins: List<RecentWinEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentWin(win: RecentWinEntity)

    @Query("DELETE FROM recent_wins")
    suspend fun clearRecentWins()

    // Special Day Plans ("MY DAY, MY WAY")
    @Query("SELECT * FROM special_day_plans ORDER BY dateString ASC")
    fun getSpecialDayPlans(): Flow<List<SpecialDayPlanEntity>>

    @Query("SELECT * FROM special_day_plans ORDER BY dateString ASC")
    suspend fun getSpecialDayPlansOnce(): List<SpecialDayPlanEntity>

    @Query("SELECT * FROM special_day_plans WHERE dateString = :dateString")
    suspend fun getSpecialDayPlanForDate(dateString: String): SpecialDayPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSpecialDayPlan(plan: SpecialDayPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecialDayPlans(plans: List<SpecialDayPlanEntity>)

    @Query("DELETE FROM special_day_plans WHERE dateString = :dateString")
    suspend fun deleteSpecialDayPlan(dateString: String)

    @Query("DELETE FROM special_day_plans")
    suspend fun clearSpecialDayPlans()

    // Event Preparation Milestones
    @Query("SELECT * FROM event_prep_milestones ORDER BY targetDateString ASC, id ASC")
    fun getEventPrepMilestones(): Flow<List<EventPrepMilestoneEntity>>

    @Query("SELECT * FROM event_prep_milestones ORDER BY targetDateString ASC, id ASC")
    suspend fun getEventPrepMilestonesOnce(): List<EventPrepMilestoneEntity>

    @Query("SELECT * FROM event_prep_milestones WHERE eventId = :eventId ORDER BY id ASC")
    suspend fun getPrepMilestonesForEventOnce(eventId: Int): List<EventPrepMilestoneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventPrepMilestones(milestones: List<EventPrepMilestoneEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventPrepMilestone(milestone: EventPrepMilestoneEntity)

    @Update
    suspend fun updateEventPrepMilestone(milestone: EventPrepMilestoneEntity)

    @Query("DELETE FROM event_prep_milestones WHERE eventId = :eventId")
    suspend fun deletePrepMilestonesForEvent(eventId: Int)

    @Query("DELETE FROM event_prep_milestones")
    suspend fun clearEventPrepMilestones()

    // End of Day Reflections ("CLOSE THE DAY")
    @Query("SELECT * FROM end_of_day_reflections ORDER BY dateString DESC")
    fun getEndOfDayReflections(): Flow<List<EndOfDayReflectionEntity>>

    @Query("SELECT * FROM end_of_day_reflections ORDER BY dateString DESC")
    suspend fun getEndOfDayReflectionsOnce(): List<EndOfDayReflectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEndOfDayReflection(reflection: EndOfDayReflectionEntity)

    @Query("DELETE FROM end_of_day_reflections")
    suspend fun clearEndOfDayReflections()

    // Setback Recovery ("LEARN WITHOUT LOSING SELF-RESPECT")
    @Query("SELECT * FROM setback_recoveries ORDER BY dateString DESC, id DESC")
    fun getSetbackRecoveries(): Flow<List<SetbackRecoveryEntity>>

    @Query("SELECT * FROM setback_recoveries ORDER BY dateString DESC, id DESC")
    suspend fun getSetbackRecoveriesOnce(): List<SetbackRecoveryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetbackRecovery(entry: SetbackRecoveryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetbackRecoveries(entries: List<SetbackRecoveryEntity>)

    @Update
    suspend fun updateSetbackRecovery(entry: SetbackRecoveryEntity)

    @Query("DELETE FROM setback_recoveries WHERE id = :id")
    suspend fun deleteSetbackRecovery(id: Int)

    @Query("DELETE FROM setback_recoveries")
    suspend fun clearSetbackRecoveries()

    // Financial Experiments ("SKILLS TO INCOME & VALUE CREATION")
    @Query("SELECT * FROM financial_experiments ORDER BY id DESC")
    fun getFinancialExperiments(): Flow<List<FinancialExperimentEntity>>

    @Query("SELECT * FROM financial_experiments ORDER BY id DESC")
    suspend fun getFinancialExperimentsOnce(): List<FinancialExperimentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancialExperiment(entry: FinancialExperimentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancialExperiments(entries: List<FinancialExperimentEntity>)

    @Update
    suspend fun updateFinancialExperiment(entry: FinancialExperimentEntity)

    @Query("DELETE FROM financial_experiments WHERE id = :id")
    suspend fun deleteFinancialExperiment(id: Int)

    @Query("DELETE FROM financial_experiments")
    suspend fun clearFinancialExperiments()

    // 1. Academic Topics (Academic Excellence Engine & Chemistry Mission)
    @Query("SELECT * FROM academic_topics ORDER BY subject ASC, chapterName ASC, id ASC")
    fun getAcademicTopics(): Flow<List<AcademicTopicEntity>>

    @Query("SELECT * FROM academic_topics ORDER BY subject ASC, chapterName ASC, id ASC")
    suspend fun getAcademicTopicsOnce(): List<AcademicTopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAcademicTopic(topic: AcademicTopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAcademicTopics(topics: List<AcademicTopicEntity>)

    @Update
    suspend fun updateAcademicTopic(topic: AcademicTopicEntity)

    @Query("DELETE FROM academic_topics WHERE id = :id")
    suspend fun deleteAcademicTopic(id: Int)

    @Query("DELETE FROM academic_topics")
    suspend fun clearAcademicTopics()

    // 2. Science Exhibition & Innovation Scorecards
    @Query("SELECT * FROM science_exhibition_scorecards ORDER BY isSelectedForExecution DESC, id DESC")
    fun getScienceExhibitionScorecards(): Flow<List<ScienceExhibitionScorecardEntity>>

    @Query("SELECT * FROM science_exhibition_scorecards ORDER BY isSelectedForExecution DESC, id DESC")
    suspend fun getScienceExhibitionScorecardsOnce(): List<ScienceExhibitionScorecardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScienceExhibitionScorecard(card: ScienceExhibitionScorecardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScienceExhibitionScorecards(cards: List<ScienceExhibitionScorecardEntity>)

    @Update
    suspend fun updateScienceExhibitionScorecard(card: ScienceExhibitionScorecardEntity)

    @Query("DELETE FROM science_exhibition_scorecards WHERE id = :id")
    suspend fun deleteScienceExhibitionScorecard(id: Int)

    @Query("DELETE FROM science_exhibition_scorecards")
    suspend fun clearScienceExhibitionScorecards()

    // 3. Scholarship & International University Strategies
    @Query("SELECT * FROM scholarship_strategies ORDER BY isMextSpecific DESC, isFullyFunded DESC, id ASC")
    fun getScholarshipStrategies(): Flow<List<ScholarshipStrategyEntity>>

    @Query("SELECT * FROM scholarship_strategies ORDER BY isMextSpecific DESC, isFullyFunded DESC, id ASC")
    suspend fun getScholarshipStrategiesOnce(): List<ScholarshipStrategyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScholarshipStrategy(strategy: ScholarshipStrategyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScholarshipStrategies(strategies: List<ScholarshipStrategyEntity>)

    @Update
    suspend fun updateScholarshipStrategy(strategy: ScholarshipStrategyEntity)

    @Query("DELETE FROM scholarship_strategies WHERE id = :id")
    suspend fun deleteScholarshipStrategy(id: Int)

    @Query("DELETE FROM scholarship_strategies")
    suspend fun clearScholarshipStrategies()

    // 4. Best, Baseline & Setback Scenario Plans
    @Query("SELECT * FROM scenario_plans ORDER BY id ASC")
    fun getScenarioPlans(): Flow<List<ScenarioPlanEntity>>

    @Query("SELECT * FROM scenario_plans ORDER BY id ASC")
    suspend fun getScenarioPlansOnce(): List<ScenarioPlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenarioPlan(plan: ScenarioPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenarioPlans(plans: List<ScenarioPlanEntity>)

    @Update
    suspend fun updateScenarioPlan(plan: ScenarioPlanEntity)

    @Query("DELETE FROM scenario_plans WHERE id = :id")
    suspend fun deleteScenarioPlan(id: Int)

    @Query("DELETE FROM scenario_plans")
    suspend fun clearScenarioPlans()

    // 5. Profile Evidence Ledger
    @Query("SELECT * FROM profile_evidence_ledger ORDER BY isExternallyVerified DESC, id DESC")
    fun getProfileEvidenceLedger(): Flow<List<ProfileEvidenceLedgerEntity>>

    @Query("SELECT * FROM profile_evidence_ledger ORDER BY isExternallyVerified DESC, id DESC")
    suspend fun getProfileEvidenceLedgerOnce(): List<ProfileEvidenceLedgerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileEvidence(item: ProfileEvidenceLedgerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileEvidenceItems(items: List<ProfileEvidenceLedgerEntity>)

    @Update
    suspend fun updateProfileEvidence(item: ProfileEvidenceLedgerEntity)

    @Query("DELETE FROM profile_evidence_ledger WHERE id = :id")
    suspend fun deleteProfileEvidence(id: Int)

    @Query("DELETE FROM profile_evidence_ledger")
    suspend fun clearProfileEvidenceLedger()

    // 6. Anger & Overwhelm Pauses
    @Query("SELECT * FROM anger_overwhelm_pauses ORDER BY timestamp DESC")
    fun getAngerOverwhelmPauses(): Flow<List<AngerOverwhelmPauseEntity>>

    @Query("SELECT * FROM anger_overwhelm_pauses ORDER BY timestamp DESC")
    suspend fun getAngerOverwhelmPausesOnce(): List<AngerOverwhelmPauseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAngerOverwhelmPause(entry: AngerOverwhelmPauseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAngerOverwhelmPauses(entries: List<AngerOverwhelmPauseEntity>)

    @Query("DELETE FROM anger_overwhelm_pauses WHERE id = :id")
    suspend fun deleteAngerOverwhelmPause(id: Int)

    @Query("DELETE FROM anger_overwhelm_pauses")
    suspend fun clearAngerOverwhelmPauses()
}


