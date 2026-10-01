package com.example.enforcement

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.permission.FocusPermissionManager
import com.example.service.FocusEnforcementService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Dedicated manager for PREP TRACK Focus Mode enforcement (Phase 5).
 *
 * Implements the strongest legitimate Android enforcement mechanism available:
 * 1. UsageStats foreground app detection.
 * 2. Foreground Service protection with persistent study notification.
 * 3. Immediate redirection to FocusBlockerActivity when an unpermitted app opens.
 * 4. Transparent disclosure of Android sandbox limitations (no arbitrary process killing).
 */
object FocusEnforcementManager {

    enum class EnforcementState {
        IDLE,
        ACTIVE,
        PAUSED,
        PERMISSION_REVOKED,
        LIMITED_BY_SYSTEM
    }

    enum class EnforcementTier {
        STANDARD_SANDBOX, // Standard Android application sandbox (UsageStats + Blocker Activity + FGS)
        DEVICE_OWNER      // Not active without enterprise MDM / Device Owner setup
    }

    private val _enforcementState = MutableStateFlow(EnforcementState.IDLE)
    val enforcementState: StateFlow<EnforcementState> = _enforcementState.asStateFlow()

    private val _allowedPackages = MutableStateFlow<Set<String>>(emptySet())
    val allowedPackages: StateFlow<Set<String>> = _allowedPackages.asStateFlow()

    private val _activeSessionId = MutableStateFlow<Long?>(null)
    val activeSessionId: StateFlow<Long?> = _activeSessionId.asStateFlow()

    private val _lastBlockedPackage = MutableStateFlow<String?>(null)
    val lastBlockedPackage: StateFlow<String?> = _lastBlockedPackage.asStateFlow()

    private val _enforcementTier = MutableStateFlow(EnforcementTier.STANDARD_SANDBOX)
    val enforcementTier: StateFlow<EnforcementTier> = _enforcementTier.asStateFlow()

    /**
     * Starts legitimate Focus Mode enforcement.
     */
    fun startEnforcement(
        context: Context,
        sessionId: Long,
        allowedApps: Set<String>
    ): Boolean {
        // Validate required permission
        if (!FocusPermissionManager.hasUsageAccessPermission(context)) {
            _enforcementState.value = EnforcementState.PERMISSION_REVOKED
            return false
        }

        _allowedPackages.value = allowedApps
        _activeSessionId.value = sessionId
        _enforcementState.value = EnforcementState.ACTIVE

        val intent = Intent(context, FocusEnforcementService::class.java).apply {
            action = FocusEnforcementService.ACTION_START_ENFORCEMENT
            putExtra(FocusEnforcementService.EXTRA_SESSION_ID, sessionId)
            putStringArrayListExtra(FocusEnforcementService.EXTRA_ALLOWED_PACKAGES, ArrayList(allowedApps))
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            return true
        } catch (_: Exception) {
            _enforcementState.value = EnforcementState.LIMITED_BY_SYSTEM
            return false
        }
    }

    /**
     * Pauses enforcement during focus breaks or paused study.
     */
    fun pauseEnforcement(context: Context) {
        if (_enforcementState.value == EnforcementState.ACTIVE) {
            _enforcementState.value = EnforcementState.PAUSED
            val intent = Intent(context, FocusEnforcementService::class.java).apply {
                action = FocusEnforcementService.ACTION_PAUSE_ENFORCEMENT
            }
            try {
                context.startService(intent)
            } catch (_: Exception) {}
        }
    }

    /**
     * Resumes enforcement when study session is unpaused.
     */
    fun resumeEnforcement(context: Context): Boolean {
        // Re-validate permission upon resume
        if (!FocusPermissionManager.hasUsageAccessPermission(context)) {
            _enforcementState.value = EnforcementState.PERMISSION_REVOKED
            stopEnforcement(context)
            return false
        }

        _enforcementState.value = EnforcementState.ACTIVE
        val intent = Intent(context, FocusEnforcementService::class.java).apply {
            action = FocusEnforcementService.ACTION_RESUME_ENFORCEMENT
        }
        try {
            context.startService(intent)
            return true
        } catch (_: Exception) {
            return false
        }
    }

    /**
     * Stops enforcement and dismisses the Foreground Service.
     */
    fun stopEnforcement(context: Context) {
        _enforcementState.value = EnforcementState.IDLE
        _activeSessionId.value = null
        val intent = Intent(context, FocusEnforcementService::class.java).apply {
            action = FocusEnforcementService.ACTION_STOP_ENFORCEMENT
        }
        try {
            context.startService(intent)
        } catch (_: Exception) {}
    }

    /**
     * Updates the in-memory allowed-app list while enforcement is active.
     */
    fun updateAllowedApps(context: Context, allowedApps: Set<String>) {
        _allowedPackages.value = allowedApps
        if (_enforcementState.value == EnforcementState.ACTIVE) {
            val intent = Intent(context, FocusEnforcementService::class.java).apply {
                action = FocusEnforcementService.ACTION_UPDATE_ALLOWED_APPS
                putStringArrayListExtra(FocusEnforcementService.EXTRA_ALLOWED_PACKAGES, ArrayList(allowedApps))
            }
            try {
                context.startService(intent)
            } catch (_: Exception) {}
        }
    }

    /**
     * Called by the service when an unpermitted package is intercepted.
     */
    fun notifyAppIntercepted(packageName: String) {
        _lastBlockedPackage.value = packageName
    }

    /**
     * Called when system permission was revoked during active enforcement.
     */
    fun notifyPermissionRevoked() {
        _enforcementState.value = EnforcementState.PERMISSION_REVOKED
    }
}
