package com.bottari.data.repository

import com.bottari.data.model.remote.bottari.item.ItemsSaveRequest
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.mapCatching
import com.bottari.domain.repository.BottariItemRepository

class BottariItemRepositoryImpl(
    private val bottariItemRemoteDataSource: BottariItemRemoteDataSource,
) : BottariItemRepository {
    override suspend fun fetchChecklist(bottariId: Long): BottariResult<List<ChecklistItem>> =
        bottariItemRemoteDataSource
            .fetchChecklist(bottariId)
            .mapCatching { checklist -> checklist.map { bottariItem -> bottariItem.toDomain() } }

    override suspend fun uncheckBottariItem(bottariItemId: Long): BottariResult<Unit> =
        bottariItemRemoteDataSource.uncheckBottariItem(bottariItemId)

    override suspend fun checkBottariItem(bottariItemId: Long): BottariResult<Unit> =
        bottariItemRemoteDataSource.checkBottariItem(bottariItemId)

    override suspend fun saveBottariItems(
        bottariId: Long,
        deleteItemIds: List<Long>,
        createItemNames: List<String>,
    ): BottariResult<Unit> =
        bottariItemRemoteDataSource.saveBottariItems(
            bottariId,
            ItemsSaveRequest(
                deleteItemIds = deleteItemIds,
                createItemNames = createItemNames,
            ),
        )

    override suspend fun resetBottariItemCheckState(bottariId: Long): BottariResult<Unit> =
        bottariItemRemoteDataSource.resetBottariItemCheckState(bottariId)
}
