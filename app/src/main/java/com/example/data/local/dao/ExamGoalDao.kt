package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ExamGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamGoalDao {
    @Query("SELECT * FROM exam_goals WHERE isActive = 1 LIMIT 1")
    fun getActiveGoal(): Flow<ExamGoalEntity?>

    @Query("SELECT * FROM exam_goals WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveGoalSync(): ExamGoalEntity?

    @Query("SELECT * FROM exam_goals ORDER BY createdAtTimestamp DESC")
    fun getAllGoals(): Flow<List<ExamGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: ExamGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: ExamGoalEntity)

    @Query("UPDATE exam_goals SET isActive = 0")
    suspend fun deactivateAllGoals()

    @Query("UPDATE exam_goals SET isActive = 1 WHERE id = :goalId")
    suspend fun activateGoal(goalId: Long)
}
