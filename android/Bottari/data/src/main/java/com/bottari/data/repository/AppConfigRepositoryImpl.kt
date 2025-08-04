package com.bottari.data.repository

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.domain.repository.AppConfigRepository

class AppConfigRepositoryImpl(
    private val dataSource: AppConfigDataSource,
) : AppConfigRepository {
    override suspend fun savePermissionFlag(flag: Boolean): Result<Unit> = dataSource.savePermissionFlag(flag)

    override suspend fun getPermissionFlag(): Result<Boolean> = dataSource.getPermissionFlag()
}
