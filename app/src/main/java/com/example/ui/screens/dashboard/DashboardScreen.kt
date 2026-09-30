package com.example.ui.screens.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ProgressRing
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.School
import com.example.ui.theme.PrepBorder
import com.example.ui.theme.PrepSurface
import com.example.ui.theme.PrepSurfaceVariant
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepStreakOrange
import com.example.ui.theme.PrepSuccess
import com.example.ui.theme.PrepWarning
import com.example.ui.theme.PrepWarningLight
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel
import java.util.Calendar

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeGoal by viewModel.activeGoal.collectAsStateWithLifecycle()
    val userStreak by viewModel.userStreak.collectAsStateWithLifecycle()
    val planCalc by viewModel.planCalculation.collectAsStateWithLifecycle()
    val subjectProgress by viewModel.subjectProgressList.collectAsStateWithLifecycle()
    val todayTasks by viewModel.todayTasks.collectAsStateWithLifecycle()
    val todayProgress by viewModel.todayProgressSummary.collectAsStateWithLifecycle()
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var showMissedAdjustDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val dailyProgressFraction = todayProgress.progressFraction

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Welcome Top Bar matching Design Spec Figure
        item {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val greeting = when {
                hour < 12 -> "Good Morning"
                hour < 17 -> "Good Afternoon"
                else -> "Good Evening"
            }
            val firstName = userProfile.name.trim().split(" ").firstOrNull()?.ifEmpty { "Scholar" } ?: "Scholar"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PrepBlueLight)
                            .border(1.5.dp, PrepBluePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = firstName.take(1).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrepCyanSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "$greeting, $firstName ☀️",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${activeGoal?.examName ?: "PREP TRACK"} (${activeGoal?.targetYear ?: "Target"})",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = PrepBluePrimary
                            )
                            Text(
                                text = " • ${planCalc.targetDays}d Plan",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Notification Icon
                    Surface(
                        shape = CircleShape,
                        color = PrepSurfaceVariant,
                        modifier = Modifier
                            .size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Reset & Reload Syllabus Button
                    Surface(
                        shape = CircleShape,
                        color = PrepSurfaceVariant,
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { showResetDialog = true }
                            .testTag("dashboard_reset_syllabus_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset & Reload Syllabus",
                                tint = PrepBluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Switch Exam Goal Button
                    Surface(
                        shape = CircleShape,
                        color = PrepBluePrimary,
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { viewModel.navigateTo(AppScreen.OnboardingSelectGoal) }
                            .testTag("dashboard_switch_goal_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Switch Goal",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Today's Progress Card (Figure Layout)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PrepSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrepBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Today's Progress",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular Progress Ring
                        ProgressRing(
                            progress = todayProgress.progressFraction,
                            modifier = Modifier.size(76.dp),
                            strokeWidth = 8.dp,
                            progressColor = if (todayProgress.isGoalAchieved) PrepSuccess else PrepBluePrimary,
                            backgroundColor = PrepSurfaceVariant
                        ) {
                            Text(
                                text = "${(todayProgress.progressFraction * 100).toInt()}%",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = if (todayProgress.isGoalAchieved) PrepSuccess else PrepBluePrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${todayProgress.completedLectures}/${todayProgress.totalTasks} Tasks Done",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Required Workload: ${planCalc.dailyWorkloadFormatted}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PrepBluePrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Completed: ${todayProgress.completedWorkloadFormatted} • Left: ${todayProgress.remainingWorkloadFormatted}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (todayProgress.isGoalAchieved) PrepSuccess else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Target: ${planCalc.dailyLecturesRequired} lectures/day (${planCalc.targetDays}d target)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (todayProgress.isGoalAchieved) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "🎉 Today's Required Goal Completed!",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrepSuccess
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3 Metric Pills (Day Streak, Total Lectures, Syllabus %)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PrepSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrepBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = null, tint = PrepStreakOrange, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${userStreak?.currentStreak ?: 1}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Day Streak", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Total Lectures Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PrepSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrepBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.School, contentDescription = null, tint = PrepBluePrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${planCalc.completedLectures}/${planCalc.totalLectures}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Total Lectures", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Syllabus Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PrepSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrepBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val sylPercent = if (planCalc.totalLectures > 0) (planCalc.completedLectures * 100 / planCalc.totalLectures) else 0
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = PrepSuccess, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$sylPercent%", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Syllabus", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Missed Workload Adjustment Banner if needed
        val missedHours = studyPlan?.missedWorkloadHoursAdjustment ?: 0f
        if (missedHours > 0f) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrepWarningLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = PrepWarning,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${String.format("%.1f", missedHours)}h of planned work remain from previous days.",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF92400E)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.applyMissedWorkload(0f, "DISTRIBUTE") },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Distribute Across Days", fontSize = 11.sp)
                            }
                            OutlinedButton(
                                onClick = { viewModel.applyMissedWorkload(0f, "CLEAR") },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Dismiss", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Text(
                text = "QUICK ACTIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                item {
                    QuickActionButton(
                        icon = Icons.Default.PlayArrow,
                        label = "Start Focus",
                        isPrimary = true,
                        onClick = { viewModel.navigateTo(AppScreen.Focus) },
                        testTag = "quick_action_start_focus"
                    )
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.CalendarMonth,
                        label = "Study Plan",
                        onClick = { viewModel.navigateTo(AppScreen.Plan) },
                        testTag = "quick_action_study_plan"
                    )
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.MenuBook,
                        label = "Syllabus",
                        onClick = { viewModel.navigateTo(AppScreen.Syllabus) },
                        testTag = "quick_action_syllabus"
                    )
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.Timer,
                        label = "Pomodoro",
                        onClick = { viewModel.navigateTo(AppScreen.Focus) },
                        testTag = "quick_action_pomodoro"
                    )
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.BarChart,
                        label = "Analytics",
                        onClick = { viewModel.navigateTo(AppScreen.Analytics) },
                        testTag = "quick_action_analytics"
                    )
                }
            }
        }

        // Today's Lectures & Tasks Checklist
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S LECTURES & TASKS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${todayProgress.completedLectures} / ${todayProgress.totalTasks} Done",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (todayProgress.isGoalAchieved) PrepSuccess else PrepBluePrimary
                )
            }
        }

        if (todayTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrepSuccess,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "All caught up for today!",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Or explore your syllabus to track additional chapters.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(todayTasks) { task ->
                TodayTaskCard(
                    task = task,
                    onToggleComplete = { viewModel.toggleLectureCompleted(task.lectureId) },
                    onToggleNotes = { viewModel.toggleLectureNotes(task.lectureId) },
                    onTogglePractice = { viewModel.toggleLecturePractice(task.lectureId) },
                    playbackSpeed = planCalc.playbackSpeed
                )
            }
        }

        // Subject Progress Section
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SUBJECT PROGRESS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Fixed 2h Reference",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrepBluePrimary
                )
            }
        }

        items(subjectProgress) { item ->
            SubjectProgressCard(
                progressData = item,
                onClick = { viewModel.navigateTo(AppScreen.Syllabus) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "Reset & Reload Syllabus",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Current Exam: ${activeGoal?.examName ?: "Syllabus"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = PrepBluePrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Choose how you want to reset your prep:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.resetAndReloadSyllabus()
                            showResetDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reload Clean Default Syllabus")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            viewModel.resetAllProgress()
                            showResetDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset All Progress (Start Fresh 0%)")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            showResetDialog = false
                            viewModel.navigateTo(AppScreen.OnboardingSelectGoal)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Switch Exam / Change Goal")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun WorkloadPill(
    icon: ImageVector,
    title: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrepBluePrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    isPrimary: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isPrimary) PrepBluePrimary else MaterialTheme.colorScheme.surface,
        shadowElevation = if (isPrimary) 2.dp else 1.dp,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isPrimary) Color.White else PrepBluePrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPrimary) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TodayTaskCard(
    task: com.example.viewmodel.TodayTaskItem,
    onToggleComplete: () -> Unit,
    onToggleNotes: () -> Unit,
    onTogglePractice: () -> Unit,
    playbackSpeed: Float
) {
    val estimatedWatchMin = (120 / playbackSpeed).toInt()
    val estHours = estimatedWatchMin / 60
    val estMin = estimatedWatchMin % 60
    val estWatchStr = if (estMin > 0) "${estHours}h ${estMin}m" else "${estHours}h"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("today_task_item_${task.lectureId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (task.isCompleted) androidx.compose.foundation.BorderStroke(1.dp, PrepSuccess.copy(alpha = 0.5f)) else null
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lecture Checkbox
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = if (task.isCompleted) "Completed" else "Incomplete",
                    tint = if (task.isCompleted) PrepSuccess else MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable(onClick = onToggleComplete)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = try {
                                Color(android.graphics.Color.parseColor(task.subjectColor)).copy(alpha = 0.15f)
                            } catch (e: Exception) {
                                PrepBlueLight
                            }
                        ) {
                            Text(
                                text = task.subjectName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = try {
                                    Color(android.graphics.Color.parseColor(task.subjectColor))
                                } catch (e: Exception) {
                                    PrepBluePrimary
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ref: 2h • Watch: $estWatchStr",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = task.lectureTitle.ifEmpty { "Lecture ${task.lectureNumber}: ${task.chapterName}" },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-tasks: Notes & Practice
            Row(
                modifier = Modifier.padding(start = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onToggleNotes)
                ) {
                    Checkbox(
                        checked = task.notesCompleted,
                        onCheckedChange = { onToggleNotes() },
                        colors = CheckboxDefaults.colors(checkedColor = PrepBluePrimary),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Notes",
                        fontSize = 12.sp,
                        color = if (task.notesCompleted) PrepBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onTogglePractice)
                ) {
                    Checkbox(
                        checked = task.practiceCompleted,
                        onCheckedChange = { onTogglePractice() },
                        colors = CheckboxDefaults.colors(checkedColor = PrepBluePrimary),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Practice / DPP",
                        fontSize = 12.sp,
                        color = if (task.practiceCompleted) PrepBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectProgressCard(
    progressData: com.example.viewmodel.SubjectProgressData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
            .testTag("subject_progress_card_${progressData.subject.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = progressData.subject.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${progressData.percent}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrepBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (progressData.percent / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PrepBluePrimary,
                trackColor = PrepBlueLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${progressData.completedLectures} / ${progressData.totalLectures} lectures",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Ref: ${progressData.completedReferenceHours.toInt()}/${progressData.referenceHours.toInt()}h",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
