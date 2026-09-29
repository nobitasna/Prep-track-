package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects WHERE examId = :examId ORDER BY sortOrder ASC, id ASC")
    fun getSubjectsForExam(examId: Long): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE examId = :examId ORDER BY sortOrder ASC, id ASC")
    suspend fun getSubjectsForExamSync(examId: Long): List<SubjectEntity>

    @Query("SELECT * FROM subjects WHERE id = :id LIMIT 1")
    suspend fun getSubjectById(id: Long): SubjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>): List<Long>

    @Update
    suspend fun updateSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)

    @Query("DELETE FROM subjects WHERE id = :subjectId")
    suspend fun deleteSubjectById(subjectId: Long)

    @Query("UPDATE subjects SET sortOrder = :order WHERE id = :id")
    suspend fun updateSortOrder(id: Long, order: Int)
}
