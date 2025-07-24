package com.bottari.domain.usecase.template

import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository

class SearchBottariTemplatesUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(searchWord: String): Result<List<BottariTemplate>> =
        bottariTemplateRepository.fetchBottariTemplates(searchWord)
}
