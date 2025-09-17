package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.bottari.BottariCreateRequest
import com.bottari.data.model.remote.bottari.BottariFetchResponse
import com.bottari.data.model.remote.bottari.BottariTitleUpdateRequest
import com.bottari.data.model.remote.bottari.BottariesFetchResponse
import com.bottari.data.service.bottari.BottariService

class BottariRemoteDataSourceImpl(
    private val bottariService: BottariService,
) : BottariRemoteDataSource {
    override suspend fun fetchBottaries(): Result<List<BottariesFetchResponse>> =
        safeApiCall {
            bottariService.fetchBottaries()
        }

    override suspend fun fetchBottariDetail(id: Long): Result<BottariFetchResponse> =
        safeApiCall {
            bottariService.fetchBottari(id = id)
        }

    override suspend fun createBottari(bottariCreateRequest: BottariCreateRequest): Result<Long> =
        runCatching {
            val response = bottariService.createBottari(bottariCreateRequest)
            val id = response.extractIdFromHeader(HEADER_BOTTARI_ID_PREFIX)
            checkNotNull(id)
        }

    override suspend fun deleteBottari(id: Long): Result<Unit> =
        safeApiCall {
            bottariService.deleteBottari(id = id)
        }

    override suspend fun saveBottariTitle(
        id: Long,
        request: BottariTitleUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            bottariService.saveBottariTitle(id = id, request = request)
        }

    companion object {
        private const val HEADER_BOTTARI_ID_PREFIX = "/bottaries/"
    }
}
