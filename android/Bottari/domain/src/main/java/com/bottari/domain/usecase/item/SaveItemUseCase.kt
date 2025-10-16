package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository
import javax.inject.Inject

class SaveItemUseCase @Inject constructor(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        itemName: String,
    ): Result<Unit> = bottariItemRepository.saveItem(bottariId, itemName)
}
