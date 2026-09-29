package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrepCyanSecondary
import com.example.viewmodel.AppScreen

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: AppScreen,
    val testTag: String
) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, AppScreen.Dashboard, "nav_item_home")
    object Syllabus : BottomNavItem("Syllabus", Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook, AppScreen.Syllabus, "nav_item_syllabus")
    object Plan : BottomNavItem("Plan", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, AppScreen.Plan, "nav_item_plan")
    object Analytics : BottomNavItem("Analytics", Icons.Filled.BarChart, Icons.Outlined.BarChart, AppScreen.Analytics, "nav_item_analytics")
    object More : BottomNavItem("More", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz, AppScreen.Settings, "nav_item_more")
}

/**
 * Floating Hovering Board (Bottom Navigation Bar)
 * Hovers smoothly above the bottom edge with rounded capsule borders and glowing active indicators.
 */
@Composable
fun PrepTrackBottomBar(
    currentScreen: AppScreen,
    onTabSelected: (AppScreen) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Syllabus,
        BottomNavItem.Plan,
        BottomNavItem.Analytics,
        BottomNavItem.More
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xF212172A),
            border = BorderStroke(1.2.dp, Color(0x3338BDF8)),
            shadowElevation = 14.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .testTag("hovering_bottom_board")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = when (item) {
                        is BottomNavItem.Home -> currentScreen is AppScreen.Dashboard
                        is BottomNavItem.Syllabus -> currentScreen is AppScreen.Syllabus || currentScreen is AppScreen.ChapterDetail
                        is BottomNavItem.Plan -> currentScreen is AppScreen.Plan
                        is BottomNavItem.Analytics -> currentScreen is AppScreen.Analytics || currentScreen is AppScreen.Focus
                        is BottomNavItem.More -> currentScreen is AppScreen.Settings
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onTabSelected(item.screen) }
                            .padding(vertical = 4.dp)
                            .testTag(item.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = if (isSelected) 46.dp else 36.dp, height = 28.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) Color(0x2E0066FF) else Color.Transparent
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) PrepCyanSecondary else Color(0xFF94A3B8),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PrepCyanSecondary else Color(0xFF94A3B8),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
