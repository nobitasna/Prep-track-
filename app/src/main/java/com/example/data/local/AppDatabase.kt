package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.ChapterDao
import com.example.data.local.dao.ExamGoalDao
import com.example.data.local.dao.FocusAllowedAppDao
import com.example.data.local.dao.FocusSessionDao
import com.example.data.local.dao.FocusSettingsDao
import com.example.data.local.dao.LectureDao
import com.example.data.local.dao.StudyPlanDao
import com.example.data.local.dao.SubjectDao
import com.example.data.local.dao.UserStreakDao
import com.example.data.local.dao.WeeklyTimetableDao
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.ExamGoalEntity
import com.example.data.local.entity.FocusAllowedAppEntity
import com.example.data.local.entity.FocusSessionEntity
import com.example.data.local.entity.FocusSettingsEntity
import com.example.data.local.entity.LectureEntity
import com.example.data.local.entity.StudyPlanEntity
import com.example.data.local.entity.SubjectEntity
import com.example.data.local.entity.UserStreakEntity
import com.example.data.local.entity.WeeklyTimetableEntryEntity

@Database(
    entities = [
        ExamGoalEntity::class,
        SubjectEntity::class,
        ChapterEntity::class,
        LectureEntity::class,
        StudyPlanEntity::class,
        WeeklyTimetableEntryEntity::class,
        FocusSessionEntity::class,
        FocusSettingsEntity::class,
        FocusAllowedAppEntity::class,
        UserStreakEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun examGoalDao(): ExamGoalDao
    abstract fun subjectDao(): SubjectDao
    abstract fun chapterDao(): ChapterDao
    abstract fun lectureDao(): LectureDao
    abstract fun studyPlanDao(): StudyPlanDao
    abstract fun weeklyTimetableDao(): WeeklyTimetableDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun focusSettingsDao(): FocusSettingsDao
    abstract fun focusAllowedAppDao(): FocusAllowedAppDao
    abstract fun userStreakDao(): UserStreakDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN goalId INTEGER DEFAULT NULL") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN taskId INTEGER DEFAULT NULL") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN subjectId INTEGER DEFAULT NULL") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN startTime INTEGER NOT NULL DEFAULT 0") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN endTime INTEGER NOT NULL DEFAULT 0") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN pausedDuration INTEGER NOT NULL DEFAULT 0") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN activeDuration INTEGER NOT NULL DEFAULT 0") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN status TEXT NOT NULL DEFAULT 'COMPLETED'") } catch (_: Exception) {}
                try { db.execSQL("ALTER TABLE focus_sessions ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0") } catch (_: Exception) {}

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS focus_settings (
                        id INTEGER PRIMARY KEY NOT NULL,
                        isFocusModeActive INTEGER NOT NULL,
                        isPaused INTEGER NOT NULL,
                        pauseRemainingSeconds INTEGER NOT NULL,
                        pausedAtTimestamp INTEGER,
                        activeSessionId INTEGER,
                        targetEndTimeTimestamp INTEGER,
                        totalSeconds INTEGER NOT NULL,
                        remainingSeconds INTEGER NOT NULL,
                        isBreak INTEGER NOT NULL,
                        selectedGoalId INTEGER,
                        selectedSubjectId INTEGER,
                        selectedSubjectName TEXT NOT NULL,
                        selectedChapterName TEXT NOT NULL,
                        selectedTopicName TEXT NOT NULL,
                        distractionShieldEnabled INTEGER NOT NULL,
                        strictModeEnabled INTEGER NOT NULL,
                        ambientSoundType TEXT NOT NULL,
                        keepScreenOn INTEGER NOT NULL,
                        lastUpdatedTimestamp INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS focus_allowed_apps (
                        packageName TEXT PRIMARY KEY NOT NULL,
                        appName TEXT NOT NULL,
                        isEnabled INTEGER NOT NULL,
                        category TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "preptrack_database.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
