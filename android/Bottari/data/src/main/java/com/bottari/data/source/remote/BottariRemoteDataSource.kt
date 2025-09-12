package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariRequest
import com.bottari.data.model.bottari.BottariesFetchResponse

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(): Result<List<BottariesFetchResponse>>

    suspend fun createBottari(createBottariRequest: BottariRequest.CreateBottariRequest): Result<Long>

    suspend fun fetchBottariDetail(id: Long): Result<BottariFetchResponse>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        request: BottariRequest.UpdateBottariTitleRequest,
    ): Result<Unit>
}
