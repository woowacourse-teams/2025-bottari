package com.bottari.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppConfigDataStore(
    private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)
    private val keyPermissionFlag = booleanPreferencesKey(KEY_PERMISSION_FLAG)

    suspend fun savePermissionFlag(flag: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[keyPermissionFlag] = flag
        }
    }

    suspend fun getPermissionFlag(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[keyPermissionFlag] ?: false
    }

    companion object {
        private const val DATASTORE_NAME = "app_config"
        private const val KEY_PERMISSION_FLAG = "KEY_PERMISSION_FLAG"
    }
}
