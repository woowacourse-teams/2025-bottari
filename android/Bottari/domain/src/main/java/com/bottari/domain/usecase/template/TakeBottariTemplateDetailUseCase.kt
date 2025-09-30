package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariRepository
import com.bottari.domain.repository.BottariTemplateRepository

class TakeBottariTemplateDetailUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(
        templateId: Long,
        title: String,
        itemNames: List<String>,
    ): Result<Long> =
        runCatching {
            val bottariId = bottariRepository.saveBottariWithItems(title, itemNames).getOrThrow()
            bottariTemplateRepository.takeBottariTemplate(bottariId)
            bottariId
        }
}
