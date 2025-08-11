package com.bottari.data.repository

import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.domain.repository.ReportRepository

class ReportRepositoryImpl(
    private val reportRemoteDataSource: ReportRemoteDataSource,
) : ReportRepository {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit> = reportRemoteDataSource.reportTemplate(bottariTemplateId, reason)
}
