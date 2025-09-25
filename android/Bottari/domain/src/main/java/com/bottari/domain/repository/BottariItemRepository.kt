package com.bottari.domain.repository

import com.bottari.domain.model.bottari.item.ChecklistItem
import kotlinx.coroutines.flow.Flow

interface BottariItemRepository {
    fun fetchItems(bottariId: Long): Flow<List<ChecklistItem>>

    suspend fun saveItems(
        bottariId: Long,
        items: List<String>,
    ): Result<Unit>

    suspend fun deleteItem(id: Long): Result<Unit>

    suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit>

    suspend fun resetCheckState(bottariId: Long): Result<Unit>
}
