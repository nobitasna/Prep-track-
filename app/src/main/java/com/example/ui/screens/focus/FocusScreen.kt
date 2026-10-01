package com.example.ui.screens.focus

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.components.ProgressRing
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepPurpleAccent
import com.example.ui.theme.PrepPurpleLight
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

@Composable
fun FocusScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState by viewModel.focusPermissionState.collectAsStateWithLifecycle()

    // Automatically recheck permission whenever returning to PREP TRACK
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

    // If required Android permission is missing, denied, or revoked, show permission screen
    if (!permissionState.isGranted) {
        FocusPermissionScreen(
            viewModel = viewModel,
            modifier = modifier,
            onPermissionGranted = {
                // Automatically transitions to Focus Mode setup
            }
        )
        return
    }

    val pomodoroState by viewModel.pomodoroState.collectAsStateWithLifecycle()

    // Phase 6: When a Focus session is active (running or paused), show Active Focus Session Screen!
    if (pomodoroState.activeSessionId != null && (pomodoroState.isRunning || pomodoroState.isPaused)) {
        ActiveFocusSessionView(
            viewModel = viewModel,
            pomodoroState = pomodoroState,
            modifier = modifier
        )
        return
    }

    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val planCalc by viewModel.planCalculation.collectAsStateWithLifecycle()
    val totalFocusMinutes by viewModel.userStreak.collectAsStateWithLifecycle()
    val allowedApps by viewModel.allowedApps.collectAsStateWithLifecycle()
    val enabledAllowedApps by viewModel.enabledAllowedApps.collectAsStateWithLifecycle()
    val focusSettings by viewModel.focusSettings.collectAsStateWithLifecycle()
    val activeSession by viewModel.activeFocusSession.collectAsStateWithLifecycle()
    val enforcementState by viewModel.enforcementState.collectAsStateWithLifecycle()
    val lastBlockedPackage by viewModel.lastBlockedPackage.collectAsStateWithLifecycle()
    val enforcementTier by viewModel.enforcementTier.collectAsStateWithLifecycle()

    var showSubjectDropdown by remember { mutableStateOf(false) }
    var topicInput by remember { mutableStateOf(pomodoroState.selectedTopic) }
    var showAddAppDialog by remember { mutableStateOf(false) }
    var newAppName by remember { mutableStateOf("") }
    var newPackageName by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("EDUCATION") }

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
        // Android Permission Status Chip
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF0F172A),
                border = BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { viewModel.navigateTo(AppScreen.FocusPermission) }
                    .testTag("focus_permission_status_chip")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrepSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Usage Access Active • Focus Protection Configured",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                    Text(
                        text = "Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrepCyanSecondary
                    )
                }
            }
        }

        // Active Focus Mode Enforcement Banner
        if (pomodoroState.isRunning) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("focus_active_enforcement_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1929)),
                    border = BorderStroke(1.dp, Color(0x6638BDF8))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = PrepCyanSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Focus Mode Active",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0x2210B981),
                                border = BorderStroke(1.dp, Color(0x6610B981))
                            ) {
                                Text(
                                    text = "ENFORCING",
                                    color = Color(0xFF34D399),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Foreground Service & Usage monitoring active. ${enabledAllowedApps.size} apps are allowed; all other apps will be intercepted.",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8),
                            lineHeight = 16.sp
                        )

                        if (lastBlockedPackage != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Recent interception: $lastBlockedPackage",
                                fontSize = 11.sp,
                                color = PrepCyanSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

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

        // Focus Mode Advanced Settings (Room DB Backed)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("focus_settings_card"),
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
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrepBluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Strict Study Mode",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Prevents early cancellation & enforces commitment",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = pomodoroState.strictModeEnabled,
                            onCheckedChange = { viewModel.setStrictMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PrepBluePrimary),
                            modifier = Modifier.testTag("strict_mode_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Ambient Audio
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = PrepBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ambient Soundscape",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val sounds = listOf(
                            "NONE" to "Off",
                            "WHITE_NOISE" to "White Noise",
                            "RAIN" to "Rain",
                            "BINAURAL" to "Binaural"
                        )
                        sounds.forEach { (type, label) ->
                            val isSelected = pomodoroState.ambientSoundType == type
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setAmbientSound(type) }
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else PrepBlueDark,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Room DB Persistence Status Indicator
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Room Database: Focus state & session survive screen navigation & restarts",
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Allowed Apps Whitelist Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("focus_allowed_apps_card"),
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
                                imageVector = Icons.Default.Apps,
                                contentDescription = null,
                                tint = PrepBluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Allowed Apps Whitelist",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${enabledAllowedApps.size} apps permitted during focus mode",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.ChooseAllowedApps) },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary),
                            modifier = Modifier.testTag("choose_allowed_apps_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Apps,
                                contentDescription = "Choose",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Choose Apps", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (enabledAllowedApps.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF131D31),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.navigateTo(AppScreen.ChooseAllowedApps) }
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No Allowed Apps Selected",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "All non-study apps will be restricted during Focus Mode. Tap 'Choose Apps' to select apps you want available.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF94A3B8),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        enabledAllowedApps.forEach { app ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (app.isEnabled) PrepBlueLight else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = app.appName.take(1).uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                color = if (app.isEnabled) PrepBlueDark else MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = app.appName,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = app.category,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PrepBluePrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = app.packageName,
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }

                                Switch(
                                    checked = app.isEnabled,
                                    onCheckedChange = { viewModel.toggleAllowedApp(app.packageName, it) },
                                    colors = SwitchDefaults.colors(checkedThumbColor = PrepBluePrimary),
                                    modifier = Modifier.testTag("app_switch_${app.packageName.replace('.', '_')}")
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Allowed App Dialog
    if (showAddAppDialog) {
        AlertDialog(
            onDismissRequest = { showAddAppDialog = false },
            title = {
                Text("Add Allowed App", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "Permit an application to be accessible during Focus Mode sessions:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newAppName,
                        onValueChange = { newAppName = it },
                        label = { Text("App Name") },
                        placeholder = { Text("e.g. NCERT Books, Calculator") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPackageName,
                        onValueChange = { newPackageName = it },
                        label = { Text("Package Name") },
                        placeholder = { Text("e.g. com.example.ncert") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("EDUCATION", "UTILITY", "REFERENCE").forEach { cat ->
                            val isSel = newCategory == cat
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { newCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else PrepBlueDark,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAppName.isNotBlank() && newPackageName.isNotBlank()) {
                            viewModel.addAllowedApp(
                                packageName = newPackageName.trim(),
                                appName = newAppName.trim(),
                                category = newCategory
                            )
                            newAppName = ""
                            newPackageName = ""
                            showAddAppDialog = false
                        }
                    },
                    enabled = newAppName.isNotBlank() && newPackageName.isNotBlank()
                ) {
                    Text("Add App")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAppDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
