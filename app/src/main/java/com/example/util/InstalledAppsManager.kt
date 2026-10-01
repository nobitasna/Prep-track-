package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.example.data.model.DeviceAppInfo

/**
 * Dynamically queries user-launchable apps installed on the current Android device
 * using legitimate Android PackageManager APIs.
 *
 * Fully compliant with Android 11+ package visibility (<queries> in AndroidManifest).
 * Zero hardcoded lists. Minimum information retrieved (label, package, icon).
 */
object InstalledAppsManager {

    fun getInstalledLaunchableApps(context: Context): List<DeviceAppInfo> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfoList = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(
                    intent,
                    PackageManager.ResolveInfoFlags.of(0L)
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(intent, 0)
            }
        } catch (_: Exception) {
            emptyList()
        }

        val currentAppPackage = context.packageName

        return resolveInfoList
            .mapNotNull { resolveInfo ->
                val activityInfo = resolveInfo.activityInfo ?: return@mapNotNull null
                val pkgName = activityInfo.packageName ?: return@mapNotNull null

                // Exclude PREP TRACK itself
                if (pkgName == currentAppPackage) return@mapNotNull null

                val label = try {
                    val rawLabel = resolveInfo.loadLabel(packageManager)?.toString()
                    if (!rawLabel.isNullOrBlank()) rawLabel else activityInfo.name ?: pkgName
                } catch (_: Exception) {
                    pkgName
                }

                val icon = try {
                    resolveInfo.loadIcon(packageManager) ?: activityInfo.loadIcon(packageManager)
                } catch (_: Exception) {
                    null
                }

                DeviceAppInfo(
                    packageName = pkgName,
                    appName = label,
                    icon = icon,
                    isAllowed = false
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.appName.lowercase() }
    }
}
