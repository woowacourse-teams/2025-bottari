package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.report.TemplateReportRequest

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): Result<Unit>
}
