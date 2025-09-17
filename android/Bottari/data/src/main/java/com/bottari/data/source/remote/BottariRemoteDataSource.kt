package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.BottariCreateRequest
import com.bottari.data.model.remote.bottari.BottariFetchResponse
import com.bottari.data.model.remote.bottari.BottariTitleUpdateRequest
import com.bottari.data.model.remote.bottari.BottariesFetchResponse

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(): Result<List<BottariesFetchResponse>>

    suspend fun createBottari(bottariCreateRequest: BottariCreateRequest): Result<Long>

    suspend fun fetchBottariDetail(id: Long): Result<BottariFetchResponse>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        request: BottariTitleUpdateRequest,
    ): Result<Unit>
}
