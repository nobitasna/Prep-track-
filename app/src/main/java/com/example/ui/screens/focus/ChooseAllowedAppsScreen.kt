package com.example.ui.screens.focus

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GlobalBackButton
import com.example.ui.theme.PrepBluePrimary
import com.example.ui.theme.PrepCyanSecondary
import com.example.ui.theme.PrepSuccess
import com.example.viewmodel.MainViewModel

/**
 * PREP TRACK — Focus Mode: Choose Allowed Apps Screen (Phase 4).
 *
 * Dynamically queries launchable applications from the user's Android device.
 * Stores and persists selected apps by package name using the Room database.
 */
@Composable
fun ChooseAllowedAppsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val installedApps by viewModel.installedDeviceApps.collectAsStateWithLifecycle()
    val isAppsLoading by viewModel.isInstalledAppsLoading.collectAsStateWithLifecycle()
    val allowedAppsFromDb by viewModel.enabledAllowedApps.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "ALLOWED", "RESTRICTED"
    var saveSuccessMessage by remember { mutableStateOf(false) }

    // Dynamically load apps from THIS device on first compose
    LaunchedEffect(Unit) {
        viewModel.loadDeviceApps(context)
    }

    // Set of package names that are currently selected in the DB
    val allowedPackageNames = remember(allowedAppsFromDb) {
        allowedAppsFromDb.map { it.packageName }.toSet()
    }

    val filteredApps = remember(installedApps, allowedPackageNames, searchQuery, selectedFilter) {
        installedApps.filter { app ->
            val matchesSearch = searchQuery.isBlank() ||
                    app.appName.contains(searchQuery, ignoreCase = true) ||
                    app.packageName.contains(searchQuery, ignoreCase = true)

            val isAllowed = allowedPackageNames.contains(app.packageName)
            val matchesFilter = when (selectedFilter) {
                "ALLOWED" -> isAllowed
                "RESTRICTED" -> !isAllowed
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070D18)) // Dark navy/black PREP TRACK background
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlobalBackButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("choose_allowed_apps_back_button")
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "FOCUS MODE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = PrepCyanSecondary
                    )
                    Text(
                        text = "Choose Allowed Apps",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Select the apps you want to keep available while Focus Mode is active.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search installed apps...",
                        color = Color(0xFF64748B),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF64748B)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF0F172A),
                    unfocusedContainerColor = Color(0xFF0F172A),
                    focusedBorderColor = PrepCyanSecondary,
                    unfocusedBorderColor = Color(0xFF1E2D48),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_allowed_apps_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Tabs & Stats Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val filters = listOf(
                        "ALL" to "All (${installedApps.size})",
                        "ALLOWED" to "Allowed (${allowedPackageNames.size})",
                        "RESTRICTED" to "Restricted (${(installedApps.size - allowedPackageNames.size).coerceAtLeast(0)})"
                    )

                    filters.forEach { (key, label) ->
                        val isSelected = selectedFilter == key
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) PrepBluePrimary else Color(0xFF131D31),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) PrepCyanSecondary else Color(0xFF1E2D48)
                            ),
                            modifier = Modifier.clickable { selectedFilter = key }
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                if (allowedPackageNames.isNotEmpty()) {
                    Text(
                        text = "Clear All",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444),
                        modifier = Modifier
                            .clickable {
                                viewModel.clearAllAllowedApps()
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Feedback Banner
        if (saveSuccessMessage) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0x2210B981),
                border = BorderStroke(1.dp, Color(0x6610B981)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrepSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Allowed apps configuration saved to local database!",
                        fontSize = 12.sp,
                        color = Color(0xFF34D399),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // App List Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                isAppsLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PrepCyanSecondary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Scanning installed applications...",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    }
                }

                installedApps.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF131D31),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Launchable Apps Found",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Could not discover eligible launcher applications on this device.",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                filteredApps.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matching apps found",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredApps, key = { it.packageName }) { app ->
                            val isAllowed = allowedPackageNames.contains(app.packageName)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.toggleAllowedAppSelection(
                                            packageName = app.packageName,
                                            appName = app.appName,
                                            isAllowed = !isAllowed
                                        )
                                        saveSuccessMessage = false
                                    }
                                    .testTag("app_item_${app.packageName.replace('.', '_')}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isAllowed) Color(0xFF131D31) else Color(0xFF0F172A)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isAllowed) Color(0x6638BDF8) else Color(0xFF1E2D48)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // App Icon
                                    AppIconDisplay(
                                        drawable = app.icon,
                                        appName = app.appName,
                                        modifier = Modifier.size(42.dp)
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // App Name and Package Name
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = app.appName,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            maxLines = 1
                                        )
                                        Text(
                                            text = app.packageName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isAllowed) PrepCyanSecondary else Color(0xFF64748B),
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }

                                    // Selection Toggle (Checkbox)
                                    Checkbox(
                                        checked = isAllowed,
                                        onCheckedChange = { checked ->
                                            viewModel.toggleAllowedAppSelection(
                                                packageName = app.packageName,
                                                appName = app.appName,
                                                isAllowed = checked
                                            )
                                            saveSuccessMessage = false
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = PrepBluePrimary,
                                            uncheckedColor = Color(0xFF475569),
                                            checkmarkColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("app_checkbox_${app.packageName.replace('.', '_')}")
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar: Save Configuration
        Surface(
            color = Color(0xFF0A1220),
            border = BorderStroke(1.dp, Color(0xFF1E2D48)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${allowedPackageNames.size} Allowed Apps Selected",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Persisted to Room Database",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = {
                        viewModel.saveAllowedAppsConfiguration()
                        saveSuccessMessage = true
                        onBack()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrepBluePrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    modifier = Modifier.testTag("save_allowed_apps_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Save Selection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/**
 * Renders the device application icon smoothly.
 */
@Composable
private fun AppIconDisplay(
    drawable: Drawable?,
    appName: String,
    modifier: Modifier = Modifier
) {
    val imageBitmap = remember(drawable) {
        drawable?.let {
            try {
                it.toBitmap(width = 96, height = 96).asImageBitmap()
            } catch (_: Exception) {
                null
            }
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = appName,
            modifier = modifier.clip(RoundedCornerShape(10.dp))
        )
    } else {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF1E293B),
            border = BorderStroke(1.dp, Color(0xFF334155)),
            modifier = modifier
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = appName.take(1).uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38BDF8)
                )
            }
        }
    }
}
