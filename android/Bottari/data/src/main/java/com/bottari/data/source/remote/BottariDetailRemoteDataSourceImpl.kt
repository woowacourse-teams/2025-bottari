package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.service.BottariDetailService
import com.bottari.data.util.safeApiCall

class BottariDetailRemoteDataSourceImpl(
    val bottariDetailService: BottariDetailService,
) : BottariDetailRemoteDataSource {
    override suspend fun findBottari(id: Long, ssaid: String): Result<BottariResponse> =
        safeApiCall {
            bottariDetailService.findBottari(id, ssaid)
        }
}
