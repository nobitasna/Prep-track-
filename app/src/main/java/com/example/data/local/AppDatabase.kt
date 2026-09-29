package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ChapterDao
import com.example.data.local.dao.ExamGoalDao
import com.example.data.local.dao.FocusSessionDao
import com.example.data.local.dao.LectureDao
import com.example.data.local.dao.StudyPlanDao
import com.example.data.local.dao.SubjectDao
import com.example.data.local.dao.UserStreakDao
import com.example.data.local.dao.WeeklyTimetableDao
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.ExamGoalEntity
import com.example.data.local.entity.FocusSessionEntity
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
        UserStreakEntity::class
    ],
    version = 1,
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
    abstract fun userStreakDao(): UserStreakDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "preptrack_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
