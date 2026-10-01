package com.example.data.model

import android.graphics.drawable.Drawable

/**
 * Model representing an application installed on the user's device,
 * discovered dynamically via PackageManager.
 */
data class DeviceAppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable? = null,
    val isAllowed: Boolean = false
)
