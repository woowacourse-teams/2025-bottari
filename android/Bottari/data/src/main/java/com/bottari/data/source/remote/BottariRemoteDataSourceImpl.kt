package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.BottariCreateRequest
import com.bottari.data.model.remote.bottari.BottariFetchResponse
import com.bottari.data.model.remote.bottari.BottariTitleUpdateRequest
import com.bottari.data.model.remote.bottari.BottariesFetchResponse
import com.bottari.data.service.BottariService
import com.bottari.domain.model.exception.BottariResult

class BottariRemoteDataSourceImpl(
    private val bottariService: BottariService,
) : BottariRemoteDataSource {
    override suspend fun fetchBottaries(): BottariResult<List<BottariesFetchResponse>> = bottariService.fetchBottaries()

    override suspend fun fetchBottariDetail(id: Long): BottariResult<BottariFetchResponse> = bottariService.fetchBottari(id = id)

    override suspend fun createBottari(bottariCreateRequest: BottariCreateRequest): BottariResult<Long> =
        bottariService.createBottari(bottariCreateRequest)

    override suspend fun deleteBottari(id: Long): BottariResult<Unit> = bottariService.deleteBottari(id = id)

    override suspend fun saveBottariTitle(
        id: Long,
        request: BottariTitleUpdateRequest,
    ): BottariResult<Unit> = bottariService.saveBottariTitle(id = id, request = request)
}
