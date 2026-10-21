package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserPreferencesEntity::class,
        OsTaskEntity::class,
        TimelineBlockEntity::class,
        StudyChapterEntity::class,
        ErrorLogEntity::class,
        ProjectEntity::class,
        ProfileAchievementEntity::class,
        OpportunityEntity::class,
        BrainDumpEntity::class,
        DistractionLogEntity::class,
        DailyHabitEntity::class,
        SkincareStepEntity::class,
        HydrationReminderEntity::class,
        ConfidenceEntryEntity::class,
        DailyProgressEntity::class,
        CalendarEventEntity::class,
        MonthlyGoalEntity::class,
        LongTermGoalEntity::class,
        RecentWinEntity::class,
        SpecialDayPlanEntity::class,
        EventPrepMilestoneEntity::class,
        EndOfDayReflectionEntity::class,
        SetbackRecoveryEntity::class,
        FinancialExperimentEntity::class,
        AcademicTopicEntity::class,
        ScienceExhibitionScorecardEntity::class,
        ScholarshipStrategyEntity::class,
        ScenarioPlanEntity::class,
        ProfileEvidenceLedgerEntity::class,
        AngerOverwhelmPauseEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class GlowUpDatabase : RoomDatabase() {
    abstract fun glowUpDao(): GlowUpDao

    companion object {
        @Volatile
        private var INSTANCE: GlowUpDatabase? = null

        fun getDatabase(context: Context): GlowUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GlowUpDatabase::class.java,
                    "glowup_wellness_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
