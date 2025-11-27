package com.bottari.core.data.source.local

import com.bottari.core.local.datastore.AppConfigDataStore
import javax.inject.Inject

class AppConfigLocalDataSourceImpl @Inject constructor(
    private val dataStore: AppConfigDataStore,
) : AppConfigDataSource {
    override suspend fun savePermissionFlag(flag: Boolean): Result<Unit> =
        runCatching {
            dataStore.savePermissionFlag(flag)
        }

    override suspend fun getPermissionFlag(): Result<Boolean> = runCatching { dataStore.getPermissionFlag() }
}
