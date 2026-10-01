package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "focus_settings")
data class FocusSettingsEntity(
    @PrimaryKey
    val id: Int = 1, // Single global configuration row
    val isFocusModeActive: Boolean = false,
    val isPaused: Boolean = false,
    val pauseRemainingSeconds: Int = 0,
    val pausedAtTimestamp: Long? = null,
    val activeSessionId: Long? = null,
    val targetEndTimeTimestamp: Long? = null,
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val isBreak: Boolean = false,
    val selectedGoalId: Long? = null,
    val selectedSubjectId: Long? = null,
    val selectedSubjectName: String = "Physics",
    val selectedChapterName: String = "",
    val selectedTopicName: String = "",
    val distractionShieldEnabled: Boolean = true,
    val strictModeEnabled: Boolean = false,
    val ambientSoundType: String = "NONE", // "NONE", "WHITE_NOISE", "RAIN", "BINAURAL"
    val keepScreenOn: Boolean = true,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
) {
    @get:Ignore
    val enabled: Boolean
        get() = isFocusModeActive
}
