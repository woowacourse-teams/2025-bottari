package com.bottari.core.domain.usecase.bookmark

import com.bottari.core.domain.model.bottari.template.BookmarkTemplate
import com.bottari.core.domain.repository.BookmarkRepository
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(bookmark: BookmarkTemplate): Result<Unit> = bookmarkRepository.upsertBookmark(bookmark)
}
