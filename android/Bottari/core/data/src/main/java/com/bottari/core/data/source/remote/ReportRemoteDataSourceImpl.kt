package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.report.TemplateReportRequest
import com.bottari.core.network.service.ReportService
import javax.inject.Inject

class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportService: ReportService,
) : ReportRemoteDataSource {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): Result<Unit> = safeApiCall { reportService.reportTemplate(bottariTemplateId, request) }
}
