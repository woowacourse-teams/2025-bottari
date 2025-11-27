package com.bottari.core.domain.usecase.bookmark

import com.bottari.core.domain.repository.BookmarkRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(templateId: Long): Result<Unit> = bookmarkRepository.deleteBookmarkByTemplateId(templateId)
}
