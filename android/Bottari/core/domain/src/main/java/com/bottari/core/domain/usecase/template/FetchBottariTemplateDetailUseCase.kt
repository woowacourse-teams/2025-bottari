package com.bottari.core.domain.usecase.template

import com.bottari.core.domain.model.bottari.template.BottariTemplate
import com.bottari.core.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class FetchBottariTemplateDetailUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<BottariTemplate> = bottariTemplateRepository.fetchBottariTemplate(bottariId)
}
