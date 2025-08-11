package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariTemplateRepository

class DeleteMyBottariTemplateUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(bottariTemplateId: Long): Result<Unit> =
        bottariTemplateRepository.deleteMyBottariTemplate(bottariTemplateId)
}
