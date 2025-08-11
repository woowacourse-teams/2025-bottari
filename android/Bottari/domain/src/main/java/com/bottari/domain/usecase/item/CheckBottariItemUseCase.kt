package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class CheckBottariItemUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariItemId: Long): Result<Unit> = bottariItemRepository.checkBottariItem(bottariItemId)
}
