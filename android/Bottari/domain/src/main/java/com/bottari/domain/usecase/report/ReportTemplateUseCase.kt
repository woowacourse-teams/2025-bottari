package com.bottari.domain.usecase.report

import com.bottari.domain.repository.ReportRepository

class ReportTemplateUseCase(
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit> = reportRepository.reportTemplate(ssaid, bottariTemplateId, reason)
}
