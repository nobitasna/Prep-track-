package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.WeeklyTimetableEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyTimetableDao {
    @Query("SELECT * FROM weekly_timetable WHERE planId = :planId ORDER BY dayOfWeek ASC")
    fun getTimetableForPlan(planId: Long): Flow<List<WeeklyTimetableEntryEntity>>

    @Query("SELECT * FROM weekly_timetable WHERE planId = :planId ORDER BY dayOfWeek ASC")
    suspend fun getTimetableForPlanSync(planId: Long): List<WeeklyTimetableEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<WeeklyTimetableEntryEntity>)

    @Query("DELETE FROM weekly_timetable WHERE planId = :planId")
    suspend fun deleteTimetableForPlan(planId: Long)
}
