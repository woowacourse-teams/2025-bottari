package com.bottari.data.source.local

import com.bottari.data.local.UserInfoDataStore

class UserInfoLocalDataSourceImpl(
    private val dataStore: UserInfoDataStore,
) : UserInfoLocalDataSource {
    override suspend fun saveUserId(userId: String): Result<Unit> =
        runCatching {
            dataStore.saveUserId(userId)
        }

    override suspend fun getUserId(): Result<String> =
        runCatching {
            dataStore.getUserId()
        }
}
