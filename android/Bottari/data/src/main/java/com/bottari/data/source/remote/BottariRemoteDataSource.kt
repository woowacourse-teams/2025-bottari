package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.UpdateBottariTitleRequest

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(): Result<List<FetchBottariesResponse>>

    suspend fun createBottari(createBottariRequest: CreateBottariRequest): Result<Long?>

    suspend fun fetchBottariDetail(id: Long): Result<BottariResponse>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        request: UpdateBottariTitleRequest,
    ): Result<Unit>
}
