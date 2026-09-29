package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrepBlueLight
import com.example.ui.theme.PrepBluePrimary
import com.example.viewmodel.AppScreen

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: AppScreen,
    val testTag: String
) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, AppScreen.Dashboard, "nav_item_home")
    object Plan : BottomNavItem("Plan", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, AppScreen.Plan, "nav_item_plan")
    object Syllabus : BottomNavItem("Syllabus", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, AppScreen.Syllabus, "nav_item_syllabus")
    object Focus : BottomNavItem("Focus", Icons.Filled.Timer, Icons.Outlined.Timer, AppScreen.Focus, "nav_item_focus")
    object Analytics : BottomNavItem("Analytics", Icons.Filled.BarChart, Icons.Outlined.BarChart, AppScreen.Analytics, "nav_item_analytics")
}

@Composable
fun PrepTrackBottomBar(
    currentScreen: AppScreen,
    onTabSelected: (AppScreen) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Plan,
        BottomNavItem.Syllabus,
        BottomNavItem.Focus,
        BottomNavItem.Analytics
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = androidx.compose.ui.unit.Dp(6f)
    ) {
        items.forEach { item ->
            val isSelected = when (item) {
                is BottomNavItem.Home -> currentScreen is AppScreen.Dashboard
                is BottomNavItem.Plan -> currentScreen is AppScreen.Plan
                is BottomNavItem.Syllabus -> currentScreen is AppScreen.Syllabus || currentScreen is AppScreen.ChapterDetail
                is BottomNavItem.Focus -> currentScreen is AppScreen.Focus
                is BottomNavItem.Analytics -> currentScreen is AppScreen.Analytics
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrepBluePrimary,
                    selectedTextColor = PrepBluePrimary,
                    indicatorColor = PrepBlueLight,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
