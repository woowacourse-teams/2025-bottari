package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.service.BottariService
import com.bottari.data.util.safeApiCall

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
}
