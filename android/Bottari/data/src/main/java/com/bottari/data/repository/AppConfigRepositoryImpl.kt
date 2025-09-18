package com.bottari.data.repository

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.toBottariResult
import com.bottari.domain.repository.AppConfigRepository

class AppConfigRepositoryImpl(
    private val dataSource: AppConfigDataSource,
) : AppConfigRepository {
    override suspend fun savePermissionFlag(flag: Boolean): BottariResult<Unit> = dataSource.savePermissionFlag(flag).toBottariResult()

    override suspend fun getPermissionFlag(): BottariResult<Boolean> = dataSource.getPermissionFlag().toBottariResult()
}
