package com.bottari.core.domain.usecase.template

import com.bottari.core.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class CreateBottariTemplateUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        items: List<String>,
        hashtag: List<String>,
    ): Result<Long> = bottariTemplateRepository.createBottariTemplate(title, description, items, hashtag)
}
