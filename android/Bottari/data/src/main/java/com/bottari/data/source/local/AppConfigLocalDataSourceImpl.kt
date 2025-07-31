package com.bottari.data.source.local

import com.bottari.data.local.AppConfigDataStore

class AppConfigLocalDataSourceImpl(
    private val dataStore: AppConfigDataStore,
) : AppConfigDataSource {
    override suspend fun savePermissionFlag(flag: Boolean): Result<Unit> =
        runCatching {
            dataStore.savePermissionFlag(flag)
        }

    override suspend fun getPermissionFlag(): Result<Boolean> = runCatching { dataStore.getPermissionFlag() }
}
