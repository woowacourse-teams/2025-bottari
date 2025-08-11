package com.bottari.data.source.remote

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.model.item.SaveBottariItemsRequest

interface BottariItemRemoteDataSource {
    suspend fun fetchChecklist(bottariId: Long): Result<List<FetchChecklistResponse>>

    suspend fun uncheckBottariItem(bottariItemId: Long): Result<Unit>

    suspend fun checkBottariItem(bottariItemId: Long): Result<Unit>

    suspend fun saveBottariItems(
        bottariId: Long,
        request: SaveBottariItemsRequest,
    ): Result<Unit>
}
