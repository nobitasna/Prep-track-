package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.FocusAllowedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusAllowedAppDao {
    @Query("SELECT * FROM focus_allowed_apps ORDER BY appName ASC")
    fun getAllAllowedApps(): Flow<List<FocusAllowedAppEntity>>

    @Query("SELECT * FROM focus_allowed_apps ORDER BY appName ASC")
    suspend fun getAllAllowedAppsSync(): List<FocusAllowedAppEntity>

    @Query("SELECT * FROM focus_allowed_apps WHERE isEnabled = 1 ORDER BY appName ASC")
    fun getEnabledAllowedApps(): Flow<List<FocusAllowedAppEntity>>

    @Query("SELECT * FROM focus_allowed_apps WHERE isEnabled = 1 ORDER BY appName ASC")
    suspend fun getEnabledAllowedAppsSync(): List<FocusAllowedAppEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(app: FocusAllowedAppEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(apps: List<FocusAllowedAppEntity>)

    @Query("UPDATE focus_allowed_apps SET isEnabled = :isEnabled, updatedAt = :updatedAt WHERE packageName = :packageName")
    suspend fun setAppEnabled(packageName: String, isEnabled: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM focus_allowed_apps WHERE packageName = :packageName")
    suspend fun delete(packageName: String)

    @Query("SELECT COUNT(*) FROM focus_allowed_apps")
    suspend fun getCount(): Int
}
