package com.bottari.domain.usecase.bookmark

import com.bottari.domain.model.bottari.template.BookmarkTemplate
import com.bottari.domain.repository.BookmarkRepository
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(bookmark: BookmarkTemplate): Result<Unit> = bookmarkRepository.upsertBookmark(bookmark)
}
