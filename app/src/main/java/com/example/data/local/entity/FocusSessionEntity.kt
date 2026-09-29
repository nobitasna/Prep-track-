package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectName: String,
    val chapterName: String = "",
    val topicName: String = "",
    val durationMinutes: Int,
    val sessionType: String = "FOCUS", // "FOCUS", "BREAK"
    val completedAtTimestamp: Long = System.currentTimeMillis()
)
