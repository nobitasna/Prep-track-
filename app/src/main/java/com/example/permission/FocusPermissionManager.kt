package com.example.permission

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings

/**
 * Reusable Android Permission Manager for PREP TRACK Focus Mode.
 *
 * Implements strictly legitimate Android APIs:
 * 1. Usage Access (AppOpsManager.OPSTR_GET_USAGE_STATS / PACKAGE_USAGE_STATS):
 *    Official API for detecting foreground apps during study sessions without root or
 *    invasive accessibility services.
 * 2. System Alert Window (Settings.canDrawOverlays / SYSTEM_ALERT_WINDOW):
 *    Official API for displaying study reminder / focus blocking interfaces over
 *    restricted distracting apps.
 */
object FocusPermissionManager {

    data class FocusPermissionState(
        val hasUsageAccess: Boolean = false,
        val hasOverlay: Boolean = false,
        val isGranted: Boolean = false,
        val isRevoked: Boolean = false,
        val isDenied: Boolean = false,
        val lastCheckedTimestamp: Long = System.currentTimeMillis()
    )

    /**
     * Legitimate check for Usage Access permission via AppOpsManager.
     * Compatible with Android 7.0 (API 24) through Android 15 / 16 (API 36).
     */
    fun hasUsageAccessPermission(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Legitimate check for Display Over Other Apps (Overlay) permission.
     */
    fun hasOverlayPermission(context: Context): Boolean {
        return try {
            Settings.canDrawOverlays(context)
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Checks all required permissions and computes the current comprehensive state,
     * detecting granted, missing, denied, and revoked conditions.
     */
    fun checkPermissionState(
        context: Context,
        wasPreviouslyGranted: Boolean = false,
        hasAttemptedGrant: Boolean = false
    ): FocusPermissionState {
        val usageAccess = hasUsageAccessPermission(context)
        val overlay = hasOverlayPermission(context)

        // Usage access is the primary required permission for Focus Mode enforcement
        val isAllRequiredGranted = usageAccess

        val isRevoked = wasPreviouslyGranted && !isAllRequiredGranted
        val isDenied = !isAllRequiredGranted && hasAttemptedGrant && !isRevoked

        return FocusPermissionState(
            hasUsageAccess = usageAccess,
            hasOverlay = overlay,
            isGranted = isAllRequiredGranted,
            isRevoked = isRevoked,
            isDenied = isDenied,
            lastCheckedTimestamp = System.currentTimeMillis()
        )
    }

    /**
     * Opens official Android system Settings screen for Usage Access.
     */
    fun openUsageAccessSettings(context: Context): Boolean {
        return try {
            // First attempt with package URI for direct deep-link on supported OEM/Android versions
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            try {
                // Fallback to general Usage Access settings list
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                true
            } catch (_: Exception) {
                // Final fallback to Application Details Settings
                openAppDetailsSettings(context)
            }
        }
    }

    /**
     * Opens official Android system Settings screen for Overlay permission.
     */
    fun openOverlaySettings(context: Context): Boolean {
        return try {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                true
            } catch (_: Exception) {
                openAppDetailsSettings(context)
            }
        }
    }

    /**
     * Opens the required Android system Settings screen for whichever required
     * permission is currently missing.
     */
    fun openRequiredSettings(context: Context): Boolean {
        return if (!hasUsageAccessPermission(context)) {
            openUsageAccessSettings(context)
        } else if (!hasOverlayPermission(context)) {
            openOverlaySettings(context)
        } else {
            openUsageAccessSettings(context)
        }
    }

    /**
     * Opens official Android Application Details screen.
     */
    fun openAppDetailsSettings(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
