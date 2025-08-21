package com.bottari.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class MemberInfoDataStore(
    private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)
    private val keyMemberId = longPreferencesKey(KEY_MEMBER_ID)

    suspend fun saveMemberId(memberId: Long): Result<Unit> =
        runCatching {
            context.dataStore.edit { prefs ->
                prefs[keyMemberId] = memberId
            }
        }

    suspend fun getMemberId(): Result<Long> =
        runCatching {
            val prefs = context.dataStore.data.first()
            val memberId = prefs[keyMemberId]
            requireNotNull(memberId) { ERROR_MEMBER_ID_NULL }
        }

    companion object {
        private const val DATASTORE_NAME = "user_info"
        private const val KEY_MEMBER_ID = "KEY_MEMBER_ID"
        private const val ERROR_MEMBER_ID_NULL = "[ERROR] 회원 ID가 null 입니다"
    }
}
