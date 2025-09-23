package com.bottari.data.source.local.bottari

import com.bottari.data.model.local.bottari.ItemEntity
import kotlinx.coroutines.flow.Flow

interface ItemLocalDataSource {
    fun fetchItems(bottariId: Long): Result<Flow<List<ItemEntity>>>

    suspend fun saveItems(items: List<ItemEntity>): Result<Unit>

    suspend fun deleteItem(id: Long): Result<Unit>

    suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit>

    suspend fun resetCheckState(bottariId: Long): Result<Unit>
}
