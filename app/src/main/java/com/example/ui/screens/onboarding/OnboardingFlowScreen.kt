package com.example.ui.screens.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import com.example.ui.components.GlobalBackButton
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
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
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2200)
        if (!hasNavigated) {
            hasNavigated = true
            onTimeout()
        }
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
            .clickable {
                if (!hasNavigated) {
                    hasNavigated = true
                    onTimeout()
                }
            }
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
// 2. ONBOARDING CAROUSEL & STUDY COCKPIT LAUNCHPAD
// ==========================================
@Composable
fun OnboardingCarouselScreen(
    initialPage: Int = 1,
    onFinishOnboarding: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onGoogleSignIn: (() -> Unit)? = null,
    onGetStarted: (() -> Unit)? = null
) {
    var viewMode by remember { mutableStateOf("COCKPIT") } // "COCKPIT" (Default Study Hub), "STREAMS" (Stream Presets), "TOUR" (Guided Tour)
    var currentPage by remember { mutableIntStateOf(initialPage.coerceIn(1, 4)) }

    // System Back Button navigation
    BackHandler(enabled = viewMode != "COCKPIT" || currentPage > 1) {
        if (viewMode == "TOUR") {
            if (currentPage > 1) {
                currentPage -= 1
            } else {
                viewMode = "COCKPIT"
            }
        } else if (viewMode == "STREAMS") {
            viewMode = "COCKPIT"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF090E17),
                        Color(0xFF0F172A),
                        Color(0xFF131D31)
                    )
                )
            )
    ) {
        // Decorative background starry cosmos
        Canvas(modifier = Modifier.fillMaxSize()) {
            val starPositions = listOf(
                Offset(size.width * 0.12f, size.height * 0.08f),
                Offset(size.width * 0.35f, size.height * 0.12f),
                Offset(size.width * 0.68f, size.height * 0.07f),
                Offset(size.width * 0.88f, size.height * 0.14f),
                Offset(size.width * 0.18f, size.height * 0.28f),
                Offset(size.width * 0.82f, size.height * 0.32f),
                Offset(size.width * 0.50f, size.height * 0.22f)
            )
            starPositions.forEachIndexed { idx, pos ->
                drawCircle(
                    color = Color.White.copy(alpha = if (idx % 2 == 0) 0.6f else 0.25f),
                    radius = if (idx % 3 == 0) 2.0f else 1.2f,
                    center = pos
                )
            }
        }

        if (viewMode != "TOUR") {
            // ==========================================
            // PREMIER STUDY COCKPIT & LAUNCHPAD INTERFACE
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // TOP BRAND HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PrepTrackBrandLogo(size = 40.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "PREP TRACK",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.2.sp,
                                        fontSize = 17.sp
                                    ),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(PrepCyanSecondary)
                                )
                            }
                            Text(
                                text = "PLAN • FOCUS • TRACK • ACHIEVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    fontSize = 9.sp
                                ),
                                color = PrepCyanSecondary
                            )
                        }
                    }

                    Surface(
                        onClick = onNavigateToLogin,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Text(
                            text = "Sign In",
                            color = PrepCyanSecondary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // INTERACTIVE MODE SWITCHER (Segmented control)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF131D31),
                    border = BorderStroke(1.dp, Color(0xFF1E293B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            onClick = { viewMode = "COCKPIT" },
                            shape = RoundedCornerShape(12.dp),
                            color = if (viewMode == "COCKPIT") PrepBluePrimary else Color.Transparent,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "🚀 Study Hub",
                                color = if (viewMode == "COCKPIT") Color.White else Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Surface(
                            onClick = { viewMode = "STREAMS" },
                            shape = RoundedCornerShape(12.dp),
                            color = if (viewMode == "STREAMS") PrepBluePrimary else Color.Transparent,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "📚 Streams",
                                color = if (viewMode == "STREAMS") Color.White else Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Surface(
                            onClick = { viewMode = "TOUR" },
                            shape = RoundedCornerShape(12.dp),
                            color = if (viewMode == "TOUR") PrepBluePrimary else Color.Transparent,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "📖 4-Step Tour",
                                color = if (viewMode == "TOUR") Color.White else Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (viewMode == "STREAMS") {
                    // ==========================================
                    // STREAM PRESETS INTERFACE (Direct One-Tap Starters)
                    // ==========================================
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "CHOOSE YOUR ACADEMIC STREAM",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = PrepCyanSecondary
                        )

                        listOf(
                            Triple("🔬 Science Stream", "PCM • PCB • PCMB • CS • IP", listOf("Physics", "Chemistry", "Maths / Bio", "English")),
                            Triple("💼 Commerce Stream", "Accounts • Business Studies • Economics • Maths", listOf("Accountancy", "Business Studies", "Economics", "English")),
                            Triple("🎨 Humanities / Arts", "History • Pol Science • Geography • Psychology", listOf("History", "Pol Science", "Geography", "English")),
                            Triple("🩺 Competitive Exams", "NEET • JEE Main • CUET • NDA • UPSC", listOf("Exam-specific curated chapters & PYQ")),
                            Triple("⚙️ Custom Combination", "Tailor your school's unique subject combination", listOf("+ Add Custom Subjects freely"))
                        ).forEach { (title, subtitle, subjects) ->
                            Surface(
                                onClick = { onGetStarted?.invoke() ?: onFinishOnboarding() },
                                shape = RoundedCornerShape(18.dp),
                                color = Color(0xFF131D31),
                                border = BorderStroke(1.dp, Color(0xFF22304C)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = subtitle, color = Color(0xFF94A3B8), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                                        Row(
                                            modifier = Modifier.padding(top = 6.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            subjects.take(3).forEach { sub ->
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = Color(0xFF1E293B)
                                                ) {
                                                    Text(
                                                        text = sub,
                                                        color = PrepCyanSecondary,
                                                        fontSize = 9.5.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = PrepCyanSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // HERO STUDY COCKPIT CARD (Rich, visual, unclipped)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_cockpit_card"),
                        shape = RoundedCornerShape(28.dp),
                        color = Color(0xF20F172A),
                        border = BorderStroke(
                            width = 1.2.dp,
                            brush = Brush.verticalGradient(
                                listOf(
                                    PrepCyanSecondary.copy(alpha = 0.5f),
                                    PrepBluePrimary.copy(alpha = 0.35f),
                                    Color(0xFF1E293B)
                                )
                            )
                        ),
                        shadowElevation = 14.dp
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            // Card Header: Dynamic Cockpit
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎯", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "DYNAMIC STUDY COCKPIT",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "Boards • NEET • JEE • Commerce • Arts",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 10.5.sp
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "2026–27",
                                        color = Color(0xFF10B981),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Stream Pills row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("🔬 Science", "💼 Commerce", "🎨 Humanities", "🩺 NEET").forEach { pill ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF1E293B),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = pill,
                                            color = Color(0xFFCBD5E1),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Bar Preview
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Syllabus Mastery",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "68% Done",
                                    color = PrepCyanSecondary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF1E293B))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.68f)
                                        .height(7.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(PrepBluePrimary, PrepCyanSecondary)
                                            )
                                        )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // 3 FEATURE VALUE PILLARS
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Pillar 1: Fixed 2-Hour Benchmark
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = PrepBluePrimary.copy(alpha = 0.2f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Speed,
                                            contentDescription = null,
                                            tint = PrepCyanSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Fixed 2-Hour Reference Workload",
                                        color = Color.White,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Objective progress baseline unaffected by 1.5x / 2x speed",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Pillar 2: Separate Focus & Pomodoro
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = null,
                                            tint = Color(0xFFA78BFA),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Independent Pomodoro Tracking",
                                        color = Color.White,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Separate pure self-study focus from video watch time",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Pillar 3: Adaptive Planning & Buffer Days
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFFF6D00).copy(alpha = 0.2f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = Color(0xFFFF9E80),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Adaptive Catch-up Engine",
                                        color = Color.White,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Auto-recalibrates daily targets with dynamic buffer days",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // PRIMARY ACTION BUTTON: GET STARTED
                Button(
                    onClick = { onGetStarted?.invoke() ?: onFinishOnboarding() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_primary_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // SECONDARY ACTION: GOOGLE SIGN IN
                if (onGoogleSignIn != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        onClick = onGoogleSignIn,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("onboarding_google_button")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("G", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4285F4))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Continue with Google",
                                color = Color(0xFF1F2937),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // FOOTER: SIGN IN LINK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already a member? ",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Sign in",
                        color = PrepCyanSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(onClick = onNavigateToLogin)
                            .testTag("onboarding_to_login_button")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        } else {
            // ==========================================
            // 4-STEP GUIDED TOUR (IMPROVED & UNCLIPPED)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 12.dp)
            ) {
                // TOP BAR NAVIGATION
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlobalBackButton(
                        onClick = {
                            if (currentPage > 1) {
                                currentPage -= 1
                            } else {
                                viewMode = "COCKPIT"
                            }
                        },
                        modifier = Modifier.testTag("onboarding_back_button")
                    )

                    // Top Right: Skip Button
                    TextButton(
                        onClick = { viewMode = "COCKPIT" },
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = "Overview",
                            color = PrepCyanSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // MAIN ILLUSTRATION AREA (Smooth Transition, unclipped)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = currentPage,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { width -> width / 3 } + fadeIn()) togetherWith
                                        (slideOutHorizontally { width -> -width / 3 } + fadeOut())
                            } else {
                                (slideInHorizontally { width -> -width / 3 } + fadeIn()) togetherWith
                                        (slideOutHorizontally { width -> width / 3 } + fadeOut())
                            }
                        },
                        label = "OnboardingIllustrationAnim"
                    ) { page ->
                        when (page) {
                            1 -> OnboardingIllustrationOne()
                            2 -> OnboardingIllustrationTwo()
                            3 -> OnboardingIllustrationThree()
                            4 -> OnboardingIllustrationFour()
                        }
                    }
                }

                // LARGE ROUNDED DARK CARD
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("onboarding_card"),
                    shape = RoundedCornerShape(32.dp),
                    color = Color(0xF20F172A),
                    shadowElevation = 16.dp,
                    border = BorderStroke(
                        width = 1.2.dp,
                        brush = Brush.verticalGradient(
                            listOf(
                                PrepCyanSecondary.copy(alpha = 0.45f),
                                PrepBluePrimary.copy(alpha = 0.3f),
                                Color(0xFF1E293B)
                            )
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Modern Page Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (dot in 1..4) {
                                val isActive = dot == currentPage
                                val targetWidth = if (isActive) 28.dp else 8.dp
                                val width by animateDpAsState(
                                    targetValue = targetWidth,
                                    animationSpec = tween(durationMillis = 300),
                                    label = "dotWidth"
                                )
                                val color by animateColorAsState(
                                    targetValue = if (isActive) PrepBluePrimary else Color(0xFF334155),
                                    animationSpec = tween(durationMillis = 300),
                                    label = "dotColor"
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(width = width, height = 8.dp)
                                        .clip(if (isActive) RoundedCornerShape(4.dp) else CircleShape)
                                        .background(color)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Text Content
                        AnimatedContent(
                            targetState = currentPage,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInHorizontally { width -> width / 4 } + fadeIn()) togetherWith
                                            (slideOutHorizontally { width -> -width / 4 } + fadeOut())
                                } else {
                                    (slideInHorizontally { width -> -width / 4 } + fadeIn()) togetherWith
                                            (slideOutHorizontally { width -> width / 4 } + fadeOut())
                                }
                            },
                            label = "OnboardingTextAnim"
                        ) { page ->
                            when (page) {
                                1 -> OnboardingContent(
                                    title = "Track Your Study Journey",
                                    subtitle = "Plan your syllabus, track lectures, notes & practice — all in one unified cockpit."
                                )
                                2 -> OnboardingContent(
                                    title = "Plan Smarter",
                                    subtitle = "Turn your syllabus into a clear, manageable study plan."
                                )
                                3 -> OnboardingContent(
                                    title = "Stay Focused",
                                    subtitle = "Use Focus Mode and Pomodoro sessions to make every study session count."
                                )
                                4 -> OnboardingContent(
                                    title = "Track Your Progress",
                                    subtitle = "See your lectures, practice, revision and study time come together in one place."
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Primary Action Button
                        Button(
                            onClick = {
                                if (currentPage < 4) {
                                    currentPage += 1
                                } else {
                                    onGetStarted?.invoke() ?: onFinishOnboarding()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("onboarding_primary_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                        ) {
                            Text(
                                text = if (currentPage < 4) "Next" else "Get Started",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Already a member? ",
                                color = Color(0xFF94A3B8),
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Sign in",
                                color = PrepCyanSecondary,
                                fontSize = 13.sp,
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
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onDismissError: () -> Unit = {},
    onOpenSettings: (() -> Unit)? = null,
    onLoginWithEmail: ((String) -> Unit)? = null,
    onLoginSuccess: () -> Unit = onGoogleSignIn,
    onNavigateToSignUp: () -> Unit = onGoogleSignIn,
    onBack: (() -> Unit)? = null
) {
    var showCustomEmailDialog by remember { mutableStateOf(false) }
    var customEmailInput by remember { mutableStateOf("") }

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
                    GlobalBackButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("login_back_button")
                    )
                } else {
                    Spacer(modifier = Modifier.size(44.dp))
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

                    // Error Display (if authentication failed)
                    AnimatedVisibility(
                        visible = !errorMessage.isNullOrBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp)
                                .testTag("login_error_banner"),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x33EF4444),
                            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.6f))
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "⚠️ ${errorMessage ?: ""}",
                                        color = Color(0xFFFCA5A5),
                                        fontSize = 12.5.sp,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "✕",
                                        color = Color(0xFFFCA5A5),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable { onDismissError() }
                                            .padding(4.dp)
                                    )
                                }
                                if (onOpenSettings != null && (errorMessage?.contains("Settings", ignoreCase = true) == true || errorMessage?.contains("Google account", ignoreCase = true) == true)) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onOpenSettings() },
                                        color = Color(0xFFEF4444).copy(alpha = 0.25f),
                                        border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = "⚙️ Open Android Settings to Add Account",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ==========================================
                    // SOLE LOGIN BUTTON: CONTINUE WITH GOOGLE
                    // ==========================================
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(enabled = !isLoading) { onGoogleSignIn() }
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
                            if (isLoading) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = Color(0xFF1F2937),
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Signing in with Google...",
                                    color = Color(0xFF1F2937),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            } else {
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

    if (showCustomEmailDialog) {
        AlertDialog(
            onDismissRequest = { showCustomEmailDialog = false },
            title = {
                Text(
                    text = "Sign in with Email",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your Google account email to sign in or test another account ID:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customEmailInput,
                        onValueChange = { customEmailInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. user@gmail.com") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val email = customEmailInput.trim()
                        if (email.isNotBlank()) {
                            showCustomEmailDialog = false
                            if (onLoginWithEmail != null) {
                                onLoginWithEmail(email)
                            } else {
                                onGoogleSignIn()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary)
                ) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomEmailDialog = false }) {
                    Text("Cancel")
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

// 2. Onboarding 1 Artwork: Student, Study Goal, Planning, Progress, Achievement
@Composable
fun OnboardingIllustrationOne() {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        // Central Roadmap & Progress Card (No circle clipping, perfectly aligned)
        Card(
            modifier = Modifier.size(200.dp, 195.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121B2D)),
            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(PrepCyanSecondary, PrepBluePrimary)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header with Target Goal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎯", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "STUDY JOURNEY",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "2027",
                            color = Color(0xFF10B981),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Interactive 4-Phase Roadmap Track
                Canvas(modifier = Modifier.fillMaxWidth().height(65.dp)) {
                    val w = size.width
                    val h = size.height

                    // Track line
                    val path = Path().apply {
                        moveTo(w * 0.10f, h * 0.75f)
                        cubicTo(
                            w * 0.35f, h * 0.75f,
                            w * 0.50f, h * 0.30f,
                            w * 0.90f, h * 0.20f
                        )
                    }
                    drawPath(
                        path = path,
                        brush = Brush.linearGradient(listOf(PrepBluePrimary, PrepCyanSecondary, Color.White)),
                        style = Stroke(width = 6f, cap = StrokeCap.Round)
                    )

                    // Node 1: Foundation (Done)
                    drawCircle(Color(0xFF10B981), radius = 8f, center = Offset(w * 0.15f, h * 0.73f))
                    drawCircle(Color.White, radius = 3.5f, center = Offset(w * 0.15f, h * 0.73f))

                    // Node 2: Lectures (In progress)
                    drawCircle(Color(0xFF38BDF8), radius = 9f, center = Offset(w * 0.45f, h * 0.48f))
                    drawCircle(Color.White, radius = 4f, center = Offset(w * 0.45f, h * 0.48f))

                    // Node 3: Mocks & PYQ
                    drawCircle(Color(0xFF818CF8), radius = 8f, center = Offset(w * 0.70f, h * 0.28f))
                    drawCircle(Color.White, radius = 3.5f, center = Offset(w * 0.70f, h * 0.28f))

                    // Apex Goal Flag
                    val flagX = w * 0.90f
                    val flagY = h * 0.20f
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color(0xFFFDE047), Color(0xFFF59E0B), Color.Transparent),
                            center = Offset(flagX, flagY),
                            radius = 20f
                        ),
                        center = Offset(flagX, flagY),
                        radius = 20f
                    )
                    drawLine(Color.White, Offset(flagX, flagY), Offset(flagX, flagY - 18f), strokeWidth = 2.5f)
                    val flag = Path().apply {
                        moveTo(flagX, flagY - 18f)
                        lineTo(flagX + 16f, flagY - 12f)
                        lineTo(flagX, flagY - 6f)
                        close()
                    }
                    drawPath(flag, Color(0xFFF59E0B))
                }

                HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)

                // Bottom Status Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("2.0h / day pace", color = Color(0xFFE2E8F0), fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔥", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("100% On Track", color = Color(0xFF10B981), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Floating Badges - Positioned carefully with safe bounds
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 2.dp, end = 2.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF0F172A),
            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚡", fontSize = 10.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text("Syllabus Ready", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 3. Onboarding 2 Artwork: Syllabus, Calendar, Checklist, Study Planning
@Composable
fun OnboardingIllustrationTwo() {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        // Central Planner & Checklist Card
        Card(
            modifier = Modifier.size(190.dp, 190.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121B2D)),
            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(PrepCyanSecondary, PrepBluePrimary)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Calendar Timetable Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = PrepCyanSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SMART PLANNER",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PrepBluePrimary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "2h/day",
                            color = PrepCyanSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Days of week row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("M", "T", "W", "T", "F", "S").forEachIndexed { i, day ->
                        val isDone = i < 4
                        Surface(
                            shape = CircleShape,
                            color = if (isDone) PrepBluePrimary else Color(0xFF1E293B),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = day,
                                    fontSize = 9.sp,
                                    color = if (isDone) Color.White else Color(0xFF94A3B8),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)

                // Syllabus Task Checklist items
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Task 1
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Physics • Kinematics", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    // Task 2
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chemistry • Atomic Structure", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    // Task 3
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            border = BorderStroke(1.5.dp, PrepCyanSecondary),
                            color = Color.Transparent,
                            modifier = Modifier.size(14.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(7.dp))
                        Text("Maths • Calculus Practice", color = Color(0xFFCBD5E1), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Floating Tag
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 4.dp, end = 2.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF0F172A),
            border = BorderStroke(1.dp, PrepCyanSecondary.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📋", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Syllabus Linked", color = Color.White, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 4. Onboarding 3 Artwork: Timer, Focus Session, Progress Ring, Distraction Control
@Composable
fun OnboardingIllustrationThree() {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(210.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val r = size.width / 2

            // Concentric ambient glow
            drawCircle(
                color = PrepBluePrimary.copy(alpha = 0.12f),
                radius = r * 0.95f,
                center = center
            )

            // Outer Track Ring
            drawCircle(
                color = Color(0xFF1E293B),
                radius = r * 0.78f,
                center = center,
                style = Stroke(width = 12f)
            )

            // Animated Cyan-Blue Focus Progress Arc (~75% completed)
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(PrepBluePrimary, PrepCyanSecondary, Color.White, PrepCyanSecondary)
                ),
                startAngle = -90f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )

            // Inner Ring
            drawCircle(
                color = Color(0xFF0F172A),
                radius = r * 0.65f,
                center = center
            )
        }

        // Digital Focus Display in Center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PrepBluePrimary.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, PrepCyanSecondary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⏱", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("POMODORO", color = PrepCyanSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "25:00",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Text(
                text = "Deep Focus Active",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )
        }

        // Floating Distraction Shield Badge at Bottom Right
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xF2121A2C),
            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("Zero Distraction", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 5. Onboarding 4 Artwork: Progress Graph, Completed Tasks, Study Streak, Statistics
@Composable
fun OnboardingIllustrationFour() {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        // Analytics Card
        Card(
            modifier = Modifier.size(200.dp, 190.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121B2D)),
            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(PrepCyanSecondary, PrepBluePrimary)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header with Streak
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GROWTH ANALYTICS",
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFF6D00).copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color(0xFFFF6D00).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔥", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("7d Streak", color = Color(0xFFFF9E40), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Bar Chart with Ascending Growth
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val barHeights = listOf(0.35f, 0.45f, 0.60f, 0.55f, 0.78f, 0.90f, 1.0f)
                    val days = listOf("M", "T", "W", "T", "F", "S", "S")

                    barHeights.forEachIndexed { idx, frac ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 16.dp, height = (60 * frac).dp)
                                    .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            if (idx >= 5) listOf(Color.White, PrepCyanSecondary)
                                            else listOf(PrepCyanSecondary, PrepBluePrimary)
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = days[idx],
                                fontSize = 9.sp,
                                color = if (idx >= 5) PrepCyanSecondary else Color(0xFF94A3B8),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Completion status banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Syllabus Mastery", color = Color(0xFF94A3B8), fontSize = 10.5.sp)
                        Text("84% Done", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Floating Target Pin
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp),
            shape = CircleShape,
            color = PrepBluePrimary,
            border = BorderStroke(1.5.dp, Color.White)
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Text("📈", fontSize = 12.sp)
            }
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
