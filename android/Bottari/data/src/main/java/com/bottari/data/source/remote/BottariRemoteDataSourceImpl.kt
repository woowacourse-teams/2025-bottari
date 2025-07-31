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
    override suspend fun fetchBottaries(ssaid: String): Result<List<FetchBottariesResponse>> =
        safeApiCall {
            bottariService.fetchBottaries(ssaid)
        }

    override suspend fun findBottari(
        id: Long,
        ssaid: String,
    ): Result<BottariResponse> =
        safeApiCall {
            bottariService.findBottari(id = id, ssaid = ssaid)
        }

    override suspend fun createBottari(
        ssaid: String,
        createBottariRequest: CreateBottariRequest,
    ): Result<Long?> =
        runCatching {
            val response = bottariService.createBottari(ssaid, createBottariRequest)
            response.extractIdFromHeader(HEADER_BOTTARI_ID_PREFIX)
        }

    override suspend fun deleteBottari(
        id: Long,
        ssaid: String,
    ): Result<Unit> =
        safeApiCall {
            bottariService.deleteBottari(id = id, ssaid = ssaid)
        }

    override suspend fun saveBottariTitle(
        id: Long,
        ssaid: String,
        request: UpdateBottariTitleRequest,
    ): Result<Unit> =
        safeApiCall {
            bottariService.saveBottariTitle(id = id, ssaid = ssaid, request = request)
        }

    companion object {
        private const val HEADER_BOTTARI_ID_PREFIX = "/bottaries/"
    }
}
