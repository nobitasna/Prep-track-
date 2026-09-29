package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_goals")
data class ExamGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // School / Board, Engineering, Medical, University, Defence, Government Exams, Custom Goal
    val examName: String, // e.g. NEET 2027, JEE Main 2027, CBSE Class 10
    val targetYear: String, // e.g. 2027, 2026-27
    val targetDateTimestamp: Long = 0L,
    val isActive: Boolean = true,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)
