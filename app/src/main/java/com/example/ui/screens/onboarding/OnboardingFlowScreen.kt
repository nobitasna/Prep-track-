package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrepBackground
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepSurface
import com.example.ui.theme.PrepSurfaceVariant
import kotlinx.coroutines.delay

// ==========================================
// 1. SPLASH SCREEN (Figure 1 in Design Spec)
// ==========================================
@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2200)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF161626),
                        Color(0xFF1E1E2E)
                    )
                )
            )
            .clickable { onTimeout() }
    ) {
        // Starry Night Sky Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val starPositions = listOf(
                Offset(size.width * 0.15f, size.height * 0.12f),
                Offset(size.width * 0.35f, size.height * 0.08f),
                Offset(size.width * 0.65f, size.height * 0.15f),
                Offset(size.width * 0.82f, size.height * 0.10f),
                Offset(size.width * 0.22f, size.height * 0.25f),
                Offset(size.width * 0.78f, size.height * 0.28f),
                Offset(size.width * 0.50f, size.height * 0.20f),
                Offset(size.width * 0.90f, size.height * 0.22f)
            )
            starPositions.forEachIndexed { idx, pos ->
                drawCircle(
                    color = Color.White.copy(alpha = if (idx % 2 == 0) 0.8f else 0.4f),
                    radius = if (idx % 3 == 0) 2.5f else 1.5f,
                    center = pos
                )
            }
        }

        // Center Brand Identity
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stylized 'P' Upward Arrow Logo
            PrepTrackBrandLogo(size = 96.dp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PREP TRACK",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "PLAN  /  STUDY  /  ACHIEVE",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    fontSize = 12.sp
                ),
                color = PrepCyanSecondary
            )
        }

        // Mountain Artwork at Bottom
        MountainArtwork(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .align(Alignment.BottomCenter)
        )

        // Bottom Quote & Dots
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(PrepBluePrimary))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(PrepCyanSecondary))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.5f)))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Your dreams. Our tracking.",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = Color.White.copy(alpha = 0.75f)
            )
        }
    }
}

