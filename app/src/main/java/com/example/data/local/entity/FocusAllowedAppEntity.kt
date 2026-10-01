package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "focus_allowed_apps")
data class FocusAllowedAppEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val isEnabled: Boolean = true,
    val category: String = "EDUCATION", // "EDUCATION", "UTILITY", "REFERENCE", "SYSTEM"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    @get:Ignore
    val enabled: Boolean
        get() = isEnabled
}
