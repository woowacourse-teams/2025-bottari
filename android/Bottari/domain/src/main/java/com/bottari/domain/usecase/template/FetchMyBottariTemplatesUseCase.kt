package com.bottari.domain.usecase.template

import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository

class FetchMyBottariTemplatesUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(): Result<List<BottariTemplate>> = bottariTemplateRepository.fetchMyBottariTemplates()
}
