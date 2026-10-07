package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserPreferencesEntity::class,
        DailyHabitEntity::class,
        SkincareStepEntity::class,
        HydrationReminderEntity::class,
        ConfidenceEntryEntity::class,
        DailyProgressEntity::class
    ],
    version = 1,
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
