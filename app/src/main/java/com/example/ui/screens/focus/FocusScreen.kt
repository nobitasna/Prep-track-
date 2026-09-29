package com.example.ui.screens.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ProgressRing
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepPurpleAccent
import com.example.ui.theme.PrepPurpleLight
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel

@Composable
fun FocusScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val pomodoroState by viewModel.pomodoroState.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val planCalc by viewModel.planCalculation.collectAsStateWithLifecycle()
    val totalFocusMinutes by viewModel.userStreak.collectAsStateWithLifecycle()

    var showSubjectDropdown by remember { mutableStateOf(false) }
    var topicInput by remember { mutableStateOf(pomodoroState.selectedTopic) }

    val minutes = pomodoroState.remainingSeconds / 60
    val seconds = pomodoroState.remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val progressFraction = if (pomodoroState.totalSeconds > 0) {
        pomodoroState.remainingSeconds.toFloat() / pomodoroState.totalSeconds
    } else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode & Preset Selector
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (pomodoroState.isBreak) "BREAK TIME" else "FOCUS INTERVAL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (pomodoroState.isBreak) PrepSuccess else PrepBluePrimary
                        )
                        Text(
                            text = "Today: ${pomodoroState.completedFocusMinutesToday} mins",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val is25 = pomodoroState.totalSeconds == 25 * 60 && !pomodoroState.isBreak
                        val is50 = pomodoroState.totalSeconds == 50 * 60 && !pomodoroState.isBreak

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (is25) PrepBluePrimary else PrepBlueLight,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setPomodoroPreset(25, 5) }
                                .testTag("focus_preset_25")
                        ) {
                            Text(
                                text = "25m / 5m",
                                fontWeight = if (is25) FontWeight.Bold else FontWeight.Medium,
                                color = if (is25) Color.White else PrepBlueDark,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (is50) PrepBluePrimary else PrepBlueLight,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setPomodoroPreset(50, 10) }
                                .testTag("focus_preset_50")
                        ) {
                            Text(
                                text = "50m / 10m",
                                fontWeight = if (is50) FontWeight.Bold else FontWeight.Medium,
                                color = if (is50) Color.White else PrepBlueDark,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PrepBlueLight,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setPomodoroPreset(45, 15) }
                                .testTag("focus_preset_custom")
                        ) {
                            Text(
                                text = "45m / 15m",
                                fontWeight = FontWeight.Medium,
                                color = PrepBlueDark,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Timer Dial
        item {
            Box(
                modifier = Modifier
                    .padding(vertical = 18.dp)
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                ProgressRing(
                    progress = progressFraction,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 14.dp,
                    progressColor = if (pomodoroState.isBreak) PrepSuccess else PrepBluePrimary,
                    backgroundColor = if (pomodoroState.isBreak) Color(0xFFD1FAE5) else PrepBlueLight
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (pomodoroState.isBreak) Icons.Default.Coffee else Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (pomodoroState.isBreak) PrepSuccess else PrepBluePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 42.sp,
                                letterSpacing = 2.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (pomodoroState.isBreak) "Break Interval" else pomodoroState.selectedSubject,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Control Buttons: Start, Pause, Resume, Reset, Skip Break
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.resetPomodoro() },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("focus_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Timer",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (pomodoroState.isRunning) {
                            viewModel.pausePomodoro()
                        } else {
                            viewModel.startPomodoro()
                        }
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                        .testTag("focus_start_pause_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pomodoroState.isBreak) PrepSuccess else PrepBluePrimary
                    )
                ) {
                    Icon(
                        imageVector = if (pomodoroState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (pomodoroState.isRunning) "Pause" else "Start"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (pomodoroState.isRunning) "Pause" else "Start Focus",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { viewModel.skipBreak() },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("focus_skip_break_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Skip Break",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Session Tagging: Subject & Topic
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SESSION TAGGING",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PrepBluePrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Subject:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Box {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrepBlueLight,
                                modifier = Modifier.clickable { showSubjectDropdown = true }
                            ) {
                                Text(
                                    text = pomodoroState.selectedSubject,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrepBlueDark,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showSubjectDropdown,
                                onDismissRequest = { showSubjectDropdown = false }
                            ) {
                                subjects.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s.name) },
                                        onClick = {
                                            viewModel.setPomodoroTag(s.name, "", pomodoroState.selectedTopic)
                                            showSubjectDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = {
                            topicInput = it
                            viewModel.setPomodoroTag(pomodoroState.selectedSubject, "", it)
                        },
                        label = { Text("Topic / Chapter (Optional)") },
                        placeholder = { Text("e.g. Rotational Motion numericals") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // CRITICAL METRIC SEPARATION CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrepPurpleLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = PrepPurpleAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Three Distinct Study Metrics",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4A148C)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• A. Fixed Reference: 2h/lecture invariant benchmark\n• B. Estimated Watch Time: Adjusted by playback speed (${planCalc.playbackSpeed}×)\n• C. Actual Focus Time: Measured live here through active timer",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A148C)
                    )
                }
            }
        }

        // Distraction Control & Focus Shield
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = PrepBluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Focus Mode Distraction Shield",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Switch(
                            checked = pomodoroState.selectedDistractionShield,
                            onCheckedChange = { viewModel.toggleDistractionShield() },
                            colors = SwitchDefaults.colors(checkedThumbColor = PrepBluePrimary)
                        )
                    }

                    if (pomodoroState.selectedDistractionShield) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PrepBlueLight
                        ) {
                            Text(
                                text = "🛡️ Shield Active: Keep this screen open during study. Put your device on 'Do Not Disturb' to prevent notification interruptions.",
                                fontSize = 12.sp,
                                color = PrepBlueDark,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
