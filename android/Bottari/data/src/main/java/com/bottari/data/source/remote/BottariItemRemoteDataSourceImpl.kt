package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.item.ItemFetchResponse
import com.bottari.data.model.remote.bottari.item.ItemsSaveRequest
import com.bottari.data.service.BottariItemService
import com.bottari.domain.model.exception.BottariResult

class BottariItemRemoteDataSourceImpl(
    private val bottariItemService: BottariItemService,
) : BottariItemRemoteDataSource {
    override suspend fun fetchChecklist(bottariId: Long): BottariResult<List<ItemFetchResponse>> =
        bottariItemService.fetchChecklist(bottariId)

    override suspend fun uncheckBottariItem(bottariItemId: Long): BottariResult<Unit> = bottariItemService.uncheckBottariItem(bottariItemId)

    override suspend fun checkBottariItem(bottariItemId: Long): BottariResult<Unit> = bottariItemService.checkBottariItem(bottariItemId)

    override suspend fun saveBottariItems(
        bottariId: Long,
        request: ItemsSaveRequest,
    ): BottariResult<Unit> = bottariItemService.saveBottariItems(bottariId, request)

    override suspend fun resetBottariItemCheckState(bottariId: Long): BottariResult<Unit> =
        bottariItemService.resetBottariItemCheckState(bottariId)
}
