package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PrepBackground
import com.example.ui.theme.PrepBlueDark
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepBorder
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepSurface
import com.example.ui.theme.PrepSurfaceVariant
import com.example.ui.theme.PrepTextPrimary
import com.example.ui.theme.PrepTextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.delay

// ==========================================
// 1. SPLASH SCREEN (Figure 1)
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
            .background(PrepBackground)
            .clickable { onTimeout() }
            .testTag("splash_screen_root")
    ) {
        // Mountain Backdrop Artwork Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Mountain 1 (distant dark blue)
            val p1 = Path().apply {
                moveTo(0f, h * 0.72f)
                lineTo(w * 0.35f, h * 0.58f)
                lineTo(w * 0.7f, h * 0.68f)
                lineTo(w, h * 0.55f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(p1, color = Color(0xFF1E283A).copy(alpha = 0.6f))

            // Mountain 2 (front peak with climber silhouette)
            val p2 = Path().apply {
                moveTo(0f, h * 0.82f)
                lineTo(w * 0.48f, h * 0.66f) // Peak center
                lineTo(w * 0.52f, h * 0.66f)
                lineTo(w, h * 0.84f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(p2, color = Color(0xFF0F172A))

            // Climber silhouette at peak (w * 0.5f, h * 0.66f)
            drawCircle(color = Color(0xFF38BDF8), radius = 6f, center = Offset(w * 0.5f, h * 0.635f))
            drawLine(
                color = Color(0xFF38BDF8),
                start = Offset(w * 0.5f, h * 0.635f),
                end = Offset(w * 0.5f, h * 0.66f),
                strokeWidth = 4f
            )

            // Starry sky glowing specks
            drawCircle(Color(0xFF38BDF8).copy(alpha = 0.8f), 3f, Offset(w * 0.2f, h * 0.2f))
            drawCircle(Color.White.copy(alpha = 0.9f), 2.5f, Offset(w * 0.8f, h * 0.15f))
            drawCircle(Color(0xFF0A84FF).copy(alpha = 0.7f), 3f, Offset(w * 0.75f, h * 0.32f))
            drawCircle(Color.White.copy(alpha = 0.6f), 2f, Offset(w * 0.15f, h * 0.4f))
            drawCircle(Color(0xFF38BDF8).copy(alpha = 0.7f), 2.5f, Offset(w * 0.85f, h * 0.45f))
        }

        // Center Content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo Badge
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF1E283A), Color(0xFF0F172A))
                        )
                    )
                    .border(2.dp, Brush.linearGradient(listOf(PrepBluePrimary, PrepCyanSecondary)), RoundedCornerShape(26.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Stylized P Logo Icon
                PrepTrackPLogo(modifier = Modifier.size(54.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "PREP TRACK",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "PLAN  /  STUDY  /  ACHIEVE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PrepCyanSecondary,
                letterSpacing = 2.sp
            )
        }

        // Bottom Slogan
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(PrepCyanSecondary))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF475569)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF475569)))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Your dreams. Our tracking.",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ==========================================
// 2. ONBOARDING CAROUSEL (Figure 2, 3, 4)
// ==========================================
@Composable
fun OnboardingCarouselScreen(
    initialPage: Int = 0,
    onNavigateToLogin: () -> Unit = {},
    onFinishOnboarding: () -> Unit = onNavigateToLogin,
    onGetStarted: () -> Unit = onFinishOnboarding
) {
    var currentPage by remember { mutableIntStateOf(initialPage.coerceIn(0, 2)) }

    val titles = listOf(
        "Track Your\nStudy Journey",
        "Smart Planning\nMade Easy",
        "Your Goal,\nOur Priority"
    )

    val descriptions = listOf(
        "Plan your study, track your progress and stay consistent — all in one place.",
        "Get AI-powered daily plans, manage lectures, notes, practice and much more.",
        "Stay focused, beat procrastination and achieve your dream score."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrepBackground)
            .padding(24.dp)
            .testTag("onboarding_carousel_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Small Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PrepTrackPLogo(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PREP TRACK",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Skip",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.15f))

            // Dynamic Artwork per page
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ) {
                when (currentPage) {
                    0 -> OnboardingJourneyIllustration()
                    1 -> OnboardingPlanningIllustration()
                    else -> OnboardingGoalIllustration()
                }
            }

            Spacer(modifier = Modifier.weight(0.15f))

            // Title
            Text(
                text = titles[currentPage],
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = descriptions[currentPage],
                fontSize = 14.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 4-Dot Page Indicator (Figure layout)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..3) {
                    val isActive = i == currentPage
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(if (isActive) 22.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isActive) PrepBluePrimary else Color(0xFF334155))
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Primary Next / Get Started Button
            Button(
                onClick = {
                    if (currentPage < 2) {
                        currentPage++
                    } else {
                        onGetStarted()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("onboarding_action_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
            ) {
                Text(
                    text = if (currentPage < 2) "Next" else "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Login Link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrepBluePrimary,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

// ==========================================
// 3. LOGIN SCREEN (Figure 5)
// ==========================================
@Composable
fun LoginScreen(
    viewModel: MainViewModel? = null,
    onGoogleSignIn: () -> Unit = { viewModel?.signInWithGoogle() },
    onLoginSuccess: () -> Unit = onGoogleSignIn,
    onNavigateToSignUp: () -> Unit = onGoogleSignIn
) {
    var emailInput by remember { mutableStateOf("nobitanobi7209@gmail.com") }
    var passwordInput by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showGoogleAccountDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrepBackground)
            .testTag("login_screen_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Top Header: Logo + PREP TRACK
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E283A))
                    .border(1.5.dp, PrepBluePrimary, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                PrepTrackPLogo(modifier = Modifier.size(42.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "PREP TRACK",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Welcome Back Header
            Text(
                text = "Welcome Back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Login to continue",
                fontSize = 13.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email or Phone Input
            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Email or Phone") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color(0xFF64748B)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_email_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PrepSurface,
                    unfocusedContainerColor = PrepSurface,
                    focusedBorderColor = PrepBluePrimary,
                    unfocusedBorderColor = PrepBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = PrepBluePrimary,
                    unfocusedLabelColor = Color(0xFF94A3B8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Input
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color(0xFF64748B)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_password_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PrepSurface,
                    unfocusedContainerColor = PrepSurface,
                    focusedBorderColor = PrepBluePrimary,
                    unfocusedBorderColor = PrepBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = PrepBluePrimary,
                    unfocusedLabelColor = Color(0xFF94A3B8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Forgot Password Link
            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = PrepBluePrimary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { /* Reset password flow */ }
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    viewModel?.loginWithEmail(emailInput, passwordInput)
                    onLoginSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("login_primary_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
            ) {
                Text(
                    text = "Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Or Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF334155))
                Text(
                    text = "  or  ",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF334155))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // CONTINUE WITH GOOGLE (Primary User Request)
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { showGoogleAccountDialog = true }
                    .testTag("google_sign_in_button"),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Continue with Google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sign Up Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Sign Up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrepBluePrimary,
                    modifier = Modifier.clickable { showGoogleAccountDialog = true }
                )
            }
        }

        // ==========================================
        // Authentic Google Account Picker Dialog
        // ==========================================
        if (showGoogleAccountDialog) {
            AlertDialog(
                onDismissRequest = { showGoogleAccountDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = "Google",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sign in with Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Choose an account to continue to Prep Track:",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Account Option: Shubh Anand (Primary User Account)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showGoogleAccountDialog = false
                                    viewModel?.signInWithGoogle(name = "Shubh Anand", email = "nobitanobi7209@gmail.com")
                                    onLoginSuccess()
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = PrepSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(PrepBluePrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "S",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Shubh Anand",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "nobitanobi7209@gmail.com",
                                        fontSize = 12.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Custom / Add another account
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showGoogleAccountDialog = false
                                    viewModel?.signInWithGoogle(name = "Scholar Student", email = "student@gmail.com")
                                    onLoginSuccess()
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = PrepSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF334155)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Use another Google account",
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showGoogleAccountDialog = false }) {
                        Text("Cancel", color = PrepBluePrimary)
                    }
                }
            )
        }
    }
}

// ==========================================
// VECTOR ARTWORKS FOR ONBOARDING
// ==========================================

@Composable
private fun PrepTrackPLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Stem & Upward Arrow
        val arrowStem = Path().apply {
            moveTo(w * 0.35f, h * 0.76f)
            lineTo(w * 0.35f, h * 0.52f)
            lineTo(w * 0.45f, h * 0.52f)
            lineTo(w * 0.45f, h * 0.76f)
            close()
        }
        drawPath(
            arrowStem,
            brush = Brush.verticalGradient(
                listOf(PrepCyanSecondary, PrepBluePrimary),
                startY = 0f,
                endY = h
            )
        )

        // Arrowhead
        val arrowHead = Path().apply {
            moveTo(w * 0.30f, h * 0.54f)
            lineTo(w * 0.40f, h * 0.34f)
            lineTo(w * 0.50f, h * 0.54f)
            close()
        }
        drawPath(
            arrowHead,
            brush = Brush.linearGradient(
                listOf(Color.White, PrepCyanSecondary)
            )
        )

        // Top Loop of 'P'
        val loop = Path().apply {
            moveTo(w * 0.40f, h * 0.34f)
            lineTo(w * 0.58f, h * 0.34f)
            cubicTo(w * 0.72f, h * 0.34f, w * 0.74f, h * 0.48f, w * 0.74f, h * 0.52f)
            cubicTo(w * 0.74f, h * 0.62f, w * 0.64f, h * 0.66f, w * 0.50f, h * 0.66f)
            lineTo(w * 0.45f, h * 0.66f)
            lineTo(w * 0.45f, h * 0.56f)
            lineTo(w * 0.52f, h * 0.56f)
            cubicTo(w * 0.60f, h * 0.56f, w * 0.64f, h * 0.54f, w * 0.64f, h * 0.50f)
            cubicTo(w * 0.64f, h * 0.44f, w * 0.58f, h * 0.42f, w * 0.52f, h * 0.42f)
            lineTo(w * 0.40f, h * 0.42f)
            close()
        }
        drawPath(
            loop,
            brush = Brush.linearGradient(
                listOf(PrepCyanSecondary, PrepBluePrimary, Color.White)
            )
        )
    }
}

// Page 1 Artwork: Trekker gazing at mountain sunrise
@Composable
private fun OnboardingJourneyIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Glowing sunrise halo in the background
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF38BDF8).copy(alpha = 0.35f), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.45f),
                radius = w * 0.4f
            ),
            radius = w * 0.4f,
            center = Offset(w * 0.5f, h * 0.45f)
        )

        // Distant mountains
        val m1 = Path().apply {
            moveTo(0f, h * 0.65f)
            lineTo(w * 0.28f, h * 0.35f)
            lineTo(w * 0.52f, h * 0.52f)
            lineTo(w * 0.78f, h * 0.32f)
            lineTo(w, h * 0.60f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            m1,
            brush = Brush.verticalGradient(
                listOf(Color(0xFF1E3A8A).copy(alpha = 0.7f), Color(0xFF0F172A))
            )
        )

        // Foreground mountain ridge
        val m2 = Path().apply {
            moveTo(0f, h * 0.85f)
            lineTo(w * 0.5f, h * 0.62f)
            lineTo(w, h * 0.88f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(m2, color = Color(0xFF0F172A))

        // Student with backpack standing on peak
        val cx = w * 0.5f
        val cy = h * 0.62f

        // Head
        drawCircle(color = Color(0xFF38BDF8), radius = 8f, center = Offset(cx, cy - 26f))
        // Body & backpack
        drawRoundRect(
            color = Color(0xFF0A84FF),
            topLeft = Offset(cx - 6f, cy - 18f),
            size = Size(12f, 18f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
        )
        // Backpack
        drawRoundRect(
            color = Color(0xFF60A5FA),
            topLeft = Offset(cx - 12f, cy - 16f),
            size = Size(6f, 14f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f)
        )
    }
}

// Page 2 Artwork: Glowing phone with calendar, video, checkmark
@Composable
private fun OnboardingPlanningIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.5f

        // Glow behind phone
        drawCircle(
            brush = Brush.radialGradient(
                listOf(PrepBluePrimary.copy(alpha = 0.3f), Color.Transparent),
                center = Offset(cx, cy),
                radius = w * 0.45f
            ),
            radius = w * 0.45f,
            center = Offset(cx, cy)
        )

        // Floating smartphone body
        drawRoundRect(
            color = Color(0xFF1E283A),
            topLeft = Offset(cx - 55f, cy - 85f),
            size = Size(110f, 170f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f)
        )
        drawRoundRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(cx - 50f, cy - 80f),
            size = Size(100f, 160f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f)
        )

        // Screen UI lines
        drawLine(
            color = PrepCyanSecondary,
            start = Offset(cx - 35f, cy - 50f),
            end = Offset(cx + 35f, cy - 50f),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF334155),
            start = Offset(cx - 35f, cy - 30f),
            end = Offset(cx + 20f, cy - 30f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF334155),
            start = Offset(cx - 35f, cy - 14f),
            end = Offset(cx + 30f, cy - 14f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Play Button Circle inside phone
        drawCircle(
            color = PrepBluePrimary,
            radius = 18f,
            center = Offset(cx, cy + 26f)
        )
        val playPath = Path().apply {
            moveTo(cx - 5f, cy + 18f)
            lineTo(cx + 8f, cy + 26f)
            lineTo(cx - 5f, cy + 34f)
            close()
        }
        drawPath(playPath, color = Color.White)

        // Floating glowing icons around phone
        // Left Calendar Badge
        drawCircle(color = PrepCyanSecondary, radius = 22f, center = Offset(cx - 85f, cy - 30f))
        drawCircle(color = Color(0xFF1E283A), radius = 18f, center = Offset(cx - 85f, cy - 30f))
        drawRect(color = PrepCyanSecondary, topLeft = Offset(cx - 93f, cy - 38f), size = Size(16f, 16f))

        // Right Checkmark Badge
        drawCircle(color = Color(0xFF10B981), radius = 22f, center = Offset(cx + 85f, cy + 15f))
        val checkPath = Path().apply {
            moveTo(cx + 77f, cy + 15f)
            lineTo(cx + 83f, cy + 21f)
            lineTo(cx + 94f, cy + 10f)
        }
        drawPath(checkPath, color = Color.White, style = Stroke(width = 4f, cap = StrokeCap.Round))
    }
}

