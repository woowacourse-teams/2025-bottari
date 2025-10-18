package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class CreateBottariTemplateUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(
        title: String,
        items: List<String>,
    ): Result<Long?> = bottariTemplateRepository.createBottariTemplate(title, items)
}
