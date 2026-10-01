package com.example.data.model

import com.example.data.local.entity.FocusAllowedAppEntity
import com.example.data.local.entity.FocusSessionEntity
import com.example.data.local.entity.FocusSettingsEntity

/**
 * Focus Mode Domain Models & Type Aliases
 * Seamlessly integrates with PrepTrack Room architecture while remaining
 * clean and ready for future cloud sync.
 */
typealias FocusSettings = FocusSettingsEntity
typealias FocusAllowedApp = FocusAllowedAppEntity
typealias FocusSession = FocusSessionEntity

data class FocusStatusSummary(
    val isActive: Boolean = false,
    val isPaused: Boolean = false,
    val isBreak: Boolean = false,
    val remainingSeconds: Int = 25 * 60,
    val totalSeconds: Int = 25 * 60,
    val subjectName: String = "Physics",
    val chapterName: String = "",
    val topicName: String = "",
    val allowedAppsCount: Int = 0,
    val todayCompletedMinutes: Int = 0,
    val distractionShieldEnabled: Boolean = true,
    val strictModeEnabled: Boolean = false,
    val ambientSoundType: String = "NONE"
)
