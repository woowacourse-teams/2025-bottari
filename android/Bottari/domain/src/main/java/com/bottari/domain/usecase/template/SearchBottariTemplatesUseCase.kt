package com.bottari.domain.usecase.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariTemplateRepository

class SearchBottariTemplatesUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(searchWord: String): BottariResult<List<BottariTemplate>> =
        bottariTemplateRepository.fetchBottariTemplates(searchWord)
}
