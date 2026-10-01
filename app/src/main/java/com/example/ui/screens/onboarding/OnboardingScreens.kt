package com.example.ui.screens.onboarding

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
import com.example.ui.components.GlobalBackButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.defaultdata.DefaultSubjectTemplate
import com.example.data.defaultdata.DefaultSyllabusCatalog
import com.example.data.defaultdata.StreamSyllabusCatalog
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepStreakOrange
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun OnboardingWelcomeScreen(
    onBack: (() -> Unit)? = null,
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        if (onBack != null) {
            GlobalBackButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Hero Icon / Badge
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(PrepBluePrimary, PrepBlueDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "PREP TRACK",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp,
                    letterSpacing = 1.sp
                ),
                color = PrepBluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Plan • Focus • Track • Achieve",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Value Prop Cards
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    FeatureRow(
                        icon = Icons.Default.Speed,
                        title = "Fixed 2-Hour Lecture Reference",
                        desc = "Objective progress baseline unaffected by playback speed"
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    FeatureRow(
                        icon = Icons.Default.Timer,
                        title = "Separate Watch & Focus Time",
                        desc = "Measure actual Pomodoro focus sessions independently"
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    FeatureRow(
                        icon = Icons.Default.LocalFireDepartment,
                        title = "Adaptive Workload Planning",
                        desc = "Auto-calculates daily hours with missed-day adjustments"
                    )
                }
            }
        }

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .testTag("onboarding_get_started_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
        ) {
            Text(
                text = "Get Started",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun OnboardingSelectGoalScreen(
    viewModel: MainViewModel,
    onBack: (() -> Unit)? = null,
    onComplete: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("School Boards") }
    var selectedExamName by remember { mutableStateOf("CBSE Class 12") }
    var selectedClass by remember { mutableStateOf("Class 12") }
    var selectedBoard by remember { mutableStateOf("CBSE") }
    var selectedYear by remember { mutableStateOf("2026-27") }
    var customExamNameInput by remember { mutableStateOf("") }

    // Stream Selection: Science, Commerce, Humanities, Vocational, Custom
    var selectedStreamId by remember { mutableStateOf("SCIENCE") }
    var selectedCombinationName by remember { mutableStateOf("PCB") }

    // Selected Subjects List (checked subject names)
    val selectedSubjectNames = remember {
        mutableStateListOf("Physics", "Chemistry", "Biology", "English")
    }

    // Custom user-added subjects
    val customSubjectsList = remember {
        mutableStateListOf<DefaultSubjectTemplate>()
    }
    var showAddCustomSubjectDialog by remember { mutableStateOf(false) }
    var customSubjectNameInput by remember { mutableStateOf("") }

    // Target Deadline Mode: "DAYS" or "CALENDAR"
    var deadlineMode by remember { mutableStateOf("DAYS") }
    var targetDays by remember { mutableIntStateOf(90) }
    var customDaysInput by remember { mutableStateOf("63") }

    // Calendar Picker State
    var targetYear by remember { mutableIntStateOf(2027) }
    var targetMonth by remember { mutableIntStateOf(3) } // 0=Jan, 3=Apr
    var targetDay by remember { mutableIntStateOf(27) }

    // Syllabus Review Modal / State
    var showReviewScreen by remember { mutableStateOf(false) }

    val categories = listOf(
        "School Boards",
        "Medical (NEET)",
        "Engineering (JEE)",
        "CUET / Universities",
        "Civil Services / Defense",
        "Custom Goal"
    )

    // Compute target timestamp & days
    val targetTimestampAndDays by remember(deadlineMode, targetDays, targetYear, targetMonth, targetDay) {
        derivedStateOf {
            val now = Calendar.getInstance()
            if (deadlineMode == "CALENDAR") {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, targetYear)
                    set(Calendar.MONTH, targetMonth)
                    set(Calendar.DAY_OF_MONTH, targetDay)
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                }
                val diffDays = ((cal.timeInMillis - now.timeInMillis) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
                Pair(cal.timeInMillis, diffDays)
            } else {
                val cal = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, targetDays)
                }
                Pair(cal.timeInMillis, targetDays)
            }
        }
    }

    // Helper to update subjects when stream or preset changes
    fun applyStreamPreset(streamId: String, presetName: String? = null) {
        val stream = StreamSyllabusCatalog.getStreamById(streamId) ?: return
        val preset = stream.defaultCombinations.find { it.name.equals(presetName, ignoreCase = true) }
            ?: stream.defaultCombinations.firstOrNull()

        selectedSubjectNames.clear()
        if (preset != null) {
            selectedSubjectNames.addAll(preset.subjectNames)
            selectedCombinationName = preset.name
        } else {
            selectedSubjectNames.addAll(stream.defaultSubjectNames)
            selectedCombinationName = "Custom Combination"
        }
    }

    // System Back Handler
    androidx.activity.compose.BackHandler(enabled = showReviewScreen) {
        showReviewScreen = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (showReviewScreen) {
            // ==========================================
            // STEP 10: SYLLABUS REVIEW SCREEN ("Your Study Setup")
            // ==========================================
            val (finalTimestamp, finalDays) = targetTimestampAndDays
            val dateFmt = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(finalTimestamp)
            val streamDef = StreamSyllabusCatalog.getStreamById(selectedStreamId)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp)
            ) {
                item {
                    GlobalBackButton(
                        onClick = { showReviewScreen = false },
                        modifier = Modifier.testTag("review_back_button")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Your Study Setup",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Review your academic roadmap before generating your dynamic study plan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )
                }

                // Summary Overview Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = PrepBlueLight),
                        border = BorderStroke(1.5.dp, PrepBluePrimary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎯", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = selectedExamName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = PrepBluePrimary
                                    )
                                    Text(
                                        text = "Board: $selectedBoard • Session: $selectedYear",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (streamDef != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = PrepBluePrimary.copy(alpha = 0.15f))
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(streamDef.iconEmoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Stream: ${streamDef.name} ($selectedCombinationName)",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = PrepBluePrimary.copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = PrepBluePrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Target: $finalDays Days ($dateFmt)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Selected Subjects Checklist Preview
                item {
                    Text(
                        text = "SELECTED SUBJECTS (${selectedSubjectNames.size})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = PrepBluePrimary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        selectedSubjectNames.forEach { sName ->
                            val customMatch = customSubjectsList.find { it.name.equals(sName, ignoreCase = true) }
                            val subTemplate = customMatch ?: StreamSyllabusCatalog.getSubjectTemplate(sName, selectedExamName)

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = PrepSuccess,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = sName,
                                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                if (customMatch != null) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = PrepStreakOrange.copy(alpha = 0.15f)
                                                    ) {
                                                        Text(
                                                            text = "CUSTOM",
                                                            color = PrepStreakOrange,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }
                                            Text(
                                                text = "${subTemplate.chapters.size} Chapters • Fixed 2h Lecture Baseline",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }

                // Decision Prompts: "Looks good?"
                item {
                    Text(
                        text = "Looks good?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "You can customize or add custom subjects at any time later in Syllabus settings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 18.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showReviewScreen = false },
                            modifier = Modifier.weight(1f).height(52.dp).testTag("review_edit_subjects_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text("Edit Subjects", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                val finalName = if (selectedCategory == "Custom Goal" && customExamNameInput.isNotBlank()) {
                                    customExamNameInput
                                } else {
                                    selectedExamName
                                }
                                viewModel.completeOnboarding(
                                    category = selectedCategory,
                                    examName = finalName,
                                    year = selectedYear,
                                    days = finalDays,
                                    customTargetTimestamp = finalTimestamp,
                                    board = selectedBoard,
                                    stream = streamDef?.name ?: "General",
                                    selectedSubjectNames = selectedSubjectNames.toList(),
                                    customSubjects = customSubjectsList.toList()
                                )
                                onComplete()
                            },
                            modifier = Modifier.weight(1.3f).height(52.dp).testTag("review_confirm_plan_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        } else {
            // ==========================================
            // CONDITIONAL MULTI-STEP SETUP WIZARD
            // ==========================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 48.dp)
            ) {
                item {
                    if (onBack != null) {
                        Box(modifier = Modifier.padding(bottom = 16.dp)) {
                            GlobalBackButton(
                                onClick = onBack,
                                modifier = Modifier.testTag("goal_selection_back_button")
                            )
                        }
                    }
                    Text(
                        text = "Setup Your Study Goal",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Select your academic path, stream, and exact subject combination.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                    )
                }

                // STEP 1: CATEGORY SELECTION
                item {
                    Text(
                        text = "1. CHOOSE GOAL / EXAM PATH",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = PrepBluePrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        items(categories) { category ->
                            val isSelected = category == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .clickable {
                                        selectedCategory = category
                                        if (category == "Medical (NEET)") {
                                            selectedExamName = "NEET"
                                            selectedStreamId = "SCIENCE"
                                            applyStreamPreset("SCIENCE", "PCB")
                                        } else if (category == "Engineering (JEE)") {
                                            selectedExamName = "JEE Main"
                                            selectedStreamId = "SCIENCE"
                                            applyStreamPreset("SCIENCE", "PCM")
                                        } else if (category == "School Boards") {
                                            selectedExamName = "CBSE Class 12"
                                            selectedClass = "Class 12"
                                            selectedBoard = "CBSE"
                                            applyStreamPreset(selectedStreamId, selectedCombinationName)
                                        }
                                    }
                                    .testTag("category_pill_${category.replace(" ", "_")}")
                            ) {
                                Text(
                                    text = category,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else PrepBlueDark,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // STEP 2: CLASS / LEVEL (Conditional for School Boards)
                if (selectedCategory == "School Boards" || selectedCategory == "CUET / Universities") {
                    item {
                        Text(
                            text = "2. CHOOSE CLASS / LEVEL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = PrepBluePrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Class 12", "Class 11", "Class 10").forEach { cls ->
                                val isChosen = selectedClass == cls
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                                    border = if (!isChosen) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            selectedClass = cls
                                            selectedExamName = "$selectedBoard $cls"
                                            applyStreamPreset(selectedStreamId, selectedCombinationName)
                                        }
                                ) {
                                    Text(
                                        text = cls,
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }

                    // STEP 3: BOARD / AUTHORITY
                    item {
                        Text(
                            text = "3. CHOOSE BOARD / AUTHORITY",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = PrepBluePrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("CBSE", "CISCE / ISC", "State Board").forEach { brd ->
                                val isChosen = selectedBoard == brd
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                                    border = if (!isChosen) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            selectedBoard = brd
                                            selectedExamName = "$brd $selectedClass"
                                        }
                                ) {
                                    Text(
                                        text = brd,
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                } else if (selectedCategory == "Custom Goal") {
                    item {
                        Text(
                            text = "2. ENTER GOAL / EXAM NAME",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = PrepBluePrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = customExamNameInput,
                            onValueChange = {
                                customExamNameInput = it
                                selectedExamName = if (it.isNotBlank()) it else "Custom Exam"
                            },
                            placeholder = { Text("e.g. State PSC, Olympiad, College Exam, etc.") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                } else {
                    // For Medical, Engineering, Defense
                    item {
                        Text(
                            text = "2. TARGET EXAM",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = PrepBluePrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        val exams = when (selectedCategory) {
                            "Medical (NEET)" -> listOf("NEET", "AIIMS Paramedical", "NEET PG")
                            "Engineering (JEE)" -> listOf("JEE Main", "JEE Advanced", "BITSAT")
                            "Civil Services / Defense" -> listOf("NDA", "UPSC Prelims", "SSC CGL")
                            else -> listOf("CUET UG", "CUET PG")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            exams.forEach { ex ->
                                val isChosen = selectedExamName == ex
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                                    border = if (!isChosen) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            selectedExamName = ex
                                            if (ex == "NEET") applyStreamPreset("SCIENCE", "PCB")
                                            else if (ex.contains("JEE")) applyStreamPreset("SCIENCE", "PCM")
                                        }
                                ) {
                                    Text(
                                        text = ex,
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.5.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // STEP 4: TARGET SESSION / ACADEMIC YEAR
                item {
                    Text(
                        text = "4. TARGET ACADEMIC YEAR / SESSION",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = PrepBluePrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("2026-27", "2027", "2028").forEach { yr ->
                            val isChosen = yr == selectedYear
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                                border = if (!isChosen) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedYear = yr }
                            ) {
                                Text(
                                    text = yr,
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }

                // ==========================================
                // STEP 5: STREAM SELECTION (CRITICAL FOR CLASS 11/12)
                // ==========================================
                item {
                    Text(
                        text = "5. CHOOSE YOUR STREAM / ACADEMIC PATH",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = PrepBluePrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                        StreamSyllabusCatalog.streams.forEach { stream ->
                            val isChosen = stream.id == selectedStreamId
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedStreamId = stream.id
                                        applyStreamPreset(stream.id)
                                    }
                                    .testTag("stream_card_${stream.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isChosen) PrepBlueLight else MaterialTheme.colorScheme.surface
                                ),
                                border = if (isChosen) BorderStroke(1.5.dp, PrepBluePrimary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = stream.iconEmoji, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stream.name,
                                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.SemiBold,
                                            fontSize = 15.sp,
                                            color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = stream.description,
                                            fontSize = 11.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isChosen) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = PrepBluePrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // STEP 6: SUBJECT COMBINATION PRESETS
                // ==========================================
                val currentStream = StreamSyllabusCatalog.getStreamById(selectedStreamId)
                if (currentStream != null && currentStream.defaultCombinations.isNotEmpty()) {
                    item {
                        Text(
                            text = "6. SUBJECT COMBINATION PRESETS (${currentStream.name})",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = PrepBluePrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            items(currentStream.defaultCombinations) { preset ->
                                val isChosen = selectedCombinationName == preset.name
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                                    border = if (!isChosen) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                                    modifier = Modifier
                                        .clickable {
                                            selectedCombinationName = preset.name
                                            selectedSubjectNames.clear()
                                            selectedSubjectNames.addAll(preset.subjectNames)
                                        }
                                        .testTag("preset_chip_${preset.name.replace(" ", "_")}")
                                ) {
                                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                                        Text(
                                            text = preset.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${preset.subjectNames.size} Subjects",
                                            fontSize = 10.5.sp,
                                            color = if (isChosen) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // STEP 7: DYNAMIC SUBJECT CHECKLIST & CUSTOM SUBJECTS
                // ==========================================
                item {
                    Text(
                        text = "7. CHOOSE & CUSTOMIZE SUBJECTS (${selectedSubjectNames.size} Selected)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = PrepBluePrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = "Check/uncheck subjects or add your own school-specific subjects.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    // Available subjects for this stream
                    val availableStreamSubjects = remember(selectedStreamId) {
                        StreamSyllabusCatalog.getAvailableSubjectNamesForStream(selectedStreamId)
                    }

                    // Combined list of stream subjects + any user custom subjects
                    val allCandidateSubjects = (availableStreamSubjects + customSubjectsList.map { it.name } + selectedSubjectNames).distinct()

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        allCandidateSubjects.forEach { sName ->
                            val isChecked = selectedSubjectNames.contains(sName)
                            val isCustom = customSubjectsList.any { it.name.equals(sName, ignoreCase = true) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) {
                                            if (selectedSubjectNames.size > 1) {
                                                selectedSubjectNames.remove(sName)
                                            }
                                        } else {
                                            selectedSubjectNames.add(sName)
                                        }
                                    }
                                    .testTag("subject_checkbox_$sName"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isChecked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                ),
                                border = if (isChecked) BorderStroke(1.2.dp, PrepBluePrimary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { check ->
                                                if (check) {
                                                    if (!selectedSubjectNames.contains(sName)) selectedSubjectNames.add(sName)
                                                } else {
                                                    if (selectedSubjectNames.size > 1) selectedSubjectNames.remove(sName)
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = PrepBluePrimary)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = sName,
                                                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 14.sp,
                                                    color = if (isChecked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                if (isCustom) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Surface(shape = RoundedCornerShape(4.dp), color = PrepStreakOrange.copy(alpha = 0.15f)) {
                                                        Text("CUSTOM", fontSize = 8.5.sp, color = PrepStreakOrange, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (isCustom) {
                                        IconButton(
                                            onClick = {
                                                selectedSubjectNames.remove(sName)
                                                customSubjectsList.removeAll { it.name.equals(sName, ignoreCase = true) }
                                            }
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete custom subject", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // + ADD CUSTOM SUBJECT BUTTON & INLINE BOX
                    if (showAddCustomSubjectDialog) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = PrepBlueLight),
                            border = BorderStroke(1.dp, PrepBluePrimary)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Add Custom Subject", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PrepBluePrimary)
                                Text("Enter any school-specific or regional subject name", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = customSubjectNameInput,
                                    onValueChange = { customSubjectNameInput = it },
                                    placeholder = { Text("e.g. Fine Arts, Physical Education, French") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    TextButton(onClick = { showAddCustomSubjectDialog = false }) {
                                        Text("Cancel")
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Button(
                                        onClick = {
                                            val trimmed = customSubjectNameInput.trim()
                                            if (trimmed.isNotBlank()) {
                                                val newSubTemplate = StreamSyllabusCatalog.createCustomSubjectTemplate(trimmed)
                                                customSubjectsList.add(newSubTemplate)
                                                if (!selectedSubjectNames.contains(trimmed)) {
                                                    selectedSubjectNames.add(trimmed)
                                                }
                                                customSubjectNameInput = ""
                                                showAddCustomSubjectDialog = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                                    ) {
                                        Text("Add Subject", color = Color.White)
                                    }
                                }
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = { showAddCustomSubjectDialog = true },
                            modifier = Modifier.fillMaxWidth().testTag("add_custom_subject_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("+ Add Custom Subject", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }

                // ==========================================
                // STEP 8: COMPLETION DEADLINE TARGET
                // ==========================================
                item {
                    Text(
                        text = "8. COMPLETION DEADLINE TARGET",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = PrepBluePrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Switch between Days vs Calendar Date
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (deadlineMode == "DAYS") PrepBluePrimary else PrepBlueLight,
                            modifier = Modifier.weight(1f).clickable { deadlineMode = "DAYS" }
                        ) {
                            Text(
                                text = "Days Target",
                                fontWeight = if (deadlineMode == "DAYS") FontWeight.Bold else FontWeight.Medium,
                                color = if (deadlineMode == "DAYS") Color.White else PrepBlueDark,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (deadlineMode == "CALENDAR") PrepBluePrimary else PrepBlueLight,
                            modifier = Modifier.weight(1f).clickable { deadlineMode = "CALENDAR" }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = if (deadlineMode == "CALENDAR") Color.White else PrepBlueDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Calendar Date",
                                    fontWeight = if (deadlineMode == "CALENDAR") FontWeight.Bold else FontWeight.Medium,
                                    color = if (deadlineMode == "CALENDAR") Color.White else PrepBlueDark,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    if (deadlineMode == "DAYS") {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(30, 45, 60, 63, 90, 120, 180).forEach { days ->
                                        val isChosen = targetDays == days
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.weight(1f).clickable { targetDays = days }
                                        ) {
                                            Text(
                                                text = "${days}d",
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedTextField(
                                        value = customDaysInput,
                                        onValueChange = {
                                            customDaysInput = it.filter { c -> c.isDigit() }
                                            val parsed = customDaysInput.toIntOrNull()
                                            if (parsed != null && parsed > 0) {
                                                targetDays = parsed
                                            }
                                        },
                                        label = { Text("Or enter exact days (e.g. 63)") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        // Calendar Date Picker (Exact Date Selector)
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Pick Target Date", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(10.dp))

                                // Year Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(2026, 2027, 2028, 2029).forEach { y ->
                                        val isChosen = targetYear == y
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isChosen) PrepBluePrimary else PrepBlueLight,
                                            modifier = Modifier.weight(1f).clickable { targetYear = y }
                                        ) {
                                            Text(
                                                text = "$y",
                                                fontSize = 12.sp,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isChosen) Color.White else PrepBlueDark,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Months
                                val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    months.take(6).forEachIndexed { index, mName ->
                                        val isChosen = targetMonth == index
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.weight(1f).clickable { targetMonth = index }
                                        ) {
                                            Text(
                                                text = mName,
                                                fontSize = 11.sp,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    months.drop(6).forEachIndexed { index, mName ->
                                        val realIndex = index + 6
                                        val isChosen = targetMonth == realIndex
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.weight(1f).clickable { targetMonth = realIndex }
                                        ) {
                                            Text(
                                                text = mName,
                                                fontSize = 11.sp,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Days Quick Selectors
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(1, 10, 15, 20, 27, 30).forEach { d ->
                                        val isChosen = targetDay == d
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.weight(1f).clickable { targetDay = d }
                                        ) {
                                            Text(
                                                text = "$d",
                                                fontSize = 12.sp,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Target Summary Pill
                    val (calculatedTimestamp, computedDays) = targetTimestampAndDays
                    val dateFmt = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calculatedTimestamp)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF6FF),
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = PrepBluePrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "📅 Target Date: $dateFmt • $computedDays Days Remaining",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrepBluePrimary
                            )
                        }
                    }
                }

                // ==========================================
                // STEP 9: REVIEW SETUP BUTTON (OPENS REVIEW SCREEN)
                // ==========================================
                item {
                    Spacer(modifier = Modifier.height(28.dp))

                    val (_, finalDays) = targetTimestampAndDays

                    Button(
                        onClick = {
                            showReviewScreen = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("onboarding_review_setup_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                    ) {
                        Text(
                            text = "Review Study Setup (${selectedSubjectNames.size} Subjects • $finalDays Days)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(PrepBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrepBluePrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
