package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY sortOrder ASC, id ASC")
    fun getChaptersForSubject(subjectId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY sortOrder ASC, id ASC")
    suspend fun getChaptersForSubjectSync(subjectId: Long): List<ChapterEntity>

    @Query("SELECT * FROM chapters WHERE id = :id LIMIT 1")
    suspend fun getChapterById(id: Long): ChapterEntity?

    @Query("""
        SELECT c.* FROM chapters c 
        INNER JOIN subjects s ON c.subjectId = s.id 
        WHERE s.examId = :examId 
        ORDER BY s.sortOrder ASC, c.sortOrder ASC, c.id ASC
    """)
    fun getAllChaptersForExam(examId: Long): Flow<List<ChapterEntity>>

    @Query("""
        SELECT c.* FROM chapters c 
        INNER JOIN subjects s ON c.subjectId = s.id 
        WHERE s.examId = :examId 
        ORDER BY s.sortOrder ASC, c.sortOrder ASC, c.id ASC
    """)
    suspend fun getAllChaptersForExamSync(examId: Long): List<ChapterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>): List<Long>

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Delete
    suspend fun deleteChapter(chapter: ChapterEntity)

    @Query("UPDATE chapters SET isHidden = :hidden WHERE id = :id")
    suspend fun setChapterHidden(id: Long, hidden: Boolean)

    @Query("UPDATE chapters SET isImportant = NOT isImportant WHERE id = :id")
    suspend fun toggleChapterImportant(id: Long)

    @Query("UPDATE chapters SET isExcludedFromPlan = :excluded WHERE id = :id")
    suspend fun setChapterExcluded(id: Long, excluded: Boolean)

    @Query("UPDATE chapters SET sortOrder = :order WHERE id = :id")
    suspend fun updateSortOrder(id: Long, order: Int)
}
