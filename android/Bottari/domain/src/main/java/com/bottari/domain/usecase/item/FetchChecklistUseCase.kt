package com.bottari.domain.usecase.item

import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariItemRepository

class FetchChecklistUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariId: Long): BottariResult<List<ChecklistItem>> = bottariItemRepository.fetchChecklist(bottariId)
}
