package com.bottari.domain.usecase.template

import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository

class FetchBottariTemplateDetailUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<BottariTemplate> = bottariTemplateRepository.fetchBottariTemplate(bottariId)
}
