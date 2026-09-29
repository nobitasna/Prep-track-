package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_plans",
    foreignKeys = [
        ForeignKey(
            entity = ExamGoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["examId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("examId")]
)
data class StudyPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val examId: Long,
    val planScope: String = "ENTIRE", // "ENTIRE", "SELECTED"
    val finishMode: String = "DAYS", // "DAYS", "DATE"
    val targetDays: Int = 90,
    val targetDateTimestamp: Long = 0L,
    val playbackSpeed: Float = 1.0f, // 1.0, 1.25, 1.5, 1.75, 2.0
    val notesMinutesPerLecture: Int = 30, // 0, 15, 30, 45, 60
    val practiceMinutesDaily: Int = 60, // 0, 30, 60, 90, 120, 180
    val revisionMinutesDaily: Int = 30, // 0, 30, 60
    val studyPattern: String = "WEEKLY_TIMETABLE", // "WEEKLY_TIMETABLE", "ONE_BY_ONE"
    val missedWorkloadHoursAdjustment: Float = 0f,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)
