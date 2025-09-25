package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class SaveItemsUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        items: List<String>,
    ): Result<Unit> = bottariItemRepository.saveItems(bottariId, items)
}
