package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.UpdateBottariTitleRequest

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(ssaid: String): Result<List<FetchBottariesResponse>>

    suspend fun createBottari(
        ssaid: String,
        createBottariRequest: CreateBottariRequest,
    ): Result<Long?>

    suspend fun fetchBottariDetail(
        id: Long,
        ssaid: String,
    ): Result<BottariResponse>

    suspend fun deleteBottari(
        id: Long,
        ssaid: String,
    ): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        ssaid: String,
        request: UpdateBottariTitleRequest,
    ): Result<Unit>
}
