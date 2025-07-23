package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class UnCheckBottariItemUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit> = bottariItemRepository.uncheckBottariItem(ssaid, bottariItemId)
}
