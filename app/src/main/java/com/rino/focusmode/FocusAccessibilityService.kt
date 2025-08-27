package com.rino.focusmode // Change this to your package name

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.rino.focusmode.MainActivity.Companion.isFocusModeActive

class FocusAccessibilityService : AccessibilityService() {

    // A simple list of apps to block. In a real app, you'd let the user pick.
//    private val blockedApps = listOf("com.android.chrome", "com.instagram.android", "com.facebook.katana", "org.mozilla.firefox", "com.whatsapp")

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // We only care about when a window's state changes (e.g., an app opens)
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            val blockedApps = AppBlockerManager.getBlockedApps(this)

            // Check if Focus Mode is actually turned on
            if (!MainActivity.isFocusModeActive.value) {
                return
            }

            val packageName = event.packageName?.toString()

            // If the app that just opened is in our blocked list...
            if (packageName != null && blockedApps.contains(packageName)) {
                // ...then send the user back to the home screen!
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(homeIntent)
            }
        }
    }

    override fun onInterrupt() {
        // This is called when the service is interrupted. We don't need to do anything here.
    }
}