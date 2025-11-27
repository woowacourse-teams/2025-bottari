package com.bottari.core.domain.usecase.item

import com.bottari.core.domain.repository.BottariItemRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val itemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(id: Long): Result<Unit> = itemRepository.deleteItem(id)
}
