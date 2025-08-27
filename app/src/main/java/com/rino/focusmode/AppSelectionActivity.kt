package com.rino.focusmode

import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.rino.focusmode.ui.theme.FocusModeTheme
import kotlin.OptIn

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable
)

class AppSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusModeTheme {
                AppSelectionScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager

    val appsList = remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    val selectedApps = remember { mutableStateOf(AppBlockerManager.getBlockedApps(context)) }

    LaunchedEffect(Unit) {
        val installedApps = packageManager.getInstalledApplications(0)
            // Filter to keep user-installed apps AND updated system apps (like Chrome, YouTube, etc.)
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM == 0) || (it.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) }
            // Filter to remove our own app from the list
            .filter { it.packageName != context.packageName }
            .map { appInfo ->
                AppInfo(
                    name = packageManager.getApplicationLabel(appInfo).toString(),
                    packageName = appInfo.packageName,
                    icon = packageManager.getApplicationIcon(appInfo)
                )
            }.sortedBy { it.name.lowercase() }

        appsList.value = installedApps
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Apps to Block") })
        },
        bottomBar = {
            Button(
                onClick = {
                    AppBlockerManager.saveBlockedApps(context, selectedApps.value)
                    (context as? ComponentActivity)?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Selections")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            items(appsList.value) { app ->
                AppListItem(
                    appInfo = app,
                    isSelected = selectedApps.value.contains(app.packageName),
                    onCheckedChange = { packageName, isChecked ->
                        val currentSelection = selectedApps.value.toMutableSet()
                        if (isChecked) {
                            currentSelection.add(packageName)
                        } else {
                            currentSelection.remove(packageName)
                        }
                        selectedApps.value = currentSelection
                    }
                )
            }
        }
    }
}

@Composable
fun AppListItem(
    appInfo: AppInfo,
    isSelected: Boolean,
    onCheckedChange: (String, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(appInfo.packageName, !isSelected) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = appInfo.icon),
            contentDescription = "${appInfo.name} icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(appInfo.name, modifier = Modifier.weight(1f))
        Checkbox(
            checked = isSelected,
            onCheckedChange = { isChecked -> onCheckedChange(appInfo.packageName, isChecked) }
        )
    }
}