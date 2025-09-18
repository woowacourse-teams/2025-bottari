package com.bottari.domain.usecase.item

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariItemRepository

class CheckBottariItemUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariItemId: Long): BottariResult<Unit> = bottariItemRepository.checkBottariItem(bottariItemId)
}
