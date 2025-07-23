package com.bottari.data.source.remote

import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.service.BottariService
import com.bottari.data.util.safeApiCall
import retrofit2.Response

class BottariRemoteDataSourceImpl(
    private val bottariService: BottariService,
) : BottariRemoteDataSource {
    override suspend fun fetchBottaries(ssaid: String): Result<List<FetchBottariesResponse>> =
        safeApiCall {
            bottariService.fetchBottaries(ssaid)
        }

    override suspend fun createBottari(
        ssaid: String,
        createBottariRequest: CreateBottariRequest,
    ): Result<Long> = runCatching {
        val response = bottariService.createBottari(ssaid, createBottariRequest)
        requireNotNull(response.extractBottariId())
    }

    private fun Response<*>.extractBottariId(): Long? {
        val locationHeader = this.headers()[HEADER_LOCATION]
        return locationHeader
            ?.substringAfter(HEADER_BOTTARI_ID_PREFIX)
            ?.takeWhile { it.isDigit() }
            ?.toLongOrNull()
    }

    companion object {
        private const val HEADER_LOCATION = "Location"
        private const val HEADER_BOTTARI_ID_PREFIX = "/bottaries/"
    }
}
