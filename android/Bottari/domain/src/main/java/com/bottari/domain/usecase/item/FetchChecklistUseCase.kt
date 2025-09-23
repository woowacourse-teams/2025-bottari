package com.bottari.domain.usecase.item

import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.repository.BottariItemRepository
import kotlinx.coroutines.flow.Flow

class FetchChecklistUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    operator fun invoke(bottariId: Long): Flow<List<ChecklistItem>> = bottariItemRepository.fetchItems(bottariId)
}
