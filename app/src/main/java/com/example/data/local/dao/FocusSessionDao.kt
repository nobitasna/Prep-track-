package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAtTimestamp DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE completedAtTimestamp >= :sinceTimestamp ORDER BY completedAtTimestamp DESC")
    fun getSessionsSince(sinceTimestamp: Long): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE completedAtTimestamp >= :sinceTimestamp ORDER BY completedAtTimestamp DESC")
    suspend fun getSessionsSinceSync(sinceTimestamp: Long): List<FocusSessionEntity>

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE sessionType = 'FOCUS'")
    fun getTotalFocusMinutes(): Flow<Long?>

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE sessionType = 'FOCUS' AND completedAtTimestamp >= :sinceTimestamp")
    fun getFocusMinutesSince(sinceTimestamp: Long): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long
}
