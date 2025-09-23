package com.bottari.data.source.local.bottari

import com.bottari.data.local.bottari.ItemDao
import com.bottari.data.model.local.bottari.ItemEntity
import kotlinx.coroutines.flow.Flow

class ItemLocalDataSourceImpl(
    private val dao: ItemDao,
) : ItemLocalDataSource {
    override fun fetchItems(bottariId: Long): Result<Flow<List<ItemEntity>>> = runCatching { dao.fetchItems(bottariId) }

    override suspend fun saveItems(items: List<ItemEntity>): Result<Unit> = runCatching { dao.saveItem(*items.toTypedArray()) }

    override suspend fun deleteItem(id: Long): Result<Unit> = runCatching { dao.deleteItem(id) }

    override suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit> = runCatching { dao.updateCheckState(id, isChecked) }

    override suspend fun resetCheckState(bottariId: Long): Result<Unit> = runCatching { dao.resetCheckState(bottariId) }
}
