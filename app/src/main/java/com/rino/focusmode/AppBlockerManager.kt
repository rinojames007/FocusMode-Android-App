package com.rino.focusmode

import android.content.Context

object AppBlockerManager {

    private const val PREFS_NAME = "FocusModePrefs"
    private const val KEY_BLOCKED_APPS = "blockedAppsSet"

    fun saveBlockedApps(context: Context, blockedApps: Set<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(KEY_BLOCKED_APPS, blockedApps).apply()
    }

    fun getBlockedApps(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_BLOCKED_APPS, emptySet()) ?: emptySet()
    }
}