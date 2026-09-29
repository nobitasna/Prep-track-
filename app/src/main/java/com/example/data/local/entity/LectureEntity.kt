package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lectures",
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("chapterId")]
)
data class LectureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chapterId: Long,
    val lectureNumber: Int,
    val title: String = "",
    val isCompleted: Boolean = false,
    val isImportant: Boolean = false,
    val notesCompleted: Boolean = false,
    val practiceCompleted: Boolean = false,
    val personalNote: String = "",
    val completedAtTimestamp: Long = 0L
) {
    companion object {
        // CRITICAL: Fixed 2-hour lecture reference duration that NEVER changes
        const val FIXED_REFERENCE_HOURS = 2.0
        const val FIXED_REFERENCE_MINUTES = 120
    }
}
