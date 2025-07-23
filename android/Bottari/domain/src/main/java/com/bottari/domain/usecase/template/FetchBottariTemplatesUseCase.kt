package com.bottari.domain.usecase.template

import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository

class FetchBottariTemplatesUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(): Result<List<BottariTemplate>> = bottariTemplateRepository.fetchBottariTemplates()
}
