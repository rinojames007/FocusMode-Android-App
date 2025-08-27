package com.rino.focusmode

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rino.focusmode.ui.theme.FocusModeTheme
import androidx.compose.runtime.mutableStateOf

class MainActivity : ComponentActivity() {

    companion object {
        var isFocusModeActive = mutableStateOf(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // We now wrap our entire app in the theme we just created.
            // This automatically handles light/dark mode!
            FocusModeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FocusModeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusModeScreen() {
    val context = LocalContext.current
    var statusText by remember { mutableStateOf("Focus Mode is OFF") }
    var isDisabling by remember { mutableStateOf(false) }
    var countdownText by remember { mutableStateOf("") }
    var countDownTimer by remember { mutableStateOf<CountDownTimer?>(null) }

    // This now correctly observes the state change and updates the UI
    LaunchedEffect(MainActivity.isFocusModeActive.value) {
        statusText = if (MainActivity.isFocusModeActive.value) "Focus Mode is ON" else "Focus Mode is OFF"
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Focus Mode") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = statusText,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isDisabling) {
                Text(
                    text = countdownText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        countDownTimer?.cancel()
                        isDisabling = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Cancel")
                }
            } else {
                val mainButtonText = if (MainActivity.isFocusModeActive.value) "Stop Focus Mode" else "Start Focus Mode"

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (!hasPermission(context)) { return@Button }

                        if (MainActivity.isFocusModeActive.value) {
                            isDisabling = true
                            countDownTimer = object : CountDownTimer(15000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    countdownText = "Disabling in ${millisUntilFinished / 1000}..."
                                }
                                override fun onFinish() {
                                    MainActivity.isFocusModeActive.value = false
                                    isDisabling = false
                                }
                            }.start()
                        } else {
                            MainActivity.isFocusModeActive.value = true
                        }
                    }
                ) {
                    Text(mainButtonText)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { context.startActivity(Intent(context, AppSelectionActivity::class.java)) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Select Apps to Block")
                }
            }
        }
    }
}


// --- PERMISSION CHECKING LOGIC ---
// (No changes needed here, but included for completeness)
private fun hasPermission(context: android.content.Context): Boolean {
    val accessibilityEnabled = hasAccessibilityPermission(context)
    val notificationListenerEnabled = hasNotificationListenerPermission(context)

    if (!accessibilityEnabled || !notificationListenerEnabled) {
        Toast.makeText(context, "Please grant necessary permissions first", Toast.LENGTH_LONG).show()
        if (!accessibilityEnabled) requestAccessibilityPermission(context)
        if (!notificationListenerEnabled) requestNotificationListenerPermission(context)
        return false
    }
    return true
}

private fun hasAccessibilityPermission(context: android.content.Context): Boolean {
    val service = ComponentName(context, FocusAccessibilityService::class.java).flattenToString()
    val settingValue = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    return settingValue?.let { TextUtils.SimpleStringSplitter(':').apply { setString(it) }.any { s -> s.equals(service, ignoreCase = true) } } ?: false
}

private fun hasNotificationListenerPermission(context: android.content.Context): Boolean {
    val service = ComponentName(context, FocusNotificationListener::class.java).flattenToString()
    val settingValue = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return settingValue?.let { TextUtils.SimpleStringSplitter(':').apply { setString(it) }.any { s -> s.equals(service, ignoreCase = true) } } ?: false
}

private fun requestAccessibilityPermission(context: android.content.Context) {
    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
}

private fun requestNotificationListenerPermission(context: android.content.Context) {
    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
}