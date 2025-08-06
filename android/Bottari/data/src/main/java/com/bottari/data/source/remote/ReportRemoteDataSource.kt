package com.bottari.data.source.remote

import retrofit2.Response

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        ssaid: String,
        bottariTemplateId: Long,
    ): Result<Unit>
}
