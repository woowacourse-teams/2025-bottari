package com.bottari.domain.usecase.item

import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.repository.BottariItemRepository

class FetchChecklistUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<List<ChecklistItem>> = bottariItemRepository.fetchChecklist(bottariId)
}
