package com.example.ui.screens.syllabus

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.SubjectEntity
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

@Composable
fun SyllabusScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val lectures by viewModel.lectures.collectAsStateWithLifecycle()
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()

    val playbackSpeed = studyPlan?.playbackSpeed ?: 1.0f

    var selectedSubjectId by remember(subjects) {
        mutableStateOf(subjects.firstOrNull()?.id ?: 0L)
    }

    var showHiddenItems by remember { mutableStateOf(false) }
    var showAddChapterDialog by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var editingChapter by remember { mutableStateOf<ChapterEntity?>(null) }

    val activeSubject = subjects.find { it.id == selectedSubjectId } ?: subjects.firstOrNull()

    val subjectChapters = remember(chapters, activeSubject, showHiddenItems) {
        if (activeSubject == null) emptyList()
        else chapters.filter { it.subjectId == activeSubject.id && (showHiddenItems || !it.isHidden) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp)
        ) {
            // Subject Switcher Tabs
            item {
                Text(
                    text = "SUBJECTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(subjects) { subject ->
                        val isSelected = subject.id == activeSubject?.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                            shadowElevation = if (isSelected) 2.dp else 1.dp,
                            modifier = Modifier
                                .clickable { selectedSubjectId = subject.id }
                                .testTag("subject_tab_${subject.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            try {
                                                Color(android.graphics.Color.parseColor(subject.colorHex))
                                            } catch (e: Exception) {
                                                PrepBluePrimary
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = subject.name,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrepBlueLight,
                            modifier = Modifier
                                .clickable { showAddSubjectDialog = true }
                                .testTag("add_subject_tab_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Subject",
                                    tint = PrepBluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Add",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = PrepBlueDark
                                )
                            }
                        }
                    }
                }
            }

            // Subject Info Header
            if (activeSubject != null) {
                item {
                    val subLectures = lectures.filter { lec ->
                        chapters.any { ch -> ch.id == lec.chapterId && ch.subjectId == activeSubject.id }
                    }
                    val totalLecs = subLectures.size
                    val compLecs = subLectures.count { it.isCompleted }
                    val percent = if (totalLecs > 0) (compLecs * 100) / totalLecs else 0

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
                                Column {
                                    Text(
                                        text = activeSubject.name,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${subjectChapters.size} Chapters • $compLecs/$totalLecs Lectures Completed",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "$percent%",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = PrepBluePrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { (percent / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = PrepBluePrimary,
                                trackColor = PrepBlueLight
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Show / Restore Hidden Items Switch
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (showHiddenItems) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Show / Restore Removed Items",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = showHiddenItems,
                                    onCheckedChange = { showHiddenItems = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = PrepBluePrimary)
                                )
                            }
                        }
                    }
                }
            }

            // Chapter List Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHAPTERS & LECTURES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${subjectChapters.size} total",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrepBluePrimary
                    )
                }
            }

            if (subjectChapters.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No chapters found in this subject.",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { showAddChapterDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Chapter")
                            }
                        }
                    }
                }
            } else {
                items(subjectChapters) { chapter ->
                    val chapLecs = lectures.filter { it.chapterId == chapter.id }
                    val completedCount = chapLecs.count { it.isCompleted }
                    val refHours = chapter.totalLectures * 2.0
                    val estWatchHours = refHours / playbackSpeed

                    ChapterListItem(
                        chapter = chapter,
                        completedLectures = completedCount,
                        totalLectures = chapter.totalLectures,
                        referenceHours = refHours,
                        estimatedWatchHours = estWatchHours,
                        onOpenChapter = { viewModel.navigateTo(AppScreen.ChapterDetail(chapter.id)) },
                        onToggleImportant = { viewModel.toggleChapterImportant(chapter.id) },
                        onToggleHidden = { viewModel.toggleChapterHidden(chapter.id, !chapter.isHidden) },
                        onEdit = { editingChapter = chapter },
                        onDelete = { viewModel.deleteChapter(chapter) }
                    )
                }
            }
        }

        // Floating Action Button to Add Chapter
        FloatingActionButton(
            onClick = { showAddChapterDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_add_chapter"),
            containerColor = PrepBluePrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Chapter")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add Chapter", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Add Chapter Dialog
    if (showAddChapterDialog && activeSubject != null) {
        var chapterName by remember { mutableStateOf("") }
        var lectureCountText by remember { mutableStateOf("6") }
        var isImportant by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddChapterDialog = false },
            title = { Text("Add Chapter to ${activeSubject.name}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = chapterName,
                        onValueChange = { chapterName = it },
                        label = { Text("Chapter Name") },
                        placeholder = { Text("e.g. Modern Physics") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = lectureCountText,
                        onValueChange = { lectureCountText = it.filter { char -> char.isDigit() } },
                        label = { Text("Number of Lectures (Fixed 2h each)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val count = lectureCountText.toIntOrNull() ?: 4
                        if (chapterName.isNotBlank()) {
                            viewModel.addChapter(activeSubject.id, chapterName, count, isImportant)
                            showAddChapterDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddChapterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Subject Dialog
    if (showAddSubjectDialog) {
        var subjectName by remember { mutableStateOf("") }
        var chosenColor by remember { mutableStateOf("#1A73E8") }

        AlertDialog(
            onDismissRequest = { showAddSubjectDialog = false },
            title = { Text("Add Custom Subject") },
            text = {
                Column {
                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = { subjectName = it },
                        label = { Text("Subject Name") },
                        placeholder = { Text("e.g. Zoology, General Studies") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Choose Accent Color", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("#1A73E8", "#10B981", "#8B5CF6", "#F59E0B", "#EF4444").forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(hex)))
                                    .clickable { chosenColor = hex }
                                    .then(
                                        if (chosenColor == hex) Modifier.border(BorderStroke(3.dp, Color.DarkGray), CircleShape)
                                        else Modifier
                                    )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (subjectName.isNotBlank()) {
                            viewModel.addCustomSubject(subjectName, chosenColor)
                            showAddSubjectDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text("Add Subject")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubjectDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Chapter Dialog
    editingChapter?.let { chap ->
        var editName by remember { mutableStateOf(chap.name) }
        var editLectures by remember { mutableStateOf(chap.totalLectures.toString()) }

        AlertDialog(
            onDismissRequest = { editingChapter = null },
            title = { Text("Edit Chapter") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Chapter Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editLectures,
                        onValueChange = { editLectures = it.filter { c -> c.isDigit() } },
                        label = { Text("Total Lectures") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newCount = editLectures.toIntOrNull() ?: chap.totalLectures
                        viewModel.updateChapter(chap.copy(name = editName, totalLectures = newCount))
                        editingChapter = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingChapter = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ChapterListItem(
    chapter: ChapterEntity,
    completedLectures: Int,
    totalLectures: Int,
    referenceHours: Double,
    estimatedWatchHours: Double,
    onOpenChapter: () -> Unit,
    onToggleImportant: () -> Unit,
    onToggleHidden: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable(onClick = onOpenChapter)
            .testTag("chapter_card_${chapter.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (chapter.isHidden) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Star Important
                IconButton(
                    onClick = onToggleImportant,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (chapter.isImportant) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Important",
                        tint = if (chapter.isImportant) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$completedLectures / $totalLectures Lectures • Ref: ${referenceHours.toInt()}h • Watch: ${String.format("%.1f", estimatedWatchHours)}h",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Chapter") },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (chapter.isHidden) "Restore Chapter" else "Hide Chapter") },
                            leadingIcon = {
                                Icon(
                                    if (chapter.isHidden) Icons.Default.Restore else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onToggleHidden()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Chapter") },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View Lectures",
                    tint = PrepBluePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val progressFrac = if (totalLectures > 0) completedLectures.toFloat() / totalLectures else 0f
            LinearProgressIndicator(
                progress = { progressFrac },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progressFrac >= 1f) PrepSuccess else PrepBluePrimary,
                trackColor = PrepBlueLight
            )
        }
    }
}
