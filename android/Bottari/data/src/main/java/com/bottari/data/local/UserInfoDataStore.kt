package com.bottari.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class UserInfoDataStore(
    private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)
    private val keyUserId = stringPreferencesKey(KEY_USER_ID)

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { prefs ->
            prefs[keyUserId] = userId
        }
    }

    suspend fun getUserId(): String {
        val prefs = context.dataStore.data.first()
        return prefs[keyUserId] ?: ""
    }

    companion object {
        private const val DATASTORE_NAME = "user_info"
        private const val KEY_USER_ID = "KEY_USER_ID"
    }
}
