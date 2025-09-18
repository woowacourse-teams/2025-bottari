package com.bottari.domain.usecase.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariTemplateRepository

class FetchBottariTemplatesUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(): BottariResult<List<BottariTemplate>> = bottariTemplateRepository.fetchBottariTemplates(null)
}
