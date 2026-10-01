package com.example.ui.screens.focus

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.PomodoroUiState

/**
 * PREP TRACK — Active Focus Screen (Phase 6).
 *
 * Displays live, timestamp-verified study tracking during an active Focus Mode session:
 * - "FOCUS MODE ACTIVE" or "FOCUS PAUSED"
 * - Current Task
 * - Subject & Chapter
 * - Elapsed Focus Time (00:00:00)
 * - Status: ACTIVE / PAUSED
 * - Allowed Apps count
 * - Controls: [ PAUSE ] / [ RESUME ] and [ END FOCUS ]
 * - Safe back-press interception with confirmation dialog
 */
@Composable
fun ActiveFocusSessionView(
    viewModel: MainViewModel,
    pomodoroState: PomodoroUiState,
    modifier: Modifier = Modifier
) {
    var showEndConfirmDialog by remember { mutableStateOf(false) }

    // Intercept Back button so system Back never accidentally destroys the active session
    BackHandler {
        showEndConfirmDialog = true
    }

    val isActive = pomodoroState.isRunning && !pomodoroState.isPaused
    val elapsedTimeFormatted = pomodoroState.getFormattedElapsedTime()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070D18)) // Deep dark navy/black PREP TRACK background
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Status Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Pulsing Status Dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (isActive) PrepSuccess else Color(0xFFF59E0B))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isActive) "FOCUS MODE ACTIVE" else "FOCUS PAUSED",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    ),
                    color = Color.White
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isActive) Color(0x2210B981) else Color(0x22F59E0B),
                border = BorderStroke(1.dp, if (isActive) Color(0x6610B981) else Color(0x66F59E0B))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (isActive) PrepSuccess else Color(0xFFF59E0B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isActive) "SHIELD ACTIVE" else "SHIELD PAUSED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color(0xFF34D399) else Color(0xFFFBBF24)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Current Task Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF10192A)),
            border = BorderStroke(1.dp, Color(0xFF1E2D48))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HourglassBottom,
                        contentDescription = null,
                        tint = PrepCyanSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CURRENT TASK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = PrepCyanSecondary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = pomodoroState.currentTaskName.ifBlank {
                        if (pomodoroState.selectedTopic.isNotBlank()) pomodoroState.selectedTopic else "Core Exam Preparation"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Subject: ${pomodoroState.selectedSubject}${
                            if (pomodoroState.selectedChapter.isNotBlank()) " • " + pomodoroState.selectedChapter else ""
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Center Digital Clock & Elapsed Focus Time
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.5.dp, if (isActive) Color(0x6638BDF8) else Color(0x66F59E0B))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ELAPSED FOCUS TIME",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    ),
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Digital Timer
                Text(
                    text = elapsedTimeFormatted,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    modifier = Modifier.testTag("elapsed_focus_time_display")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isActive) {
                        "Active study duration calculated from system timestamps"
                    } else {
                        "Timer is paused. Paused time is not counted as study time"
                    },
                    fontSize = 12.sp,
                    color = if (isActive) Color(0xFF64748B) else Color(0xFFFBBF24),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Session Metadata Metrics Row (Status & Allowed Apps)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF10192A)),
                border = BorderStroke(1.dp, Color(0xFF1E2D48))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "STATUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isActive) "ACTIVE" else "PAUSED",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isActive) PrepSuccess else Color(0xFFF59E0B)
                    )
                }
            }

            // Allowed Apps Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF10192A)),
                border = BorderStroke(1.dp, Color(0xFF1E2D48))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ALLOWED APPS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = null,
                            tint = PrepCyanSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${pomodoroState.allowedAppsCount} Apps",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Session Action Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isActive) {
                // [ PAUSE ]
                OutlinedButton(
                    onClick = { viewModel.pauseFocusSession() },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFF59E0B)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0x11F59E0B),
                        contentColor = Color(0xFFFBBF24)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("pause_focus_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PAUSE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                // [ RESUME ]
                Button(
                    onClick = { viewModel.resumeFocusSession() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("resume_focus_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RESUME",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // [ END FOCUS ]
            Button(
                onClick = { showEndConfirmDialog = true },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("end_focus_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "End Focus",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "END FOCUS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }

    // Confirmation Dialog: "End Focus Session?"
    if (showEndConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showEndConfirmDialog = false },
            title = {
                Text(
                    text = "End Focus Session?",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column {
                    Text(
                        text = "Current active study duration: $elapsedTimeFormatted",
                        color = PrepCyanSecondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ending the session will stop distraction restrictions and record your verified study time in your progress analytics.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEndConfirmDialog = false
                        viewModel.endFocusSession(isAbandoned = false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    modifier = Modifier.testTag("confirm_end_focus_button")
                ) {
                    Text("End Focus", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEndConfirmDialog = false },
                    modifier = Modifier.testTag("continue_focus_button")
                ) {
                    Text("Continue", color = Color(0xFF94A3B8))
                }
            },
            containerColor = Color(0xFF10192A),
            textContentColor = Color(0xFFCBD5E1),
            shape = RoundedCornerShape(16.dp)
        )
    }
}
