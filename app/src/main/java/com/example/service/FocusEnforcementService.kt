package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.enforcement.FocusEnforcementManager
import com.example.permission.FocusPermissionManager
import com.example.ui.screens.focus.FocusBlockerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Foreground Service running during active Focus Mode sessions.
 *
 * Implements legitimate Android usage monitoring to detect unpermitted foreground apps
 * and redirect students to FocusBlockerActivity.
 */
class FocusEnforcementService : Service() {

    companion object {
        const val CHANNEL_ID = "prep_track_focus_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START_ENFORCEMENT = "com.example.action.START_ENFORCEMENT"
        const val ACTION_PAUSE_ENFORCEMENT = "com.example.action.PAUSE_ENFORCEMENT"
        const val ACTION_RESUME_ENFORCEMENT = "com.example.action.RESUME_ENFORCEMENT"
        const val ACTION_STOP_ENFORCEMENT = "com.example.action.STOP_ENFORCEMENT"
        const val ACTION_UPDATE_ALLOWED_APPS = "com.example.action.UPDATE_ALLOWED_APPS"

        const val EXTRA_SESSION_ID = "extra_session_id"
        const val EXTRA_ALLOWED_PACKAGES = "extra_allowed_packages"
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var monitoringJob: Job? = null

    private var allowedPackages = setOf<String>()
    private var isPaused = false
    private var systemLauncherPackage: String? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        resolveSystemLauncherPackage()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_ENFORCEMENT -> {
                val packagesList = intent.getStringArrayListExtra(EXTRA_ALLOWED_PACKAGES)
                allowedPackages = packagesList?.toSet() ?: emptySet()
                isPaused = false

                startForegroundWithNotification("Focus Mode Active", "Distraction Shield is protecting your study session.")
                startMonitoring()
            }

            ACTION_PAUSE_ENFORCEMENT -> {
                isPaused = true
                updateNotification("Focus Mode Paused", "Study session and restrictions temporarily paused.")
            }

            ACTION_RESUME_ENFORCEMENT -> {
                isPaused = false
                updateNotification("Focus Mode Active", "Distraction Shield resumed.")
            }

            ACTION_UPDATE_ALLOWED_APPS -> {
                val packagesList = intent.getStringArrayListExtra(EXTRA_ALLOWED_PACKAGES)
                allowedPackages = packagesList?.toSet() ?: emptySet()
            }

            ACTION_STOP_ENFORCEMENT -> {
                stopMonitoring()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stopMonitoring()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun startMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = serviceScope.launch {
            while (isActive) {
                delay(1000)

                // 1. Verify required permission has not been revoked
                if (!FocusPermissionManager.hasUsageAccessPermission(this@FocusEnforcementService)) {
                    FocusEnforcementManager.notifyPermissionRevoked()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                    break
                }

                if (isPaused) continue

                // 2. Identify currently foregrounded package
                val foregroundApp = getForegroundPackage() ?: continue

                // 3. Check if app is PREP TRACK itself or an allowed utility
                if (foregroundApp == packageName) continue
                if (isSystemPackageExcluded(foregroundApp)) continue

                // 4. Check if the app is in the user's allowed list
                if (allowedPackages.contains(foregroundApp)) {
                    // Allowed application — continue uninterrupted
                    continue
                }

                // 5. Unpermitted application detected — trigger restriction
                FocusEnforcementManager.notifyAppIntercepted(foregroundApp)
                launchFocusBlocker(foregroundApp)

                // Cooldown to avoid rapid duplicate launches while blocker opens
                delay(1500)
            }
        }
    }

    private fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
    }

    /**
     * Determines current foreground package using official UsageStatsManager API.
     */
    private fun getForegroundPackage(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return null
        val now = System.currentTimeMillis()

        return try {
            val events = usageStatsManager.queryEvents(now - 10000, now)
            val event = UsageEvents.Event()
            var lastForeground: String? = null

            while (events.hasNextEvent()) {
                events.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    lastForeground = event.packageName
                }
            }

            if (lastForeground != null) return lastForeground

            // Fallback for devices where queryEvents returns delayed entries
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                now - 10000,
                now
            )
            stats?.maxByOrNull { it.lastTimeUsed }?.packageName
        } catch (_: Exception) {
            null
        }
    }

    private fun isSystemPackageExcluded(pkg: String): Boolean {
        // Exclude system UI, Android settings, and default launcher
        if (pkg == systemLauncherPackage) return true
        if (pkg == "com.android.systemui") return true
        if (pkg == "com.android.dialer" || pkg == "com.google.android.dialer") return true
        if (pkg == "com.android.server.telecom") return true
        return false
    }

    private fun resolveSystemLauncherPackage() {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
            }
            val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            systemLauncherPackage = resolveInfo?.activityInfo?.packageName
        } catch (_: Exception) {}
    }

    private fun launchFocusBlocker(targetPackage: String) {
        val intent = Intent(this, FocusBlockerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("blocked_package", targetPackage)
        }
        try {
            startActivity(intent)
        } catch (_: Exception) {}
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "PREP TRACK Focus Mode",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Displays active Focus Mode session status and distraction protection"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(title: String, message: String): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun startForegroundWithNotification(title: String, message: String) {
        val notification = buildNotification(title, message)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun updateNotification(title: String, message: String) {
        val notification = buildNotification(title, message)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        manager?.notify(NOTIFICATION_ID, notification)
    }
}
