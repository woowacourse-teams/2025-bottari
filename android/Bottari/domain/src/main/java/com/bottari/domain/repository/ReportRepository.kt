package com.bottari.domain.repository

import com.bottari.domain.model.exception.BottariResult

interface ReportRepository {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        reason: String,
    ): BottariResult<Unit>
}
