package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.FocusSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSettingsDao {
    @Query("SELECT * FROM focus_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<FocusSettingsEntity?>

    @Query("SELECT * FROM focus_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsSync(): FocusSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: FocusSettingsEntity)

    @Update
    suspend fun update(settings: FocusSettingsEntity)

    @Query("""
        UPDATE focus_settings 
        SET isFocusModeActive = :isActive, 
            isPaused = :isPaused, 
            remainingSeconds = :remainingSeconds, 
            targetEndTimeTimestamp = :targetEndTime,
            lastUpdatedTimestamp = :updatedAt 
        WHERE id = 1
    """)
    suspend fun updateTimerState(
        isActive: Boolean,
        isPaused: Boolean,
        remainingSeconds: Int,
        targetEndTime: Long?,
        updatedAt: Long = System.currentTimeMillis()
    )
}
