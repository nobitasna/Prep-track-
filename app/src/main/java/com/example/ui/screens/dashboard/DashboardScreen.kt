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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
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
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
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
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()

    var showMissedAdjustDialog by remember { mutableStateOf(false) }

    val completedLecturesToday = todayTasks.count { it.isCompleted }
    val totalLecturesToday = todayTasks.size
    val dailyProgressFraction = if (totalLecturesToday > 0) completedLecturesToday.toFloat() / totalLecturesToday else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Welcome Header & Current Exam Goal
        item {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val greeting = when {
                hour < 12 -> "Good Morning"
                hour < 17 -> "Good Afternoon"
                else -> "Good Evening"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrepBlueLight),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$greeting, Scholar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = PrepBlueDark
                        )
                        Text(
                            text = "${activeGoal?.examName ?: "PREP TRACK"} (${activeGoal?.targetYear ?: "Target"})",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = PrepBluePrimary
                        )
                        Text(
                            text = "${planCalc.targetDays} days plan • ${planCalc.playbackSpeed}× speed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Streak Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = PrepStreakOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${userStreak?.currentStreak ?: 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = PrepStreakOrange
                                )
                            }
                            Text(
                                text = "Day Streak",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
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

        // Today's Plan & Workload Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TODAY'S PLAN",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = PrepBluePrimary
                            )
                            Text(
                                text = "Required Workload: ${planCalc.dailyWorkloadFormatted}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Circular Progress Ring
                        ProgressRing(
                            progress = dailyProgressFraction,
                            modifier = Modifier.size(68.dp),
                            strokeWidth = 7.dp,
                            progressColor = PrepBluePrimary,
                            backgroundColor = PrepBlueLight
                        ) {
                            Text(
                                text = "${(dailyProgressFraction * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = PrepBluePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Workload Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WorkloadPill(
                            icon = Icons.Default.Speed,
                            title = "Lectures",
                            value = "$completedLecturesToday / ${if (totalLecturesToday == 0) planCalc.totalLectures.coerceAtMost(4) else totalLecturesToday}"
                        )
                        WorkloadPill(
                            icon = Icons.Default.EditNote,
                            title = "Notes",
                            value = "${planCalc.notesMinutesPerLecture}m/lec"
                        )
                        WorkloadPill(
                            icon = Icons.Default.FitnessCenter,
                            title = "Practice",
                            value = "${planCalc.dailyPracticeMinutes}m"
                        )
                        WorkloadPill(
                            icon = Icons.Default.Timer,
                            title = "Revision",
                            value = "${planCalc.dailyRevisionMinutes}m"
                        )
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
                    text = "$completedLecturesToday / $totalLecturesToday Done",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (completedLecturesToday == totalLecturesToday && totalLecturesToday > 0) PrepSuccess else PrepBluePrimary
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
