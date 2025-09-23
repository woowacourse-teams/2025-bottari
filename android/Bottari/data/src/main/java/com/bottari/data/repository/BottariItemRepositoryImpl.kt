package com.bottari.data.repository

import com.bottari.data.model.local.bottari.ItemEntity
import com.bottari.data.source.local.bottari.ItemLocalDataSource
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.repository.BottariItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BottariItemRepositoryImpl(
    private val itemLocalDataSource: ItemLocalDataSource,
) : BottariItemRepository {
    override fun fetchItems(bottariId: Long): Flow<List<ChecklistItem>> =
        itemLocalDataSource
            .fetchItems(bottariId)
            .map { items -> items.map(ItemEntity::toDomain) }

    override suspend fun saveItems(
        bottariId: Long,
        items: List<String>,
    ): Result<Unit> {
        val itemEntities = items.map { item -> ItemEntity.from(bottariId, item) }
        return itemLocalDataSource.saveItems(itemEntities)
    }

    override suspend fun deleteItem(id: Long): Result<Unit> = itemLocalDataSource.deleteItem(id)

    override suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit> = itemLocalDataSource.updateCheckState(id, isChecked)

    override suspend fun resetCheckState(bottariId: Long): Result<Unit> = itemLocalDataSource.resetCheckState(bottariId)
}
