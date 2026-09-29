package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrepBluePrimary,
    onPrimary = Color.White,
    primaryContainer = PrepBlueLight,
    onPrimaryContainer = Color.White,
    secondary = PrepCyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E283A),
    onSecondaryContainer = PrepCyanSecondary,
    tertiary = PrepPurpleAccent,
    background = PrepBackground,
    surface = PrepSurface,
    onBackground = PrepTextPrimary,
    onSurface = PrepTextPrimary,
    surfaceVariant = PrepSurfaceVariant,
    onSurfaceVariant = PrepTextSecondary,
    outline = PrepBorder
)

@Composable
fun PrepTrackTheme(
    darkTheme: Boolean = true, // Default to design figure dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