// ==========================================
// 2. ONBOARDING CAROUSEL (Screens 1, 2, 3) WITH HOVERING BOARD
// ==========================================
@Composable
fun OnboardingCarouselScreen(
    initialPage: Int = 1,
    onFinishOnboarding: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(initialPage.coerceIn(1, 3)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrepBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 12.dp)
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PrepTrackBrandLogo(size = 32.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PREP TRACK",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            fontSize = 13.sp
                        ),
                        color = Color.White
                    )
                }

                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text(
                        text = "Skip",
                        color = PrepCyanSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Main Illustration Area (Takes remaining top space)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                when (currentPage) {
                    1 -> OnboardingIllustrationOne()
                    2 -> OnboardingIllustrationTwo()
                    3 -> OnboardingIllustrationThree()
                }
            }

            // ==========================================
            // HOVERING BOARD (FLOATING BOTTOM CARD)
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("onboarding_hovering_board"),
                shape = RoundedCornerShape(30.dp),
                color = Color(0xF2121A2C), // Translucent dark navy
                shadowElevation = 16.dp,
                border = BorderStroke(
                    width = 1.2.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            PrepCyanSecondary.copy(alpha = 0.5f),
                            PrepBluePrimary.copy(alpha = 0.35f),
                            Color(0xFF202C45)
                        )
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 4-Dot Progress Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (dot in 1..4) {
                            val isActive = dot == currentPage
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(if (isActive) 22.dp else 7.dp, 7.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (isActive) {
                                            Brush.horizontalGradient(
                                                listOf(PrepCyanSecondary, PrepBluePrimary)
                                            )
                                        } else Color.White.copy(alpha = 0.2f)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Text Content (Title & Description)
                    when (currentPage) {
                        1 -> {
                            OnboardingContent(
                                title = "Track Your\nStudy Journey",
                                subtitle = "Plan your syllabus, track lectures, notes & practice — all in one unified cockpit."
                            )
                        }
                        2 -> {
                            OnboardingContent(
                                title = "Smart Planning\nMade Easy",
                                subtitle = "Fixed 2-hour lecture baseline calculation adapts daily to keep your exam target on track."
                            )
                        }
                        3 -> {
                            OnboardingContent(
                                title = "Your Goal,\nOur Priority",
                                subtitle = "Sign in securely with Google to sync your study streaks and progress across all devices."
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Primary Action Button (Next or Continue with Google)
                    if (currentPage < 3) {
                        Button(
                            onClick = { currentPage += 1 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("onboarding_primary_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                        ) {
                            Text(
                                text = "Next",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        // Page 3: Instant Sign in with Google / Get Started
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clickable { onNavigateToLogin() }
                                .testTag("onboarding_google_button"),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            shadowElevation = 3.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                GoogleMultiColorGIcon(modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Continue with Google",
                                    color = Color(0xFF1F2937),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Direct Login / Navigation Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentPage == 3) "Already have an account? " else "Already a member? ",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.5.sp
                        )
                        Text(
                            text = "Sign in with Google",
                            color = PrepCyanSecondary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable(onClick = onNavigateToLogin)
                                .testTag("onboarding_to_login_button")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingContent(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 30.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                lineHeight = 19.sp
            ),
            color = Color(0xFFB0B9C8),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

// ==========================================
// 3. LOGIN SCREEN - EXCLUSIVELY SIGN IN WITH GOOGLE
// ==========================================
@Composable
fun LoginScreen(
    onGoogleSignIn: () -> Unit,
    onLoginSuccess: () -> Unit = onGoogleSignIn,
    onNavigateToSignUp: () -> Unit = onGoogleSignIn,
    onBack: (() -> Unit)? = null
) {
    var showGoogleAccountDialog by remember { mutableStateOf(false) }

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
        // Decorative background starry cosmos
        Canvas(modifier = Modifier.fillMaxSize()) {
            val starPositions = listOf(
                Offset(size.width * 0.12f, size.height * 0.10f),
                Offset(size.width * 0.28f, size.height * 0.06f),
                Offset(size.width * 0.72f, size.height * 0.12f),
                Offset(size.width * 0.88f, size.height * 0.08f),
                Offset(size.width * 0.45f, size.height * 0.18f),
                Offset(size.width * 0.18f, size.height * 0.26f),
                Offset(size.width * 0.82f, size.height * 0.24f)
            )
            starPositions.forEachIndexed { idx, pos ->
                drawCircle(
                    color = Color.White.copy(alpha = if (idx % 2 == 0) 0.75f else 0.35f),
                    radius = if (idx % 3 == 0) 2.2f else 1.2f,
                    center = pos
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1E293B),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable(onClick = onBack)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.size(38.dp))
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrepCyanSecondary.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, PrepCyanSecondary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "GOOGLE SIGN-IN ONLY",
                        color = PrepCyanSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.size(38.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Center Brand Identity
            PrepTrackBrandLogo(size = 80.dp)

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "PREP TRACK",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    letterSpacing = 2.sp
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Plan • Focus • Track • Achieve",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    fontSize = 13.sp
                ),
                color = PrepCyanSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // HOVERING BOARD LOGIN CARD
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_hovering_board"),
                shape = RoundedCornerShape(28.dp),
                color = Color(0xF2121A2C),
                shadowElevation = 18.dp,
                border = BorderStroke(
                    width = 1.2.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            PrepCyanSecondary.copy(alpha = 0.45f),
                            PrepBluePrimary.copy(alpha = 0.35f),
                            Color(0xFF22304C)
                        )
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In with Google",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Single-tap Google sign-in to securely sync your syllabus, 2-hour daily workload, notes and streaks.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        color = Color(0xFFB0B9C8),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Benefit badges
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF172033))
                            .padding(14.dp)
                    ) {
                        LoginBenefitRow(
                            icon = "🎯",
                            title = "Smart Exam Syllabus Tracking",
                            desc = "NEET, JEE, CBSE & custom tailored roadmaps"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LoginBenefitRow(
                            icon = "⚡",
                            title = "Instant Cloud Sync",
                            desc = "Keep your progress safe & available anywhere"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LoginBenefitRow(
                            icon = "🔒",
                            title = "Google Identity Protection",
                            desc = "No passwords needed, secure and encrypted"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ==========================================
                    // SOLE LOGIN BUTTON: CONTINUE WITH GOOGLE
                    // ==========================================
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { showGoogleAccountDialog = true }
                            .testTag("login_google_sign_in_button"),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            GoogleMultiColorGIcon(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Sign in with Google",
                                color = Color(0xFF1F2937),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Security & Privacy Disclaimer
                    Text(
                        text = "By signing in, you agree to Prep Track's Terms of Service and Privacy Policy. Secured by Google Identity.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }

    // Interactive Google Account Chooser Dialog
    if (showGoogleAccountDialog) {
        AlertDialog(
            onDismissRequest = { showGoogleAccountDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GoogleMultiColorGIcon(modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign in with Google",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Choose an account to continue to PREP TRACK",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Account Card (nobitanobi7209@gmail.com / Shubh Anand)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PrepSurfaceVariant,
                        border = BorderStroke(1.dp, PrepBluePrimary.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showGoogleAccountDialog = false
                                onGoogleSignIn()
                            }
                            .testTag("google_account_shubh_anand")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(PrepBluePrimary, PrepBlueDark)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "S",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Shubh Anand",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "nobitanobi7209@gmail.com",
                                    fontSize = 12.sp,
                                    color = Color(0xFFB0B9C8)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary Account / Add Another Option
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PrepSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showGoogleAccountDialog = false
                                onGoogleSignIn()
                            }
                            .testTag("google_account_add_another")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF334155)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Use another Google Account",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.5.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showGoogleAccountDialog = false }) {
                    Text("Cancel", color = PrepCyanSecondary)
                }
            }
        )
    }
}

@Composable
private fun LoginBenefitRow(icon: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = desc,
                fontSize = 11.5.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

// ==========================================
// CUSTOM VECTOR ARTWORK FOR ONBOARDING
// ==========================================

// 1. Brand Logo: Modern 'P' with growth arrow
@Composable
fun PrepTrackBrandLogo(size: androidx.compose.ui.unit.Dp = 80.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF1E283A))
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(listOf(PrepCyanSecondary, PrepBluePrimary)),
                shape = RoundedCornerShape(size * 0.28f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.65f)) {
            val w = this.size.width
            val h = this.size.height

            // Upward growth stem (arrow)
            val stemPath = Path().apply {
                moveTo(w * 0.22f, h * 0.88f)
                lineTo(w * 0.22f, h * 0.35f)
                lineTo(w * 0.40f, h * 0.35f)
                lineTo(w * 0.40f, h * 0.88f)
                close()
            }
            drawPath(
                path = stemPath,
                brush = Brush.verticalGradient(
                    listOf(PrepCyanSecondary, PrepBluePrimary),
                    startY = 0f,
                    endY = h
                )
            )

            // Arrow head
            val arrowPath = Path().apply {
                moveTo(w * 0.12f, h * 0.38f)
                lineTo(w * 0.31f, h * 0.14f)
                lineTo(w * 0.50f, h * 0.38f)
                close()
            }
            drawPath(
                path = arrowPath,
                brush = Brush.verticalGradient(
                    listOf(Color.White, PrepCyanSecondary)
                )
            )

            // 'P' right curved loop
            val loopPath = Path().apply {
                moveTo(w * 0.38f, h * 0.18f)
                lineTo(w * 0.62f, h * 0.18f)
                cubicTo(
                    w * 0.85f, h * 0.18f,
                    w * 0.85f, h * 0.54f,
                    w * 0.62f, h * 0.54f
                )
                lineTo(w * 0.38f, h * 0.54f)
                close()
            }
            drawPath(
                path = loopPath,
                brush = Brush.linearGradient(
                    listOf(PrepCyanSecondary, PrepBluePrimary)
                ),
                style = Stroke(width = w * 0.14f)
            )
        }
    }
}

// 2. Onboarding 1 Artwork: Hiker facing mountain peaks & sunrise
@Composable
fun OnboardingIllustrationOne() {
    Box(
        modifier = Modifier
            .size(240.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(
                        Color(0xFF1E3A8A).copy(alpha = 0.6f),
                        Color(0xFF0F172A).copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val w = size.width
            val h = size.height

            // Rising Sun
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0xFFFDE047), Color(0xFFF59E0B), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.35f),
                    radius = w * 0.25f
                ),
                center = Offset(w * 0.5f, h * 0.35f),
                radius = w * 0.25f
            )

            // Mountain Peaks in Background
            val bgMountain = Path().apply {
                moveTo(0f, h * 0.8f)
                lineTo(w * 0.3f, h * 0.38f)
                lineTo(w * 0.55f, h * 0.65f)
                lineTo(w * 0.8f, h * 0.32f)
                lineTo(w, h * 0.75f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(bgMountain, Color(0xFF1E293B))

            // Snow on peaks
            val snowPeakLeft = Path().apply {
                moveTo(w * 0.24f, h * 0.47f)
                lineTo(w * 0.3f, h * 0.38f)
                lineTo(w * 0.36f, h * 0.47f)
                close()
            }
            drawPath(snowPeakLeft, Color(0xFFE2E8F0))

            val snowPeakRight = Path().apply {
                moveTo(w * 0.73f, h * 0.42f)
                lineTo(w * 0.8f, h * 0.32f)
                lineTo(w * 0.87f, h * 0.42f)
                close()
            }
            drawPath(snowPeakRight, Color(0xFFE2E8F0))

            // Foreground Ridge
            val fgRidge = Path().apply {
                moveTo(0f, h * 0.88f)
                lineTo(w * 0.5f, h * 0.7f)
                lineTo(w, h * 0.9f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(fgRidge, Color(0xFF0F172A))

            // Student/Hiker Silhouette standing on Ridge
            val hikerCenterX = w * 0.5f
            val hikerBaseY = h * 0.7f
            // Head
            drawCircle(Color(0xFF38BDF8), radius = 7f, center = Offset(hikerCenterX, hikerBaseY - 42f))
            // Body / Backpack
            drawRoundRect(
                color = Color(0xFF0A84FF),
                topLeft = Offset(hikerCenterX - 8f, hikerBaseY - 34f),
                size = Size(16f, 22f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
            // Backpack bump
            drawCircle(Color(0xFF0284C7), radius = 6f, center = Offset(hikerCenterX - 8f, hikerBaseY - 24f))
            // Legs
            drawLine(Color(0xFF0A84FF), Offset(hikerCenterX - 4f, hikerBaseY - 12f), Offset(hikerCenterX - 4f, hikerBaseY), strokeWidth = 3f)
            drawLine(Color(0xFF0A84FF), Offset(hikerCenterX + 4f, hikerBaseY - 12f), Offset(hikerCenterX + 4f, hikerBaseY), strokeWidth = 3f)
        }
    }
}

// 3. Onboarding 2 Artwork: Floating Phone with study widgets
@Composable
fun OnboardingIllustrationTwo() {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating Phone mockup in Center
        Card(
            modifier = Modifier
                .size(130.dp, 190.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = PrepSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, PrepBluePrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                // Top Notch
                Box(
                    modifier = Modifier
                        .size(36.dp, 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                )
                // Mini Graph / Progress
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(PrepBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = PrepCyanSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                // Mini lines
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(80.dp, 5.dp).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.4f)))
                    Box(modifier = Modifier.size(50.dp, 5.dp).clip(RoundedCornerShape(2.dp)).background(PrepCyanSecondary))
                }
            }
        }

        // Floating Badges around phone
        // Top Left: Calendar
        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PrepBlueLight),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrepCyanSecondary.copy(alpha = 0.6f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = PrepCyanSecondary, modifier = Modifier.size(22.dp))
            }
        }

        // Top Right: Video Play
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PrepBlueLight),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrepBluePrimary.copy(alpha = 0.6f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = PrepBluePrimary, modifier = Modifier.size(24.dp))
            }
        }

        // Bottom Right: Checkmark
        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF132F24)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(22.dp))
            }
        }
    }
}

