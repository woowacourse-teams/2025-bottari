package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariRequest
import com.bottari.data.model.bottari.BottariResponse

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(): Result<List<BottariResponse.FetchBottariesResponse>>

    suspend fun createBottari(createBottariRequest: BottariRequest.CreateBottariRequest): Result<Long>

    suspend fun fetchBottariDetail(id: Long): Result<BottariResponse.FetchBottariResponse>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        request: BottariRequest.UpdateBottariTitleRequest,
    ): Result<Unit>
}
