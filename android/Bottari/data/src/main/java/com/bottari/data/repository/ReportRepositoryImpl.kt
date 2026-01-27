package com.bottari.data.repository

import com.bottari.data.model.remote.report.TemplateReportRequest
import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource,
) : ReportRepository {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit> = reportRemoteDataSource.reportTemplate(bottariTemplateId, TemplateReportRequest(reason))
}
