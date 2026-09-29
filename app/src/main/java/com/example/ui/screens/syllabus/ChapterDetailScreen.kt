package com.example.ui.screens.syllabus

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.local.entity.LectureEntity
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterDetailScreen(
    chapterId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val lectures by viewModel.lectures.collectAsStateWithLifecycle()
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()
    val playbackSpeed = studyPlan?.playbackSpeed ?: 1.0f

    val chapter = chapters.find { it.id == chapterId }
    val chapterLectures = lectures.filter { it.chapterId == chapterId }.sortedBy { it.lectureNumber }

    val completedCount = chapterLectures.count { it.isCompleted }
    val totalCount = chapterLectures.size
    val referenceHours = totalCount * 2.0
    val estimatedWatchHours = referenceHours / playbackSpeed

    var selectedLectureForNote by remember { mutableStateOf<LectureEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = chapter?.name ?: "Chapter Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("chapter_detail_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Chapter Summary Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "PROGRESS: $completedCount / $totalCount LECTURES",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = PrepBluePrimary
                                )
                                Text(
                                    text = "${if (totalCount > 0) (completedCount * 100) / totalCount else 0}% Completed",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val progressFrac = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
                        LinearProgressIndicator(
                            progress = { progressFrac },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (progressFrac >= 1f) PrepSuccess else PrepBluePrimary,
                            trackColor = PrepBlueLight
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Fixed Reference", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${referenceHours.toInt()} Hours", fontWeight = FontWeight.Bold, color = PrepBluePrimary)
                            }
                            Column {
                                Text("Estimated Watch (${playbackSpeed}×)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${String.format("%.1f", estimatedWatchHours)} Hours", fontWeight = FontWeight.Bold, color = PrepSuccess)
                            }
                            Column {
                                Text("Per Lecture Ref", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("2.0 Hours", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "INDIVIDUAL LECTURE TRACKER",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }

            items(chapterLectures) { lecture ->
                LectureItemCard(
                    lecture = lecture,
                    playbackSpeed = playbackSpeed,
                    onToggleComplete = { viewModel.toggleLectureCompleted(lecture.id) },
                    onToggleImportant = { viewModel.toggleLectureImportant(lecture.id) },
                    onToggleNotes = { viewModel.toggleLectureNotes(lecture.id) },
                    onTogglePractice = { viewModel.toggleLecturePractice(lecture.id) },
                    onOpenNoteDialog = { selectedLectureForNote = lecture }
                )
            }
        }
    }

    // Personal Note Dialog
    selectedLectureForNote?.let { lec ->
        var noteContent by remember { mutableStateOf(lec.personalNote) }

        AlertDialog(
            onDismissRequest = { selectedLectureForNote = null },
            title = { Text("Lecture ${lec.lectureNumber} Note") },
            text = {
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("Personal Study Notes & Key Formulas") },
                    placeholder = { Text("e.g. Revise formula sheet on page 42, tricky numerical on friction...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.savePersonalNote(lec.id, noteContent)
                        selectedLectureForNote = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text("Save Note")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedLectureForNote = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LectureItemCard(
    lecture: LectureEntity,
    playbackSpeed: Float,
    onToggleComplete: () -> Unit,
    onToggleImportant: () -> Unit,
    onToggleNotes: () -> Unit,
    onTogglePractice: () -> Unit,
    onOpenNoteDialog: () -> Unit
) {
    val estimatedWatchMin = (120 / playbackSpeed).toInt()
    val estHours = estimatedWatchMin / 60
    val estMin = estimatedWatchMin % 60
    val estWatchStr = if (estMin > 0) "${estHours}h ${estMin}m" else "${estHours}h"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("lecture_row_${lecture.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (lecture.isCompleted) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (lecture.isCompleted) androidx.compose.foundation.BorderStroke(1.dp, PrepSuccess.copy(alpha = 0.5f)) else null
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Complete Checkmark Icon
                Icon(
                    imageVector = if (lecture.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = if (lecture.isCompleted) "Completed" else "Pending",
                    tint = if (lecture.isCompleted) PrepSuccess else MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable(onClick = onToggleComplete)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrepBlueLight
                        ) {
                            Text(
                                text = "Lecture ${lecture.lectureNumber}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PrepBluePrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fixed Ref: 2h • Watch: $estWatchStr",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = lecture.title.ifEmpty { "Lecture ${lecture.lectureNumber}" },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Star Important
                IconButton(
                    onClick = onToggleImportant,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (lecture.isImportant) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Important",
                        tint = if (lecture.isImportant) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Note Icon
                IconButton(
                    onClick = onOpenNoteDialog,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = "Personal Note",
                        tint = if (lecture.personalNote.isNotBlank()) PrepBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Sub tasks: Notes & Practice
            Row(
                modifier = Modifier.padding(start = 40.dp, top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onToggleNotes)
                ) {
                    Checkbox(
                        checked = lecture.notesCompleted,
                        onCheckedChange = { onToggleNotes() },
                        colors = CheckboxDefaults.colors(checkedColor = PrepBluePrimary),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Notes Made",
                        fontSize = 12.sp,
                        color = if (lecture.notesCompleted) PrepBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onTogglePractice)
                ) {
                    Checkbox(
                        checked = lecture.practiceCompleted,
                        onCheckedChange = { onTogglePractice() },
                        colors = CheckboxDefaults.colors(checkedColor = PrepBluePrimary),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Practice / DPP",
                        fontSize = 12.sp,
                        color = if (lecture.practiceCompleted) PrepBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (lecture.personalNote.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, top = 6.dp)
                ) {
                    Text(
                        text = lecture.personalNote,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
