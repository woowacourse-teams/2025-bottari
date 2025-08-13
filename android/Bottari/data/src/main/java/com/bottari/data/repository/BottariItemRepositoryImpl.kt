package com.bottari.data.repository

import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.model.item.SaveBottariItemsRequest
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.repository.BottariItemRepository

class BottariItemRepositoryImpl(
    private val bottariItemRemoteDataSource: BottariItemRemoteDataSource,
) : BottariItemRepository {
    override suspend fun fetchChecklist(bottariId: Long): Result<List<ChecklistItem>> =
        bottariItemRemoteDataSource
            .fetchChecklist(bottariId)
            .mapCatching { checklist -> checklist.map { bottariItem -> bottariItem.toDomain() } }

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
            SaveBottariItemsRequest(
                deleteItemIds = deleteItemIds,
                createItemNames = createItemNames,
            ),
        )
}
