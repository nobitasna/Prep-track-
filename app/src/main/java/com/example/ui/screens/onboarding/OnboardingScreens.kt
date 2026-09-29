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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.viewmodel.MainViewModel

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
    var targetDays by remember { mutableStateOf(90) }

    val categories = DefaultSyllabusCatalog.categories

    val availableTemplates = remember(selectedCategory) {
        DefaultSyllabusCatalog.getTemplatesForCategory(selectedCategory)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 36.dp, bottom = 48.dp)
    ) {
        item {
            Text(
                text = "What are you preparing for?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Select your goal to automatically load a pre-built default syllabus.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )
        }

        item {
            Text(
                text = "SELECT CATEGORY",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = PrepBluePrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        items(categories) { category ->
            val isSelected = category == selectedCategory
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
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
                    .testTag("category_card_${category.replace(" ", "_")}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) PrepBlueLight else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, PrepBluePrimary) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSelected) PrepBluePrimary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = PrepBluePrimary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "EXAM / CLASS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
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
                    label = { Text("Enter Exam / Goal Name") },
                    placeholder = { Text("e.g. CFA Level 1, SAT 2027, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTemplates.forEach { template ->
                        val isChosen = template.examName == selectedExamName
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                            border = if (!isChosen) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedExamName = template.examName
                                    selectedYear = template.availableYears.firstOrNull() ?: "2027"
                                }
                        ) {
                            Text(
                                text = template.examName,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "TARGET YEAR / SESSION",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = PrepBluePrimary,
                modifier = Modifier.padding(bottom = 10.dp)
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

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "COMPLETION TARGET",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = PrepBluePrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(60, 90, 120, 180).forEach { days ->
                    val isChosen = targetDays == days
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isChosen) PrepBluePrimary else MaterialTheme.colorScheme.surface,
                        border = if (!isChosen) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)) else null,
                        modifier = Modifier.clickable { targetDays = days }
                    ) {
                        Text(
                            text = "$days Days",
                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                            color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    val finalName = if (selectedCategory == "Custom Goal" && customExamNameInput.isNotBlank()) {
                        customExamNameInput
                    } else {
                        selectedExamName
                    }
                    viewModel.completeOnboarding(selectedCategory, finalName, selectedYear, targetDays)
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
                    text = "Load Syllabus & Create Plan",
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
