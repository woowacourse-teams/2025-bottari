package com.bottari.data.source.remote

import com.bottari.data.model.remote.report.TemplateReportRequest
import com.bottari.domain.model.exception.BottariResult

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): BottariResult<Unit>
}
