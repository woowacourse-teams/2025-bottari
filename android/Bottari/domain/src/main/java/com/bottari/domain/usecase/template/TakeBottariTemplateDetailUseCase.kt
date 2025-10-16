package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariRepository
import com.bottari.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class TakeBottariTemplateDetailUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(
        templateId: Long,
        title: String,
        itemNames: List<String>,
    ): Result<Long> =
        runCatching {
            val bottariId = bottariRepository.createBottariWithItems(title, itemNames).getOrThrow()
            bottariTemplateRepository.takeBottariTemplate(bottariId)
            bottariId
        }
}
