package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class DeleteItemUseCase(
    private val itemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(id: Long): Result<Unit> = itemRepository.deleteItem(id)
}
