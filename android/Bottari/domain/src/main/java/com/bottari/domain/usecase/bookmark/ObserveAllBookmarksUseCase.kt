package com.bottari.domain.usecase.bookmark

import com.bottari.domain.model.bottari.template.BookmarkTemplate
import com.bottari.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    operator fun invoke(): Flow<List<BookmarkTemplate>> = bookmarkRepository.observeAllBookmarks()
}
