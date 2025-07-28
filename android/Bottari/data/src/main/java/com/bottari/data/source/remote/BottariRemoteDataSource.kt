package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(ssaid: String): Result<List<FetchBottariesResponse>>

    suspend fun createBottari(
        ssaid: String,
        createBottariRequest: CreateBottariRequest,
    ): Result<Long?>

    suspend fun findBottari(
        id: Long,
        ssaid: String,
    ): Result<BottariResponse>

    suspend fun deleteBottari(
        id: Long,
        ssaid: String,
    ): Result<Unit>
}