// 4. Onboarding 3 Artwork: Glowing Target Bullseye
@Composable
fun OnboardingIllustrationThree() {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val maxR = size.width / 2

            // Glowing Outer Ring
            drawCircle(
                color = PrepBluePrimary.copy(alpha = 0.15f),
                radius = maxR,
                center = center
            )
            // Outer Ring
            drawCircle(
                color = PrepCyanSecondary,
                radius = maxR * 0.85f,
                center = center,
                style = Stroke(width = 8f)
            )
            // Middle Ring
            drawCircle(
                color = PrepBluePrimary,
                radius = maxR * 0.58f,
                center = center,
                style = Stroke(width = 9f)
            )
            // Inner Ring
            drawCircle(
                color = Color.White,
                radius = maxR * 0.32f,
                center = center,
                style = Stroke(width = 8f)
            )
            // Center Bullseye
            drawCircle(
                color = PrepCyanSecondary,
                radius = maxR * 0.16f,
                center = center
            )

            // Arrow striking Center
            val arrowLength = maxR * 0.7f
            val startArrow = Offset(center.x + arrowLength * 0.7f, center.y - arrowLength * 0.7f)
            drawLine(
                brush = Brush.linearGradient(listOf(Color.White, PrepCyanSecondary)),
                start = startArrow,
                end = center,
                strokeWidth = 6f
            )
            // Arrow Feathers
            drawLine(Color.White, startArrow, Offset(startArrow.x - 12f, startArrow.y - 12f), strokeWidth = 4f)
            drawLine(Color.White, startArrow, Offset(startArrow.x + 12f, startArrow.y + 12f), strokeWidth = 4f)
        }
    }
}

