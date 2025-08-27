package com.rino.focusmode // Change this to your package name

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.rino.focusmode.MainActivity.Companion.isFocusModeActive

class FocusNotificationListener : NotificationListenerService() {

    // Use the same list of blocked apps
//    private val blockedApps = listOf("com.android.chrome", "com.instagram.android", "com.facebook.katana", "org.mozilla.firefox", "com.whatsapp");
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val blockedApps = AppBlockerManager.getBlockedApps(this)

        // If sbn is null or Focus Mode is off, do nothing.
        if (sbn == null || !MainActivity.isFocusModeActive.value) {
            return
        }

        val packageName = sbn.packageName

        // If the notification is from a blocked app...
        if (blockedApps.contains(packageName)) {
            // ...cancel it immediately!
            cancelNotification(sbn.key)
        }
    }
}