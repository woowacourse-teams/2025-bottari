package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.item.ItemFetchResponse
import com.bottari.data.model.remote.bottari.item.ItemsSaveRequest
import com.bottari.domain.model.exception.BottariResult

interface BottariItemRemoteDataSource {
    suspend fun fetchChecklist(bottariId: Long): BottariResult<List<ItemFetchResponse>>

    suspend fun uncheckBottariItem(bottariItemId: Long): BottariResult<Unit>

    suspend fun checkBottariItem(bottariItemId: Long): BottariResult<Unit>

    suspend fun saveBottariItems(
        bottariId: Long,
        request: ItemsSaveRequest,
    ): BottariResult<Unit>

    suspend fun resetBottariItemCheckState(bottariId: Long): BottariResult<Unit>
}
