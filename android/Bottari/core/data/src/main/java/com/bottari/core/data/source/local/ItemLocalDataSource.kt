package com.bottari.core.data.source.local

import com.bottari.core.local.entity.bottari.ItemEntity
import kotlinx.coroutines.flow.Flow

interface ItemLocalDataSource {
    fun fetchItems(bottariId: Long): Flow<List<ItemEntity>>

    suspend fun saveItem(item: ItemEntity): Result<Unit>

    suspend fun deleteItem(id: Long): Result<Unit>

    suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit>

    suspend fun resetCheckState(bottariId: Long): Result<Unit>
}
