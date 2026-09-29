package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_streak")
data class UserStreakEntity(
    @PrimaryKey
    val id: Long = 1L,
    val currentStreak: Int = 1,
    val longestStreak: Int = 1,
    val lastStudyDateString: String = "", // e.g. "2026-09-28"
    val totalFocusMinutes: Long = 0L,
    val totalLecturesCompleted: Int = 0
)
