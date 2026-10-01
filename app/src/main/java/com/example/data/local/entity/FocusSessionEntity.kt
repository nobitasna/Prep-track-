package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val goalId: Long? = null,
    val taskId: Long? = null,
    val subjectId: Long? = null,
    val subjectName: String = "",
    val chapterName: String = "",
    val topicName: String = "",
    val durationMinutes: Int = 0,
    val sessionType: String = "FOCUS", // "FOCUS", "BREAK"
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val pausedDuration: Long = 0L, // in seconds
    val activeDuration: Long = 0L, // in seconds
    val status: String = "COMPLETED", // "IN_PROGRESS", "PAUSED", "COMPLETED", "ABANDONED"
    val completedAtTimestamp: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)
