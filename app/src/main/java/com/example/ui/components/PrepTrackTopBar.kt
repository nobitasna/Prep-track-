package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubscriptionInfo
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepStreakOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrepTrackTopBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    streakCount: Int = 1,
    subscriptionInfo: SubscriptionInfo? = null,
    onSubscriptionClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.layout.Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                Box(modifier = Modifier.padding(start = 12.dp, end = 4.dp)) {
                    GlobalBackButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("top_bar_back_button")
                    )
                }
            }
        },
        actions = {
            // Subscription / License Pill
            if (subscriptionInfo != null) {
                if (subscriptionInfo.isPro) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFFBBF24).copy(alpha = 0.15f),
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clickable(onClick = onSubscriptionClick)
                            .testTag("top_bar_pro_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Pro",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "PRO",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                color = Color(0xFFFBBF24)
                            )
                        }
                    }
                } else if (subscriptionInfo.isTrialActive) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PrepCyanSecondary.copy(alpha = 0.14f),
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clickable(onClick = onSubscriptionClick)
                            .testTag("top_bar_trial_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Trial",
                                tint = PrepCyanSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = subscriptionInfo.trialRemainingFormatted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = PrepCyanSecondary
                            )
                        }
                    }
                }
            }

            // Streak Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrepStreakOrange.copy(alpha = 0.12f),
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = PrepStreakOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${streakCount}d",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = PrepStreakOrange
                    )
                }
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.testTag("top_bar_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
