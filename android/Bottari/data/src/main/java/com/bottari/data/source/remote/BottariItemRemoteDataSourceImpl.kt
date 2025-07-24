package com.bottari.data.source.remote

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.model.item.SaveBottariItemsRequest
import com.bottari.data.service.BottariItemService
import com.bottari.data.util.safeApiCall

class BottariItemRemoteDataSourceImpl(
    private val bottariItemService: BottariItemService,
) : BottariItemRemoteDataSource {
    override suspend fun fetchChecklist(
        ssaid: String,
        bottariId: Long,
    ): Result<List<FetchChecklistResponse>> =
        safeApiCall {
            bottariItemService.fetchChecklist(ssaid, bottariId)
        }

    override suspend fun uncheckBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit> =
        safeApiCall {
            bottariItemService.uncheckBottariItem(ssaid, bottariItemId)
        }

    override suspend fun checkBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit> =
        safeApiCall {
            bottariItemService.checkBottariItem(ssaid, bottariItemId)
        }

    override suspend fun saveBottariItems(
        ssaid: String,
        bottariId: Long,
        request: SaveBottariItemsRequest,
    ): Result<Unit> =
        safeApiCall {
            bottariItemService.saveBottariItems(ssaid, bottariId, request)
        }
}
