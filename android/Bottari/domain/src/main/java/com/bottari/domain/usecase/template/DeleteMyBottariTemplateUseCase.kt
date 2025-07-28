package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariTemplateRepository

class DeleteMyBottariTemplateUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariTemplateId: Long,
    ): Result<Unit> = bottariTemplateRepository.deleteMyBottariTemplate(ssaid, bottariTemplateId)
}
