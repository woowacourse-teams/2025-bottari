package com.bottari.core.domain.usecase.bookmark

import com.bottari.core.domain.model.bottari.template.BookmarkTemplate
import com.bottari.core.domain.repository.BookmarkRepository
import javax.inject.Inject

class FindBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(templateId: Long): Result<BookmarkTemplate?> = bookmarkRepository.getBookmarkByTemplateId(templateId)
}
