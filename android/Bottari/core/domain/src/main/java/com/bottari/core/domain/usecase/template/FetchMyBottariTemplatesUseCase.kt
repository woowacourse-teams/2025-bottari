package com.bottari.core.domain.usecase.template

import com.bottari.core.domain.model.bottari.template.BottariTemplate
import com.bottari.core.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class FetchMyBottariTemplatesUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(): Result<List<BottariTemplate>> = bottariTemplateRepository.fetchMyBottariTemplates()
}
