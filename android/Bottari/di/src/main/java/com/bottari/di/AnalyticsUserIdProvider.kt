package com.bottari.di

import android.content.Context
import androidx.core.content.edit
import java.util.UUID

object AnalyticsUserIdProvider {
    private const val KEY_USER_ID = "analytics_user_id"
    private const val PREFS_NAME = "analytics_prefs"

    fun getOrCreate(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_ID, null) ?: UUID.randomUUID().toString().also {
            prefs.edit { putString(KEY_USER_ID, it) }
        }
    }
}
