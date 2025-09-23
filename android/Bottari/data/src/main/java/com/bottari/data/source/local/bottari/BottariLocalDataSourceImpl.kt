package com.bottari.data.source.local.bottari

import com.bottari.data.local.bottari.BottariDao
import com.bottari.data.model.local.bottari.BottariEntity
import kotlinx.coroutines.flow.Flow

class BottariLocalDataSourceImpl(
    private val dao: BottariDao,
) : BottariLocalDataSource {
    override fun fetchBottaries(): Result<Flow<List<BottariEntity>>> = runCatching { dao.fetchBottaries() }

    override suspend fun saveBottari(bottari: BottariEntity): Result<Unit> = runCatching { dao.saveBottari(bottari) }

    override suspend fun deleteBottari(bottariId: Long): Result<Unit> = runCatching { dao.deleteBottari(bottariId) }

    override suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    ): Result<Unit> = runCatching { dao.updateBottariTitle(bottariId, title) }
}
