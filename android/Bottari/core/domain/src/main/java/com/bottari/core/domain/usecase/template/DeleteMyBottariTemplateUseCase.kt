package com.bottari.core.domain.usecase.template

import com.bottari.core.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class DeleteMyBottariTemplateUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(bottariTemplateId: Long): Result<Unit> =
        bottariTemplateRepository.deleteMyBottariTemplate(bottariTemplateId)
}
