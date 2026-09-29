package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subjects",
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
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val examId: Long,
    val name: String,
    val iconName: String = "menu_book",
    val colorHex: String = "#1A73E8",
    val sortOrder: Int = 0,
    val isCustom: Boolean = false,
    val isExcludedFromPlan: Boolean = false
)
