package com.bottari.data.source.remote

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit>
}
