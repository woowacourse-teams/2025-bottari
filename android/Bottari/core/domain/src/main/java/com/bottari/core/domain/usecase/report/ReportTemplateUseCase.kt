package com.bottari.core.domain.usecase.report

import com.bottari.core.domain.repository.ReportRepository
import javax.inject.Inject

class ReportTemplateUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit> = reportRepository.reportTemplate(bottariTemplateId, reason)
}
