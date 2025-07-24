package com.bottari.data.repository

import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.model.item.SaveBottariItemsRequest
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.repository.BottariItemRepository

class BottariItemRepositoryImpl(
    private val bottariItemRemoteDataSource: BottariItemRemoteDataSource,
) : BottariItemRepository {
    override suspend fun fetchChecklist(
        ssaid: String,
        bottariId: Long,
    ): Result<List<BottariItem>> =
        bottariItemRemoteDataSource
            .fetchChecklist(ssaid, bottariId)
            .mapCatching { checklist -> checklist.map { bottariItem -> bottariItem.toDomain() } }

    override suspend fun uncheckBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit> = bottariItemRemoteDataSource.uncheckBottariItem(ssaid, bottariItemId)

    override suspend fun checkBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit> = bottariItemRemoteDataSource.checkBottariItem(ssaid, bottariItemId)

    override suspend fun saveBottariItems(
        ssaid: String,
        bottariId: Long,
        deleteItemIds: List<Long>,
        createItemNames: List<String>,
    ): Result<Unit> =
        bottariItemRemoteDataSource.saveBottariItems(
            ssaid,
            bottariId,
            SaveBottariItemsRequest(
                deleteItemIds = deleteItemIds,
                createItemNames = createItemNames,
            ),
        )
}
