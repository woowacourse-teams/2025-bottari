package com.bottari.data.repository

import com.bottari.data.mapper.BottariItemMapper.toDomain
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
}
