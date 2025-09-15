package com.bottari.data.repository

import com.bottari.data.mapper.bottari.item.BottariItemMapper.toChecklistItem
import com.bottari.data.model.bottari.item.ItemsSaveRequest
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.repository.BottariItemRepository

class BottariItemRepositoryImpl(
    private val bottariItemRemoteDataSource: BottariItemRemoteDataSource,
) : BottariItemRepository {
    override suspend fun fetchChecklist(bottariId: Long): Result<List<ChecklistItem>> =
        bottariItemRemoteDataSource
            .fetchChecklist(bottariId)
            .mapCatching { checklist -> checklist.map { bottariItem -> bottariItem.toChecklistItem() } }

    override suspend fun uncheckBottariItem(bottariItemId: Long): Result<Unit> =
        bottariItemRemoteDataSource.uncheckBottariItem(bottariItemId)

    override suspend fun checkBottariItem(bottariItemId: Long): Result<Unit> = bottariItemRemoteDataSource.checkBottariItem(bottariItemId)

    override suspend fun saveBottariItems(
        bottariId: Long,
        deleteItemIds: List<Long>,
        createItemNames: List<String>,
    ): Result<Unit> =
        bottariItemRemoteDataSource.saveBottariItems(
            bottariId,
            ItemsSaveRequest(
                deleteItemIds = deleteItemIds,
                createItemNames = createItemNames,
            ),
        )

    override suspend fun resetBottariItemCheckState(bottariId: Long): Result<Unit> =
        bottariItemRemoteDataSource.resetBottariItemCheckState(bottariId)
}
