package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.ReportRemoteDataSource
import com.bottari.core.domain.repository.ReportRepository
import com.bottari.core.network.dto.report.TemplateReportRequest
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource,
) : ReportRepository {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit> =
        reportRemoteDataSource.reportTemplate(
            bottariTemplateId,
            TemplateReportRequest(reason),
        )
}
