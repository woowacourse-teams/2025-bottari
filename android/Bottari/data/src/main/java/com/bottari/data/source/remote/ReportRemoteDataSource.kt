package com.bottari.data.source.remote

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        ssaid: String,
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit>
}
