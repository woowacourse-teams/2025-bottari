package com.bottari.domain.repository

import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.exception.BottariResult

interface BottariItemRepository {
    suspend fun fetchChecklist(bottariId: Long): BottariResult<List<ChecklistItem>>

    suspend fun uncheckBottariItem(bottariItemId: Long): BottariResult<Unit>

    suspend fun checkBottariItem(bottariItemId: Long): BottariResult<Unit>

    suspend fun saveBottariItems(
        bottariId: Long,
        deleteItemIds: List<Long>,
        createItemNames: List<String>,
    ): BottariResult<Unit>

    suspend fun resetBottariItemCheckState(bottariId: Long): BottariResult<Unit>
}