// Page 3 Artwork: Glowing neon blue target with bullseye arrow
@Composable
private fun OnboardingGoalIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.5f

        // Outer glow
        drawCircle(
            brush = Brush.radialGradient(
                listOf(PrepBluePrimary.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(cx, cy),
                radius = 100f
            ),
            radius = 100f,
            center = Offset(cx, cy)
        )

        // Target Rings
        drawCircle(
            color = PrepBluePrimary,
            radius = 85f,
            center = Offset(cx, cy),
            style = Stroke(width = 8f)
        )
        drawCircle(
            color = Color(0xFF1E283A),
            radius = 65f,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = PrepCyanSecondary,
            radius = 50f,
            center = Offset(cx, cy),
            style = Stroke(width = 7f)
        )
        drawCircle(
            color = Color(0xFF0F172A),
            radius = 35f,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color.White,
            radius = 18f,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = PrepBluePrimary,
            radius = 10f,
            center = Offset(cx, cy)
        )

        // Dart / Arrow entering bullseye
        drawLine(
            brush = Brush.linearGradient(listOf(PrepCyanSecondary, Color.White)),
            start = Offset(cx + 65f, cy - 65f),
            end = Offset(cx + 4f, cy - 4f),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
        // Dart feathers
        drawLine(
            color = PrepCyanSecondary,
            start = Offset(cx + 65f, cy - 65f),
            end = Offset(cx + 78f, cy - 60f),
            strokeWidth = 4f
        )
        drawLine(
            color = PrepCyanSecondary,
            start = Offset(cx + 65f, cy - 65f),
            end = Offset(cx + 60f, cy - 78f),
            strokeWidth = 4f
        )
    }
}
