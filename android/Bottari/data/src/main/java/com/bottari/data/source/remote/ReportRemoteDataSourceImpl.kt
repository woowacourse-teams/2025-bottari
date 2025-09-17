package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.report.TemplateReportRequest
import com.bottari.data.service.report.ReportService

class ReportRemoteDataSourceImpl(
    private val reportService: ReportService,
) : ReportRemoteDataSource {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): Result<Unit> = safeApiCall { reportService.reportTemplate(bottariTemplateId, request) }
}
