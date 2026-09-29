package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.StudyPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyPlanDao {
    @Query("SELECT * FROM study_plans WHERE examId = :examId LIMIT 1")
    fun getPlanForExam(examId: Long): Flow<StudyPlanEntity?>

    @Query("SELECT * FROM study_plans WHERE examId = :examId LIMIT 1")
    suspend fun getPlanForExamSync(examId: Long): StudyPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: StudyPlanEntity): Long

    @Update
    suspend fun updatePlan(plan: StudyPlanEntity)

    @Query("DELETE FROM study_plans WHERE examId = :examId")
    suspend fun deletePlanForExam(examId: Long)
}
