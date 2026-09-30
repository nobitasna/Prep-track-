package com.example.ui.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.security.ActivationKeySecurity
import com.example.ui.screens.subscription.ActivationKeyDialog
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activeGoal by viewModel.activeGoal.collectAsStateWithLifecycle()
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val subscriptionInfo by viewModel.subscriptionInfo.collectAsStateWithLifecycle()

    var showUpgradeDialog by remember { mutableStateOf(false) }

    var studyReminderEnabled by remember { mutableStateOf(true) }
    var streakAlertsEnabled by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }

    var showCustomNotesDialog by remember { mutableStateOf(false) }
    var customNotesInput by remember { mutableStateOf("") }

    val currentSpeed = studyPlan?.playbackSpeed ?: 1.0f
    val currentNotesMin = studyPlan?.notesMinutesPerLecture ?: 30
    val currentPracticeMin = studyPlan?.practiceMinutesDaily ?: 60
    val currentRevisionMin = studyPlan?.revisionMinutesDaily ?: 30
    val currentDays = studyPlan?.targetDays ?: 90
    val currentPattern = studyPlan?.studyPattern ?: "WEEKLY_TIMETABLE"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Settings & Preferences",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
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
            // Google Account Profile & Sign Out
            item {
                Text(
                    text = "GOOGLE ACCOUNT",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("settings_google_account_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(PrepBluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.name.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = userProfile.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = PrepCyanSecondary.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "Google",
                                            color = PrepCyanSecondary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = userProfile.email,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.signOut()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("settings_switch_account_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Switch Account", fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.signOut()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("settings_sign_out_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2))
                            ) {
                                Text("Sign Out", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // ==========================================
            // MEMBERSHIP & LICENSE STATUS (TRIAL / PRO)
            // ==========================================
            item {
                Text(
                    text = "MEMBERSHIP & LICENSE",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("settings_membership_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.2.dp,
                        if (subscriptionInfo.isPro) Color(0xFFFBBF24).copy(alpha = 0.5f)
                        else if (subscriptionInfo.isTrialActive) PrepCyanSecondary.copy(alpha = 0.4f)
                        else Color(0xFFEF4444).copy(alpha = 0.5f)
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (subscriptionInfo.isPro) Color(0xFFFBBF24).copy(alpha = 0.2f)
                                            else if (subscriptionInfo.isTrialActive) PrepCyanSecondary.copy(alpha = 0.15f)
                                            else Color(0xFFEF4444).copy(alpha = 0.18f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (subscriptionInfo.isPro) Icons.Default.Star
                                        else if (subscriptionInfo.isTrialActive) Icons.Default.Timer
                                        else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (subscriptionInfo.isPro) Color(0xFFFBBF24)
                                        else if (subscriptionInfo.isTrialActive) PrepCyanSecondary
                                        else Color(0xFFEF4444),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (subscriptionInfo.isLifetimePro) "Prep Track Pro (Lifetime)"
                                        else if (subscriptionInfo.isMonthlyPro && !subscriptionInfo.isMonthlyExpired) "Prep Track Pro (Monthly)"
                                        else if (subscriptionInfo.isMonthlyExpired) "Monthly Pro Expired"
                                        else if (subscriptionInfo.isTrialActive) "3-Day Free Trial"
                                        else "Trial Expired",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = if (subscriptionInfo.isLifetimePro) "Active • Permanent Lifetime License"
                                        else if (subscriptionInfo.isMonthlyPro && !subscriptionInfo.isMonthlyExpired) "Active • ${subscriptionInfo.monthlyRemainingDays} days left"
                                        else if (subscriptionInfo.isMonthlyExpired) "1-Month pass ended • Renewal required"
                                        else if (subscriptionInfo.isTrialActive) "${subscriptionInfo.trialRemainingFormatted} remaining"
                                        else "Upgrade required to unlock features",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (subscriptionInfo.isProActive) Color(0xFFFBBF24)
                                        else if (subscriptionInfo.isTrialActive) PrepCyanSecondary
                                        else Color(0xFFEF4444)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (subscriptionInfo.isProActive) Color(0xFFFBBF24).copy(alpha = 0.18f)
                                else if (subscriptionInfo.isTrialActive) PrepCyanSecondary.copy(alpha = 0.15f)
                                else Color(0xFFEF4444).copy(alpha = 0.18f)
                            ) {
                                Text(
                                    text = if (subscriptionInfo.isLifetimePro) "LIFETIME"
                                    else if (subscriptionInfo.isMonthlyPro && !subscriptionInfo.isMonthlyExpired) "MONTHLY"
                                    else if (subscriptionInfo.isTrialActive) "TRIAL"
                                    else "EXPIRED",
                                    color = if (subscriptionInfo.isProActive) Color(0xFFFBBF24)
                                    else if (subscriptionInfo.isTrialActive) PrepCyanSecondary
                                    else Color(0xFFEF4444),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Account ID info
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Licensed ID: ",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.5.sp
                                )
                                Text(
                                    text = subscriptionInfo.userAccountId,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy ID",
                                    tint = PrepCyanSecondary,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            clipboard.setPrimaryClip(ClipData.newPlainText("Account ID", subscriptionInfo.userAccountId))
                                            Toast.makeText(context, "Account ID copied!", Toast.LENGTH_SHORT).show()
                                        }
                                )
                            }
                        }

                        if (subscriptionInfo.isProActive) {
                            Spacer(modifier = Modifier.height(10.dp))
                            val passTypeText = if (subscriptionInfo.isMonthlyPro) "30-Day Monthly Pass" else "Lifetime License"
                            Text(
                                text = "$passTypeText: ${ActivationKeySecurity.maskKey(subscriptionInfo.activatedKey)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.5.sp
                            )
                        } else {
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { showUpgradeDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("settings_upgrade_pro_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (subscriptionInfo.isMonthlyExpired) "Renew Pro with Key" else "Upgrade to Pro with Key", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Active Goal & Exam Section
            item {
                Text(
                    text = "CURRENT TARGET",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

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
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = PrepBluePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeGoal?.examName ?: "No Exam Selected",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Category: ${activeGoal?.category ?: "General"} • Session: ${activeGoal?.targetYear ?: "Target"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.OnboardingSelectGoal) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("settings_switch_exam_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrepBlueLight)
                        ) {
                            Text(
                                text = "Switch Exam or Goal",
                                color = PrepBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Planning Preferences - DIRECTLY EDITABLE
            item {
                Text(
                    text = "PLANNING PREFERENCES (DIRECT EDIT)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // 1. Playback Speed Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Playback Speed", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Adjusts watch time; 2h reference is fixed", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrepBlueLight
                            ) {
                                Text(
                                    text = "${currentSpeed}× active",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrepBluePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Interactive speed chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(1.0f, 1.25f, 1.5f, 1.75f, 2.0f).forEach { spd ->
                                val isSel = currentSpeed == spd
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updatePlaybackSpeed(spd) }
                                        .testTag("settings_speed_${spd}")
                                ) {
                                    Text(
                                        text = "${spd}×",
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else PrepBlueDark,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                        // 2. Notes Time Per Lecture Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Notes Time / Lecture", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Additional notes workload per lecture", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrepBlueLight
                            ) {
                                Text(
                                    text = if (currentNotesMin == 0) "None" else "${currentNotesMin}m active",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrepBluePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Interactive notes chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(0 to "None", 15 to "15m", 30 to "30m", 45 to "45m", 60 to "60m").forEach { (min, label) ->
                                val isSel = currentNotesMin == min
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updateNotesTime(min) }
                                        .testTag("settings_notes_${min}")
                                ) {
                                    Text(
                                        text = label,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else PrepBlueDark,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = {
                                customNotesInput = currentNotesMin.toString()
                                showCustomNotesDialog = true
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("+ Custom Minutes", fontSize = 12.sp, color = PrepBluePrimary)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                        // 3. Daily Practice / DPP Workload
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Daily Practice / DPP", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Problem solving workload allocated daily", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrepBlueLight
                            ) {
                                Text(
                                    text = if (currentPracticeMin == 0) "None" else "${currentPracticeMin}m active",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrepBluePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(0 to "None", 30 to "30m", 60 to "1h", 90 to "1.5h", 120 to "2h").forEach { (min, label) ->
                                val isSel = currentPracticeMin == min
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updatePracticeTime(min) }
                                        .testTag("settings_practice_${min}")
                                ) {
                                    Text(
                                        text = label,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else PrepBlueDark,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                        // 4. Target Days Deadline
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Finish Deadline (Days)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Target timeframe for complete syllabus", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrepBlueLight
                            ) {
                                Text(
                                    text = "$currentDays days",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrepBluePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(30, 60, 90, 120, 180).forEach { days ->
                                val isSel = currentDays == days
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSel) PrepBluePrimary else PrepBlueLight,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updateTargetDays(days) }
                                        .testTag("settings_days_${days}")
                                ) {
                                    Text(
                                        text = "${days}d",
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else PrepBlueDark,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                        // 5. Study Pattern Mode
                        Text("Study Pattern Mode", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Weekly timetable vs sequential completion", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val isWeekly = currentPattern == "WEEKLY_TIMETABLE"
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isWeekly) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateStudyPattern("WEEKLY_TIMETABLE") }
                                    .testTag("settings_pattern_weekly")
                            ) {
                                Text(
                                    text = "Weekly Timetable",
                                    fontWeight = if (isWeekly) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isWeekly) Color.White else PrepBlueDark,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }

                            val isOneByOne = currentPattern == "ONE_BY_ONE"
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isOneByOne) PrepBluePrimary else PrepBlueLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateStudyPattern("ONE_BY_ONE") }
                                    .testTag("settings_pattern_one_by_one")
                            ) {
                                Text(
                                    text = "One By One",
                                    fontWeight = if (isOneByOne) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isOneByOne) Color.White else PrepBlueDark,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.Plan) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrepBlueLight)
                        ) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = PrepBlueDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Open Full Plan Details & Timetable",
                                color = PrepBlueDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Notification Reminders
            item {
                Text(
                    text = "STUDY NOTIFICATIONS",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

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
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Daily Study Plan Alert", fontWeight = FontWeight.SemiBold)
                                Text("Morning notification with required daily workload", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = studyReminderEnabled,
                                onCheckedChange = { studyReminderEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = PrepBluePrimary)
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Streak Protection Reminder", fontWeight = FontWeight.SemiBold)
                                Text("Reminder in the evening if no study logged today", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = streakAlertsEnabled,
                                onCheckedChange = { streakAlertsEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = PrepBluePrimary)
                            )
                        }
                    }
                }
            }

            // Reset & Data Management
            item {
                Text(
                    text = "DATA MANAGEMENT",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = { showResetDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("settings_reset_plan_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2))
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFDC2626))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Reset Plan & Re-load Syllabus",
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // About PREP TRACK
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrepBlueLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "PREP TRACK",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = PrepBluePrimary
                        )
                        Text(
                            text = "Plan • Focus • Track • Achieve",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = PrepBlueDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Version 1.0.0 • Offline-First Architecture\nUniversal study planner for School, Medical, Engineering, University & Competitive exams.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Custom Notes Minutes Dialog
    if (showCustomNotesDialog) {
        AlertDialog(
            onDismissRequest = { showCustomNotesDialog = false },
            title = { Text("Custom Notes Time") },
            text = {
                Column {
                    Text(
                        text = "Enter notes preparation time in minutes per lecture:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customNotesInput,
                        onValueChange = { customNotesInput = it.filter { char -> char.isDigit() } },
                        label = { Text("Minutes per lecture") },
                        placeholder = { Text("e.g. 20, 40, 50") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val min = customNotesInput.toIntOrNull() ?: 30
                        viewModel.updateNotesTime(min)
                        showCustomNotesDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomNotesDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Study Plan?") },
            text = {
                Text("This will re-load the default syllabus for your current exam and reset completion progress. User customizations will be refreshed.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetEntirePlanAndSyllabus()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Confirm Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showUpgradeDialog) {
        ActivationKeyDialog(
            userEmail = userProfile.email,
            onDismiss = { showUpgradeDialog = false },
            onActivateKey = { key -> viewModel.activateProWithKey(key) }
        )
    }
}
