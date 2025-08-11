package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariTemplateRepository

class TakeBottariTemplateDetailUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<Long?> = bottariTemplateRepository.takeBottariTemplate(bottariId)
}
