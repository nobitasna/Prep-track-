package com.example.ui.screens.subscription

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.security.ActivationKeySecurity
import com.example.ui.screens.onboarding.PrepTrackBrandLogo
import com.example.ui.theme.PrepBackground
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepSurface
import com.example.ui.theme.PrepSurfaceVariant

@Composable
fun SubscriptionSelectionScreen(
    userEmail: String,
    userName: String,
    onStartTrial: () -> Unit,
    onActivateKey: (String) -> Pair<Boolean, String>,
    onSuccessActivated: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    var enteredKey by remember { mutableStateOf("") }
    var keyErrorMessage by remember { mutableStateOf<String?>(null) }

    val cleanEmail = ActivationKeySecurity.normalizeEmail(userEmail)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0B111E),
                        Color(0xFF101728),
                        Color(0xFF151D30)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Top Row with Back / Switch account
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.testTag("subscription_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrepCyanSecondary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, PrepCyanSecondary.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = "MEMBERSHIP ACCESS",
                        color = PrepCyanSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Header
            PrepTrackBrandLogo(size = 72.dp)

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Welcome to PREP TRACK",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Choose your plan to start exam preparation",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                color = Color(0xFFB0B9C8),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Logged in User ID Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1E283D),
                border = BorderStroke(1.dp, Color(0xFF2E3D5C))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Account ID: ",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = cleanEmail,
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Account ID",
                        tint = PrepCyanSecondary,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Account ID", cleanEmail))
                                Toast.makeText(context, "Account ID copied!", Toast.LENGTH_SHORT).show()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // OPTION 1: 3-DAY FREE TRIAL
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("option_3_day_trial_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF2121B2D)),
                border = BorderStroke(1.2.dp, Color(0xFF283955))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E293B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = null,
                                    tint = PrepCyanSecondary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "3-Day Free Trial",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "72 Hours Full Access",
                                    color = PrepCyanSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1E293B)
                        ) {
                            Text(
                                text = "FREE",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Try all features of Prep Track completely free for three days before upgrading.",
                        color = Color(0xFFB0B9C8),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SubscriptionFeatureItem("Full syllabus tracking & completion analytics")
                    SubscriptionFeatureItem("2-hour daily reference study planner")
                    SubscriptionFeatureItem("Pomodoro focus timer & distraction shield")
                    SubscriptionFeatureItem("Active for 3 days (after 3 days, upgrade required)")

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onStartTrial,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("button_start_3_day_trial"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E293B)
                        ),
                        border = BorderStroke(1.dp, PrepCyanSecondary.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Start 3-Day Free Trial",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // OPTION 2: UPGRADE TO PRO (WITH KEY)
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("option_upgrade_pro_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF2161A33)),
                border = BorderStroke(
                    1.5.dp,
                    Brush.horizontalGradient(
                        listOf(PrepBluePrimary, Color(0xFF8B5CF6), PrepCyanSecondary)
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(PrepBluePrimary, Color(0xFF8B5CF6))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Upgrade to Pro",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Monthly & Lifetime Access",
                                    color = Color(0xFFA78BFA),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PrepBluePrimary.copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, PrepBluePrimary.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "PRO PASS",
                                color = PrepCyanSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Unlock uninterrupted Pro access with your activation key. Supports both 1-Month Pass and Lifetime License keys.",
                        color = Color(0xFFC7D2FE),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SubscriptionFeatureItem("Supports 1-Month (30-day) or Lifetime Pro keys", isPro = true)
                    SubscriptionFeatureItem("Unlocks all exam targets (NEET, JEE, CBSE & Custom)", isPro = true)
                    SubscriptionFeatureItem("Single-account ID binding protection", isPro = true)
                    SubscriptionFeatureItem("Instant activation with special license key", isPro = true)

                    Spacer(modifier = Modifier.height(18.dp))

                    // Activation Key Input Field
                    OutlinedTextField(
                        value = enteredKey,
                        onValueChange = {
                            enteredKey = it.uppercase()
                            keyErrorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_activation_key"),
                        label = { Text("Activation Key (PREP-XXXX-XXXX-XXXX)") },
                        placeholder = { Text("e.g. PREP-4A9B-8C2D-1E0F") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = PrepCyanSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = PrepCyanSecondary,
                            unfocusedBorderColor = Color(0xFF3B4866),
                            focusedLabelColor = PrepCyanSecondary,
                            unfocusedLabelColor = Color(0xFF94A3B8),
                            cursorColor = PrepCyanSecondary,
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (enteredKey.isNotBlank()) {
                                    val (success, message) = onActivateKey(enteredKey)
                                    if (success) {
                                        onSuccessActivated()
                                    } else {
                                        keyErrorMessage = message
                                    }
                                }
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (keyErrorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = keyErrorMessage ?: "",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (enteredKey.isBlank()) {
                                keyErrorMessage = "Please enter your Activation Key"
                            } else {
                                val (success, message) = onActivateKey(enteredKey)
                                if (success) {
                                    Toast.makeText(context, "🎉 Pro License Activated Successfully!", Toast.LENGTH_LONG).show()
                                    onSuccessActivated()
                                } else {
                                    keyErrorMessage = message
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("button_activate_pro_key"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrepBluePrimary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Activate Pro License",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Don't have an activation key yet? Share your Account ID with the admin to get your unique license key.",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.5.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SubscriptionFeatureItem(text: String, isPro: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isPro) Color(0xFFA78BFA) else PrepCyanSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = if (isPro) Color(0xFFE2E8F0) else Color(0xFFCBD5E1),
            fontSize = 12.5.sp
        )
    }
}
