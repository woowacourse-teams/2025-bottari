package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.BottariCreateRequest
import com.bottari.data.model.remote.bottari.BottariFetchResponse
import com.bottari.data.model.remote.bottari.BottariTitleUpdateRequest
import com.bottari.data.model.remote.bottari.BottariesFetchResponse
import com.bottari.domain.model.exception.BottariResult

interface BottariRemoteDataSource {
    suspend fun fetchBottaries(): BottariResult<List<BottariesFetchResponse>>

    suspend fun createBottari(bottariCreateRequest: BottariCreateRequest): BottariResult<Long>

    suspend fun fetchBottariDetail(id: Long): BottariResult<BottariFetchResponse>

    suspend fun deleteBottari(id: Long): BottariResult<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        request: BottariTitleUpdateRequest,
    ): BottariResult<Unit>
}