// 5. Mountain Artwork for Splash & Bottom
@Composable
fun MountainArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Back Layer Mountain
        val backMountain = Path().apply {
            moveTo(0f, h)
            lineTo(w * 0.25f, h * 0.45f)
            lineTo(w * 0.5f, h * 0.65f)
            lineTo(w * 0.75f, h * 0.40f)
            lineTo(w, h * 0.68f)
            lineTo(w, h)
            close()
        }
        drawPath(
            backMountain,
            Brush.verticalGradient(
                listOf(Color(0xFF1E293B), Color(0xFF0F172A))
            )
        )

        // Mid Layer Peak with Climber at Summit
        val summitX = w * 0.5f
        val summitY = h * 0.35f
        val midMountain = Path().apply {
            moveTo(w * 0.1f, h)
            lineTo(summitX, summitY)
            lineTo(w * 0.9f, h)
            close()
        }
        drawPath(
            midMountain,
            Brush.verticalGradient(
                listOf(Color(0xFF0284C7).copy(alpha = 0.5f), Color(0xFF0F172A))
            )
        )

        // Climber Silhouette on the Summit
        drawCircle(Color.White, radius = 5f, center = Offset(summitX, summitY - 18f))
        drawLine(Color.White, Offset(summitX, summitY - 18f), Offset(summitX, summitY), strokeWidth = 3f)
        // Arms holding a flag / celebratory gesture
        drawLine(Color(0xFF38BDF8), Offset(summitX, summitY - 12f), Offset(summitX + 12f, summitY - 24f), strokeWidth = 2.5f)
    }
}

// 6. Authentic Google "G" Icon
@Composable
fun GoogleMultiColorGIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2
        val cy = h / 2
        val radius = w * 0.46f
        val strokeWidth = w * 0.18f

        // Blue right & bar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(cx - radius, cy - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        // Horizontal bar
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(cx, cy),
            end = Offset(cx + radius, cy),
            strokeWidth = strokeWidth
        )
        // Green bottom
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(cx - radius, cy - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        // Yellow bottom-left
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(cx - radius, cy - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        // Red top-left
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(cx - radius, cy - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
    }
}
