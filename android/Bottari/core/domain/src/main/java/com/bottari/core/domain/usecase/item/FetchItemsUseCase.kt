package com.bottari.core.domain.usecase.item

import com.bottari.core.domain.model.bottari.item.ChecklistItem
import com.bottari.core.domain.repository.BottariItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchItemsUseCase @Inject constructor(
    private val bottariItemRepository: BottariItemRepository,
) {
    operator fun invoke(bottariId: Long): Flow<List<ChecklistItem>> = bottariItemRepository.fetchItems(bottariId)
}
