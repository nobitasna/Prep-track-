package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrepBluePrimary,
    onPrimary = Color.White,
    primaryContainer = PrepBlueDark,
    onPrimaryContainer = PrepBlueLight,
    secondary = PrepCyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF01579B),
    onSecondaryContainer = PrepCyanLight,
    tertiary = PrepPurpleAccent,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = PrepBluePrimary,
    onPrimary = Color.White,
    primaryContainer = PrepBlueLight,
    onPrimaryContainer = PrepBlueDark,
    secondary = PrepCyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = PrepCyanLight,
    onSecondaryContainer = Color(0xFF01579B),
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our crisp original theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
