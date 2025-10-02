package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class SaveItemUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        itemName: String,
    ): Result<Unit> = bottariItemRepository.saveItem(bottariId, itemName)
}
