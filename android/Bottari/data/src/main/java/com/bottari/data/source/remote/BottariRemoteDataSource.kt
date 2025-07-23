package com.bottari.data.source.remote

import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(ssaid: String): Result<List<FetchBottariesResponse>>

    suspend fun createBottari(ssaid: String, createBottariRequest: CreateBottariRequest): Result<Long>
}
