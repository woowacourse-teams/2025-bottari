package com.bottari.core.data.source.local

import androidx.room.withTransaction
import com.bottari.core.local.database.bottari.BottariDao
import com.bottari.core.local.database.bottari.BottariDatabase
import com.bottari.core.local.entity.bottari.BottariEntity
import com.bottari.core.local.entity.bottari.BottariWithAlarm
import com.bottari.core.local.entity.bottari.BottariWithAlarmAndItems
import com.bottari.core.local.entity.bottari.ItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BottariLocalDataSourceImpl @Inject constructor(
    private val database: BottariDatabase,
) : BottariLocalDataSource {
    private val dao: BottariDao = database.bottariDao()

    override fun fetchBottaries(): Flow<List<BottariWithAlarmAndItems>> = dao.fetchBottariesWithAlarmAndItems()

    override suspend fun fetchBottariesWithAlarm(): Result<List<BottariWithAlarm>> = runCatching { dao.fetchBottariesWithAlarm() }

    override fun findBottari(id: Long): Flow<BottariWithAlarmAndItems?> = dao.findBottariWithAlarmAndItems(id)

    override suspend fun createBottari(bottari: BottariEntity): Result<Long> = runCatching { dao.createBottari(bottari) }

    override suspend fun createBottariWithItems(
        bottari: BottariEntity,
        itemNames: List<String>,
    ): Result<Long> =
        runCatching {
            database.withTransaction {
                val bottariId = database.bottariDao().createBottari(bottari)
                val items =
                    Array(itemNames.size) { index -> ItemEntity.from(bottariId, itemNames[index]) }
                database.itemDao().saveItem(*items)
                bottariId
            }
        }

    override suspend fun deleteBottari(bottariId: Long): Result<Unit> = runCatching { dao.deleteBottari(bottariId) }

    override suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    ): Result<Unit> = runCatching { dao.updateBottariTitle(bottariId, title) }
}
