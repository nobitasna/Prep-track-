package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.PrepTrackBottomBar
import com.example.ui.components.PrepTrackTopBar
import com.example.ui.screens.analytics.AnalyticsScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.focus.FocusScreen
import com.example.ui.screens.onboarding.OnboardingSelectGoalScreen
import com.example.ui.screens.onboarding.OnboardingWelcomeScreen
import com.example.ui.screens.plan.PlanScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.syllabus.ChapterDetailScreen
import com.example.ui.screens.syllabus.SyllabusScreen
import com.example.ui.theme.PrepTrackTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrepTrackTheme {
                PrepTrackApp()
            }
        }
    }
}

@Composable
fun PrepTrackApp(
    viewModel: MainViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeGoal by viewModel.activeGoal.collectAsStateWithLifecycle()
    val userStreak by viewModel.userStreak.collectAsStateWithLifecycle()

    // Back button handling for all sub-screens
    BackHandler(enabled = currentScreen !is AppScreen.Dashboard && currentScreen !is AppScreen.OnboardingWelcome) {
        when (currentScreen) {
            is AppScreen.ChapterDetail -> viewModel.navigateTo(AppScreen.Syllabus)
            is AppScreen.Settings -> viewModel.navigateBack()
            is AppScreen.OnboardingSelectGoal -> viewModel.navigateTo(AppScreen.OnboardingWelcome)
            else -> viewModel.navigateTo(AppScreen.Dashboard)
        }
    }

    val showBars = currentScreen !is AppScreen.OnboardingWelcome &&
            currentScreen !is AppScreen.OnboardingSelectGoal &&
            currentScreen !is AppScreen.ChapterDetail &&
            currentScreen !is AppScreen.Settings

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBars) {
                val title = when (currentScreen) {
                    is AppScreen.Dashboard -> "PREP TRACK"
                    is AppScreen.Plan -> "Study Planner"
                    is AppScreen.Syllabus -> "Syllabus Manager"
                    is AppScreen.Focus -> "Focus Timer"
                    is AppScreen.Analytics -> "Analytics & Stats"
                    else -> "PREP TRACK"
                }
                val subtitle = when (currentScreen) {
                    is AppScreen.Dashboard -> activeGoal?.let { "${it.examName} (${it.targetYear})" } ?: "Plan • Focus • Track • Achieve"
                    is AppScreen.Plan -> "Fixed 2h Reference Workload"
                    is AppScreen.Syllabus -> activeGoal?.examName ?: "Customizable Syllabus"
                    is AppScreen.Focus -> "Independent Pomodoro Tracking"
                    is AppScreen.Analytics -> "Continuous Learning Progress"
                    else -> null
                }
                PrepTrackTopBar(
                    title = title,
                    subtitle = subtitle,
                    showBackButton = false,
                    streakCount = userStreak?.currentStreak ?: 1,
                    onSettingsClick = { viewModel.navigateTo(AppScreen.Settings) }
                )
            }
        },
        bottomBar = {
            if (showBars) {
                PrepTrackBottomBar(
                    currentScreen = currentScreen,
                    onTabSelected = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        when (val screen = currentScreen) {
            is AppScreen.OnboardingWelcome -> {
                OnboardingWelcomeScreen(
                    onGetStarted = { viewModel.navigateTo(AppScreen.OnboardingSelectGoal) }
                )
            }
            is AppScreen.OnboardingSelectGoal -> {
                OnboardingSelectGoalScreen(
                    viewModel = viewModel,
                    onComplete = { viewModel.navigateTo(AppScreen.Dashboard) }
                )
            }
            is AppScreen.Dashboard -> {
                DashboardScreen(viewModel = viewModel, modifier = contentModifier)
            }
            is AppScreen.Plan -> {
                PlanScreen(viewModel = viewModel, modifier = contentModifier)
            }
            is AppScreen.Syllabus -> {
                SyllabusScreen(viewModel = viewModel, modifier = contentModifier)
            }
            is AppScreen.ChapterDetail -> {
                ChapterDetailScreen(
                    chapterId = screen.chapterId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.Syllabus) }
                )
            }
            is AppScreen.Focus -> {
                FocusScreen(viewModel = viewModel, modifier = contentModifier)
            }
            is AppScreen.Analytics -> {
                AnalyticsScreen(viewModel = viewModel, modifier = contentModifier)
            }
            is AppScreen.Settings -> {
                SettingsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
            }
        }
    }
}
