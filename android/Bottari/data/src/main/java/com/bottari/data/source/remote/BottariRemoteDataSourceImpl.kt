package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.UpdateBottariTitleRequest
import com.bottari.data.service.BottariService

class BottariRemoteDataSourceImpl(
    private val bottariService: BottariService,
) : BottariRemoteDataSource {
    override suspend fun fetchBottaries(): Result<List<FetchBottariesResponse>> =
        safeApiCall {
            bottariService.fetchBottaries()
        }

    override suspend fun fetchBottariDetail(id: Long): Result<BottariResponse> =
        safeApiCall {
            bottariService.findBottari(id = id)
        }

    override suspend fun createBottari(createBottariRequest: CreateBottariRequest): Result<Long?> =
        runCatching {
            val response = bottariService.createBottari(createBottariRequest)
            response.extractIdFromHeader(HEADER_BOTTARI_ID_PREFIX)
        }

    override suspend fun deleteBottari(id: Long): Result<Unit> =
        safeApiCall {
            bottariService.deleteBottari(id = id)
        }

    override suspend fun saveBottariTitle(
        id: Long,
        request: UpdateBottariTitleRequest,
    ): Result<Unit> =
        safeApiCall {
            bottariService.saveBottariTitle(id = id, request = request)
        }

    companion object {
        private const val HEADER_BOTTARI_ID_PREFIX = "/bottaries/"
    }
}
