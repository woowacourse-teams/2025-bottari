package com.bottari.data.source.remote

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.model.item.SaveBottariItemsRequest

interface BottariItemRemoteDataSource {
    suspend fun fetchChecklist(
        ssaid: String,
        bottariId: Long,
    ): Result<List<FetchChecklistResponse>>

    suspend fun uncheckBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit>

    suspend fun checkBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit>

    suspend fun saveBottariItems(
        ssaid: String,
        bottariId: Long,
        request: SaveBottariItemsRequest,
    ): Result<Unit>
}
