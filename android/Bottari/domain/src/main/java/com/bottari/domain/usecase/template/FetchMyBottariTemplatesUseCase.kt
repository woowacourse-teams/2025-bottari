package com.bottari.domain.usecase.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class FetchMyBottariTemplatesUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(): Result<List<BottariTemplate>> = bottariTemplateRepository.fetchMyBottariTemplates()
}
