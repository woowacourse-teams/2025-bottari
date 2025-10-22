package com.bottari.domain.usecase.bookmark

import com.bottari.domain.model.bottari.template.BookmarkTemplate
import com.bottari.domain.repository.BookmarkRepository
import javax.inject.Inject

class FindBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(templateId: Long): Result<BookmarkTemplate?> = bookmarkRepository.getBookmarkByTemplateId(templateId)
}
