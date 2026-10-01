package com.example.ui.screens.focus

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.permission.FocusPermissionManager
import com.example.ui.components.GlobalBackButton
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepStreakOrange
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel

/**
 * PREP TRACK — Focus Mode Permission Screen.
 *
 * Implements Phase 3 legitimate permission management:
 * - Clear explanation: "Focus Mode needs the required Android permission to enforce your selected focus restrictions."
 * - Prominent "Allow Permission" button opening official Android Settings.
 * - Auto-recheck on lifecycle resume.
 * - Handles Granted, Missing, Denied, and Revoked states gracefully.
 */
@Composable
fun FocusPermissionScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current
    val permissionState by viewModel.focusPermissionState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Automatically check permission whenever returning to PREP TRACK from Android Settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshFocusPermissions(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Trigger onPermissionGranted when granted
    LaunchedEffect(permissionState.isGranted) {
        if (permissionState.isGranted) {
            onPermissionGranted()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070D18)) // Dark premium navy/black
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row with optional back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                GlobalBackButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("focus_permission_back_button")
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column {
                Text(
                    text = "PREP TRACK FOCUS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    ),
                    color = PrepCyanSecondary
                )
                Text(
                    text = "Permission Setup",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Hero Security / Shield Graphic
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x3338BDF8), Color(0x050284C7), Color.Transparent)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF131D31),
                border = BorderStroke(2.dp, Color(0x6638BDF8)),
                modifier = Modifier.size(76.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (permissionState.isRevoked) Icons.Default.Warning else Icons.Default.Security,
                        contentDescription = "Security Shield",
                        tint = if (permissionState.isRevoked) PrepStreakOrange else PrepCyanSecondary,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Revocation Warning Banner (if permission was revoked later)
        if (permissionState.isRevoked) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("permission_revoked_banner"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x22F97316)),
                border = BorderStroke(1.dp, Color(0x66F97316))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFB923C),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Permission Revoked",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDBA74),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Focus Mode enforcement has been safely deactivated because the system permission was removed in Android Settings.",
                            fontSize = 12.sp,
                            color = Color(0xFFFED7AA),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Primary Explanation & Action Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("focus_permission_main_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF10192A)),
            border = BorderStroke(1.dp, Color(0xFF1E2D48)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Status Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        permissionState.isGranted -> Color(0x2210B981)
                        permissionState.isRevoked -> Color(0x22EF4444)
                        permissionState.isDenied -> Color(0x22F59E0B)
                        else -> Color(0x2238BDF8)
                    },
                    border = BorderStroke(
                        1.dp,
                        when {
                            permissionState.isGranted -> Color(0x6610B981)
                            permissionState.isRevoked -> Color(0x66EF4444)
                            permissionState.isDenied -> Color(0x66F59E0B)
                            else -> Color(0x6638BDF8)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    when {
                                        permissionState.isGranted -> PrepSuccess
                                        permissionState.isRevoked -> Color(0xFFEF4444)
                                        permissionState.isDenied -> Color(0xFFF59E0B)
                                        else -> PrepCyanSecondary
                                    },
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when {
                                permissionState.isGranted -> "STATUS: PERMISSION GRANTED"
                                permissionState.isRevoked -> "STATUS: PERMISSION REVOKED"
                                permissionState.isDenied -> "STATUS: ENFORCEMENT INACTIVE (DENIED)"
                                else -> "STATUS: PERMISSION REQUIRED"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = when {
                                permissionState.isGranted -> Color(0xFF34D399)
                                permissionState.isRevoked -> Color(0xFFF87171)
                                permissionState.isDenied -> Color(0xFFFBBF24)
                                else -> PrepCyanSecondary
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // MANDATORY REQUIRED EXPLANATION TEXT
                Text(
                    text = "Focus Mode needs the required Android permission to enforce your selected focus restrictions.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Android requires explicit Usage Access permission so PREP TRACK can detect when distracting non-study apps are opened during your active focus sessions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8),
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // MANDATORY REQUIRED BUTTON
                Button(
                    onClick = {
                        viewModel.onAllowPermissionClicked(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("allow_permission_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrepBluePrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Allow Permission",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Recheck button
                OutlinedButton(
                    onClick = {
                        viewModel.refreshFocusPermissions(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("recheck_permission_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E2D48))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Re-check",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Check Permission Status",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Legitimate API Guarantee Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("privacy_guarantee_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrepSuccess,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Privacy & Official API Guarantee",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val guarantees = listOf(
                    "Official Android API" to "Uses standard AppOpsManager & UsageStatsManager without hacks.",
                    "No Root & No Accessibility Abuse" to "Never requests root, hidden monitoring, or invasive accessibility services.",
                    "100% Private" to "Never reads chats, private messages, passwords, screen recordings, or notification content.",
                    "Local-Only Evaluation" to "App checking executes purely on your device during active study sessions."
                )

                guarantees.forEach { (title, desc) ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            color = PrepCyanSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Column {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCBD5E1)
                            )
                            Text(
                                text = desc,
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
