package com.bottari.data.source.remote

import com.bottari.data.model.item.FetchChecklistResponse

interface BottariItemRemoteDataSource {
    suspend fun fetchChecklist(ssaid: String, bottariId: Long): Result<List<FetchChecklistResponse>>
    suspend fun uncheckBottariItem(ssaid: String, bottariItemId: Long): Result<Unit>
    suspend fun checkBottariItem(ssaid: String, bottariItemId: Long): Result<Unit>
}
