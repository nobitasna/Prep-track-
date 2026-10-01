package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Universal PREP TRACK Global Back Button.
 * Follows the consistent design system:
 * - Minimal left-arrow icon
 * - White / light-gray icon tint
 * - Deep navy circular background with subtle border
 * - 44-48dp minimum touch target
 * - Smooth ripple and accessible testTag
 */
@Composable
fun GlobalBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconColor: Color = Color.White,
    backgroundColor: Color = Color(0xFF131D31),
    borderColor: Color = Color(0x3338BDF8),
    contentDescription: String = "Navigate Back"
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .testTag("global_back_button"),
        shape = CircleShape,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
