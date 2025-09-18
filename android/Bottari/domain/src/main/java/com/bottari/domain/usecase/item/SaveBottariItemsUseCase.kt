package com.bottari.domain.usecase.item

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariItemRepository

class SaveBottariItemsUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        deleteItemIds: List<Long>,
        createItemNames: List<String>,
    ): BottariResult<Unit> = bottariItemRepository.saveBottariItems(bottariId, deleteItemIds, createItemNames)
}
