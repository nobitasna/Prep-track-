package com.example.ui.screens.onboarding

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.defaultdata.DefaultSyllabusCatalog
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
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
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
    onComplete: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Medical") }
    var selectedExamName by remember { mutableStateOf("NEET") }
    var selectedYear by remember { mutableStateOf("2027") }
    var customExamNameInput by remember { mutableStateOf("") }

    // Target Deadline Mode: "DAYS" or "CALENDAR"
    var deadlineMode by remember { mutableStateOf("DAYS") }
    var targetDays by remember { mutableIntStateOf(90) }
    var customDaysInput by remember { mutableStateOf("63") }

    // Calendar Picker State
    var targetYear by remember { mutableIntStateOf(2027) }
    var targetMonth by remember { mutableIntStateOf(3) } // 0=Jan, 3=Apr
    var targetDay by remember { mutableIntStateOf(27) }

    val categories = DefaultSyllabusCatalog.categories

    val availableTemplates = remember(selectedCategory) {
        DefaultSyllabusCatalog.getTemplatesForCategory(selectedCategory)
    }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 28.dp, bottom = 48.dp)
    ) {
        item {
            Text(
                text = "Select Your Target Goal",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Choose your board or competitive exam to instantly load complete subjects & chapter syllabi.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
            )
        }

        // Category Pills (Horizontal Scroll)
        item {
            Text(
                text = "1. EXAM CATEGORY",
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
                                val templates = DefaultSyllabusCatalog.getTemplatesForCategory(category)
                                if (templates.isNotEmpty()) {
                                    selectedExamName = templates.first().examName
                                    selectedYear = templates.first().availableYears.firstOrNull() ?: "2027"
                                } else {
                                    selectedExamName = "Custom Exam"
                                    selectedYear = "2027"
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

        // Available Exams in the chosen category
        item {
            Text(
                text = "2. CHOOSE EXAM / CLASS (${availableTemplates.size} Available)",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = PrepBluePrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            if (selectedCategory == "Custom Goal") {
                OutlinedTextField(
                    value = customExamNameInput,
                    onValueChange = {
                        customExamNameInput = it
                        selectedExamName = if (it.isNotBlank()) it else "Custom Exam"
                    },
                    label = { Text("Enter Exam or Goal Name") },
                    placeholder = { Text("e.g. State PSC, MBA CET, Olympiad, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableTemplates.forEach { template ->
                        val isChosen = template.examName == selectedExamName
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedExamName = template.examName
                                    selectedYear = template.availableYears.firstOrNull() ?: "2027"
                                }
                                .testTag("exam_card_${template.examName.replace(" ", "_")}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isChosen) PrepBlueLight else MaterialTheme.colorScheme.surface
                            ),
                            border = if (isChosen) androidx.compose.foundation.BorderStroke(1.5.dp, PrepBluePrimary) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isChosen) PrepBluePrimary else Color(0xFFE2E8F0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = if (isChosen) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = template.examName,
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${template.subjects.size} Subjects • ${template.subjects.sumOf { it.chapters.size }} Chapters (${template.subjects.joinToString { it.name.take(10) }})",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isChosen) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = PrepBluePrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Session / Year Selection
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "3. TARGET SESSION / YEAR",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = PrepBluePrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val currentTemplate = availableTemplates.find { it.examName == selectedExamName }
            val years = currentTemplate?.availableYears ?: listOf("2026-27", "2027", "2028")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                years.forEach { yr ->
                    val isChosen = yr == selectedYear
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                        border = if (!isChosen) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)) else null,
                        modifier = Modifier.clickable { selectedYear = yr }
                    ) {
                        Text(
                            text = yr,
                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                            color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Completion Deadline Target: Days OR Calendar Date Option
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "4. COMPLETION DEADLINE TARGET",
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
                // Calendar Date Picker (Exact Date Selector e.g. 27 April 2027)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pick Target Date",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
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

                        // Month Row (12 Months in 2 rows)
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

                        // Day Row Quick Selectors
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

        // Action Button
        item {
            Spacer(modifier = Modifier.height(30.dp))

            val (finalTimestamp, finalDays) = targetTimestampAndDays

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
                        customTargetTimestamp = finalTimestamp
                    )
                    onComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_create_plan_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
            ) {
                Text(
                    text = "Load Syllabus & Start Tracking ($finalDays Days)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
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
