package com.bottari.presentation.compose.home.template.bookmark

import androidx.lifecycle.viewModelScope
import com.bottari.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.bottari.domain.usecase.bookmark.ObserveAllBookmarksUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.BookmarkedTemplateUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookmarkTemplateViewModel @Inject constructor(
    private val observeAllBookmarksUseCase: ObserveAllBookmarksUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
) : FlowBaseViewModel<BookmarkTemplateUiState, BookmarkTemplateEvent>(BookmarkTemplateUiState()) {
    init {
        fetchBookmarks()
    }

    fun deleteBookmark(id: Long) {
        updateState { copy(isLoading = true) }

        launch { deleteBookmarkUseCase(id) }
            .invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun fetchBookmarks() {
        updateState { copy(isLoading = true) }

        observeAllBookmarksUseCase()
            .onEach { bookmarkTemplates ->
                val uiModels = bookmarkTemplates.map(BookmarkedTemplateUiModel::fromBookmark)
                updateState { copy(isLoading = false, isFetched = true, templates = uiModels) }
            }.launchIn(viewModelScope)
    }
}
