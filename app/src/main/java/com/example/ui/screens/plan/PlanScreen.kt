package com.example.ui.screens.plan

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepStreakOrange
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel

@Composable
fun PlanScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val planCalc by viewModel.planCalculation.collectAsStateWithLifecycle()
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val timetable by viewModel.timetable.collectAsStateWithLifecycle()

    val currentSpeed = studyPlan?.playbackSpeed ?: 1.0f
    val currentNotesMin = studyPlan?.notesMinutesPerLecture ?: 30
    val currentPracticeMin = studyPlan?.practiceMinutesDaily ?: 60
    val currentRevisionMin = studyPlan?.revisionMinutesDaily ?: 30
    val currentDays = studyPlan?.targetDays ?: 90
    val currentPattern = studyPlan?.studyPattern ?: "WEEKLY_TIMETABLE"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Daily Workload Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrepBluePrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ESTIMATED DAILY WORKLOAD",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${planCalc.targetDays} Days Target",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${planCalc.dailyWorkloadFormatted} / day",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Total Workload: ${planCalc.totalWorkloadFormatted} • ${planCalc.totalLectures} Total Lectures",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // CRITICAL 2-HOUR REFERENCE RULE CARD
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrepBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrepBluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Fixed 2-Hour Lecture Reference",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Standard reference: 1 Lecture = 2.0 Reference Hours",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Total Lectures",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${planCalc.totalLectures}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column {
                            Text(
                                text = "Reference Workload",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${planCalc.totalReferenceHours.toInt()} Hours",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrepBluePrimary
                            )
                        }
                        Column {
                            Text(
                                text = "Watch at ${planCalc.playbackSpeed}×",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = planCalc.estimatedWatchHoursFormatted,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrepSuccess
                            )
                        }
                    }
                }
            }
        }

        // Section: Target Days
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
                        text = "FINISH DEADLINE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PrepBluePrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(30, 60, 90, 120, 180).forEach { days ->
                            val isSel = currentDays == days
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateTargetDays(days) }
                                    .testTag("plan_days_${days}")
                            ) {
                                Text(
                                    text = "$days d",
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else PrepBlueDark,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Playback Speed
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PLAYBACK SPEED",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PrepBluePrimary
                        )
                        Text(
                            text = "Watch: ${planCalc.estimatedWatchHoursFormatted}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrepSuccess
                        )
                    }
                    Text(
                        text = "Changes estimated watch time only; 2h reference remains constant.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1.0f, 1.25f, 1.5f, 1.75f, 2.0f).forEach { spd ->
                            val isSel = currentSpeed == spd
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updatePlaybackSpeed(spd) }
                                    .testTag("plan_speed_${spd}")
                            ) {
                                Text(
                                    text = "${spd}×",
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else PrepBlueDark,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Notes Workload
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NOTES TIME PER LECTURE",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PrepBluePrimary
                        )
                        Text(
                            text = "Total Notes: ${planCalc.totalNotesHours.toInt()}h",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0 to "None", 15 to "15m", 30 to "30m", 45 to "45m", 60 to "60m").forEach { (min, label) ->
                            val isSel = currentNotesMin == min
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateNotesTime(min) }
                                    .testTag("plan_notes_${min}")
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else PrepBlueDark,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Practice & Revision
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
                        text = "DAILY PRACTICE / DPP",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PrepBluePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0 to "None", 30 to "30m", 60 to "1h", 90 to "1.5h", 120 to "2h").forEach { (min, label) ->
                            val isSel = currentPracticeMin == min
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updatePracticeTime(min) }
                                    .testTag("plan_practice_${min}")
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else PrepBlueDark,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "DAILY REVISION",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PrepBluePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0 to "None", 30 to "30m", 60 to "1h").forEach { (min, label) ->
                            val isSel = currentRevisionMin == min
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateRevisionTime(min) }
                                    .testTag("plan_revision_${min}")
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else PrepBlueDark,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Study Patterns
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "STUDY PATTERN",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PrepBluePrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PatternOptionCard(
                            icon = Icons.Default.ViewWeek,
                            title = "Weekly Timetable",
                            subtitle = "Distribute subjects by day",
                            isSelected = currentPattern == "WEEKLY_TIMETABLE",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.updateStudyPattern("WEEKLY_TIMETABLE") },
                            testTag = "pattern_weekly_timetable"
                        )
                        PatternOptionCard(
                            icon = Icons.Default.ViewAgenda,
                            title = "One By One",
                            subtitle = "Master one subject sequentially",
                            isSelected = currentPattern == "ONE_BY_ONE",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.updateStudyPattern("ONE_BY_ONE") },
                            testTag = "pattern_one_by_one"
                        )
                    }

                    if (currentPattern == "WEEKLY_TIMETABLE" && subjects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "WEEKLY SUBJECT ASSIGNMENT",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val days = listOf("Mon" to 1, "Tue" to 2, "Wed" to 3, "Thu" to 4, "Fri" to 5, "Sat" to 6, "Sun" to 7)
                        days.forEach { (dayName, dayNum) ->
                            val assigned = timetable.find { it.dayOfWeek == dayNum }
                            val sub = subjects.find { it.id == assigned?.subjectId } ?: subjects.firstOrNull()
                            var expanded by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = dayName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Box {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PrepBlueLight,
                                        modifier = Modifier.clickable { expanded = true }
                                    ) {
                                        Text(
                                            text = sub?.name ?: "Select",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = PrepBlueDark,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        subjects.forEach { s ->
                                            DropdownMenuItem(
                                                text = { Text(s.name) },
                                                onClick = {
                                                    viewModel.assignSubjectToDay(dayNum, s.id)
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PatternOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) PrepBlueLight else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, PrepBluePrimary) else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) PrepBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) PrepBluePrimary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
