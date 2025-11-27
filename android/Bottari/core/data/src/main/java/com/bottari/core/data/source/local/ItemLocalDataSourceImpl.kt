package com.bottari.core.data.source.local

import com.bottari.core.local.database.bottari.ItemDao
import com.bottari.core.local.entity.bottari.ItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemLocalDataSourceImpl @Inject constructor(
    private val dao: ItemDao,
) : ItemLocalDataSource {
    override fun fetchItems(bottariId: Long): Flow<List<ItemEntity>> = dao.fetchItems(bottariId)

    override suspend fun saveItem(item: ItemEntity): Result<Unit> = runCatching { dao.saveItem(item) }

    override suspend fun deleteItem(id: Long): Result<Unit> = runCatching { dao.deleteItem(id) }

    override suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit> = runCatching { dao.updateCheckState(id, isChecked) }

    override suspend fun resetCheckState(bottariId: Long): Result<Unit> = runCatching { dao.resetCheckState(bottariId) }
}
