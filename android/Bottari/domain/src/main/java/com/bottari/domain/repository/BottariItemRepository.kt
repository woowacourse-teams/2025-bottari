package com.bottari.domain.repository

import com.bottari.domain.model.bottari.BottariItem

interface BottariItemRepository {
    suspend fun fetchChecklist(bottariId: Long): Result<List<BottariItem>>

    suspend fun uncheckBottariItem(bottariItemId: Long): Result<Unit>

    suspend fun checkBottariItem(bottariItemId: Long): Result<Unit>

    suspend fun saveBottariItems(
        bottariId: Long,
        deleteItemIds: List<Long>,
        createItemNames: List<String>,
    ): Result<Unit>
}
