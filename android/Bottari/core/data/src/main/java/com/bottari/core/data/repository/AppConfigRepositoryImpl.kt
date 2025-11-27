package com.bottari.core.data.repository

import com.bottari.core.data.source.local.AppConfigDataSource
import com.bottari.core.domain.repository.AppConfigRepository
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(
    private val dataSource: AppConfigDataSource,
) : AppConfigRepository {
    override suspend fun savePermissionFlag(flag: Boolean): Result<Unit> = dataSource.savePermissionFlag(flag)

    override suspend fun getPermissionFlag(): Result<Boolean> = dataSource.getPermissionFlag()
}
