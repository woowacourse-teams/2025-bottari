package com.bottari.domain.usecase.template

import com.bottari.domain.repository.BottariItemRepository
import com.bottari.domain.repository.BottariRepository
import com.bottari.domain.repository.BottariTemplateRepository

class TakeBottariTemplateDetailUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
    private val bottariRepository: BottariRepository,
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        templateId: Long,
        title: String,
        items: List<String>,
    ): Result<Long> {
        var bottariId: Long? = null

        return runCatching {
            val newBottariId = bottariRepository.saveBottari(title).getOrThrow()
            bottariId = newBottariId

            bottariItemRepository.saveItems(newBottariId, items).getOrThrow()
            newBottariId
        }.onSuccess { _ ->
            notifyTemplateTaken(templateId).getOrNull() ?: return@onSuccess
        }.onFailure {
            bottariId?.let { bottariRepository.deleteBottari(it) }
        }
    }

    private suspend fun notifyTemplateTaken(templateId: Long): Result<Long?> = bottariTemplateRepository.takeBottariTemplate(templateId)
}
