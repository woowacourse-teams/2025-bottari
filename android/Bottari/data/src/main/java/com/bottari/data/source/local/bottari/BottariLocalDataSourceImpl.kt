package com.bottari.data.source.local.bottari

import com.bottari.data.local.bottari.BottariDao
import com.bottari.data.local.bottari.BottariDatabase
import com.bottari.data.model.local.bottari.BottariEntity
import com.bottari.data.model.local.bottari.BottariWithAlarmAndItems
import kotlinx.coroutines.flow.Flow

class BottariLocalDataSourceImpl(
    private val database: BottariDatabase,
) : BottariLocalDataSource {
    private val dao: BottariDao = database.bottariDao()

    override fun fetchBottaries(): Flow<List<BottariWithAlarmAndItems>> = dao.fetchBottariesWithAlarmAndItems()

    override fun findBottari(id: Long): Flow<BottariWithAlarmAndItems?> = dao.findBottariWithAlarmAndItems(id)

    override suspend fun createBottari(bottari: BottariEntity): Result<Long> = runCatching { dao.createBottari(bottari) }

    override suspend fun createBottariWithItems(
        bottari: BottariEntity,
        itemNames: List<String>,
    ): Result<Long> = runCatching { database.createBottariWithItems(bottari, itemNames) }

    override suspend fun deleteBottari(bottariId: Long): Result<Unit> = runCatching { dao.deleteBottari(bottariId) }

    override suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    ): Result<Unit> = runCatching { dao.updateBottariTitle(bottariId, title) }
}
