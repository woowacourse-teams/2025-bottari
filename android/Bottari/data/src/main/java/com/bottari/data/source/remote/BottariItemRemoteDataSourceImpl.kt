package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.model.item.SaveBottariItemsRequest
import com.bottari.data.service.BottariItemService

class BottariItemRemoteDataSourceImpl(
    private val bottariItemService: BottariItemService,
) : BottariItemRemoteDataSource {
    override suspend fun fetchChecklist(bottariId: Long): Result<List<FetchChecklistResponse>> =
        safeApiCall {
            bottariItemService.fetchChecklist(bottariId)
        }

    override suspend fun uncheckBottariItem(bottariItemId: Long): Result<Unit> =
        safeApiCall {
            bottariItemService.uncheckBottariItem(bottariItemId)
        }

    override suspend fun checkBottariItem(bottariItemId: Long): Result<Unit> =
        safeApiCall {
            bottariItemService.checkBottariItem(bottariItemId)
        }

    override suspend fun saveBottariItems(
        bottariId: Long,
        request: SaveBottariItemsRequest,
    ): Result<Unit> =
        safeApiCall {
            bottariItemService.saveBottariItems(bottariId, request)
        }

    override suspend fun resetBottariItemCheckState(bottariId: Long): Result<Unit> =
        safeApiCall {
            bottariItemService.resetBottariItemCheckState(bottariId)
        }
}
