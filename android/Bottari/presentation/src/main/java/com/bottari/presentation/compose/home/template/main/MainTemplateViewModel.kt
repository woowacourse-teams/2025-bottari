package com.bottari.presentation.compose.home.template.main

import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.usecase.bookmark.AddBookmarkUseCase
import com.bottari.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.bottari.domain.usecase.bookmark.ObserveAllBookmarksUseCase
import com.bottari.domain.usecase.template.SearchTemplatesByHashtagUseCase
import com.bottari.domain.usecase.template.SearchTemplatesByTitleUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainTemplateViewModel @Inject constructor(
    private val searchTemplatesByTitleUseCase: SearchTemplatesByTitleUseCase,
    private val searchTemplatesByHashtagUseCase: SearchTemplatesByHashtagUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val observeAllBookmarksUseCase: ObserveAllBookmarksUseCase,
) : FlowBaseViewModel<MainTemplateUiState, MainTemplateUiEvent>(MainTemplateUiState()) {
    private var pageable: Pageable<BottariTemplate> = Pageable()
    private val debouncedSearch: (Unit) -> Unit =
        viewModelScope.debounce(DEBOUND_SEARCH_DELAY) { loadNextPageBySearchWord(reset = true) }

    init {
        loadNextPageBySearchWord(reset = true)
        observeAllBookmarks()
    }

    fun refresh() {
        pageable = Pageable()

        if (currentState.chip != null) {
            loadNextPageByHashtag(reset = true)
            return
        }

        loadNextPageBySearchWord(reset = true)
    }

    fun updateSearchWord(searchWord: String) {
        if (currentState.chip != null) return
        if (currentState.searchWord == searchWord) return

        updateState { copy(searchWord = searchWord) }
        debouncedSearch(Unit)
    }

    fun updateChip(chip: BottariTemplateHashtagUiModel?) {
        if (currentState.searchWord.isNotEmpty()) return
        if (currentState.chip == chip) return

        updateState { copy(chip = chip, searchWord = "") }
        loadNextPageByHashtag(reset = true)
    }

    fun toggleBookmark(templateId: Long) {
        val found = currentState.templates.find { template -> template.id == templateId } ?: return
        if (found.isMarked) deleteBookmark(templateId) else addBookmark(templateId)
    }

    fun loadNextPage() {
        if (currentState.chip != null) {
            loadNextPageByHashtag(reset = false)
            return
        }

        loadNextPageBySearchWord(reset = false)
    }

    private fun loadNextPageBySearchWord(reset: Boolean) {
        val wordPageable = if (reset) Pageable() else pageable.nextRequest()

        launch {
            updateState { copy(isLoading = true) }

            searchTemplatesByTitleUseCase(currentState.searchWord, wordPageable)
                .onSuccess { newPageable ->
                    pageable = newPageable
                    updateState {
                        copy(templates = newPageable.contents.map(BottariTemplateUiModel::fromDomain))
                    }
                    if (reset) emitEvent(MainTemplateUiEvent.SearchTemplateSuccess)
                }.onFailure { exception ->
                    emitEvent(MainTemplateUiEvent.FetchBottariTemplatesFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    private fun loadNextPageByHashtag(reset: Boolean) {
        val chip = currentState.chip ?: return loadNextPageBySearchWord(reset = true)
        val chipPageable = if (reset) Pageable() else pageable.nextRequest()

        launch {
            updateState { copy(isLoading = true) }

            searchTemplatesByHashtagUseCase(chip.id, chipPageable)
                .onSuccess { newPageable ->
                    pageable = newPageable
                    updateState {
                        copy(templates = newPageable.contents.map(BottariTemplateUiModel::fromDomain))
                    }
                    if (reset) emitEvent(MainTemplateUiEvent.SearchTemplateSuccess)
                }.onFailure { exception ->
                    emitEvent(MainTemplateUiEvent.FetchBottariTemplatesFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun addBookmark(templateId: Long) {
        val currentTemplates = currentState.templates
        val targetTemplate =
            currentTemplates.find { template -> template.id == templateId }?.toDomain() ?: return

        launch {
            updateState { copy(isLoading = true) }

            addBookmarkUseCase(targetTemplate)
                .onSuccess { changeMarkedById(templateId, true) }
                .onFailure { exception ->
                    emitEvent(MainTemplateUiEvent.AddBookmarkFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun deleteBookmark(templateId: Long) {
        launch {
            updateState { copy(isLoading = true) }

            deleteBookmarkUseCase(templateId)
                .onSuccess { changeMarkedById(templateId, false) }
                .onFailure { exception ->
                    emitEvent(MainTemplateUiEvent.DeleteBookmarkFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun changeMarkedById(
        templateId: Long,
        isMarked: Boolean,
    ) {
        val updated =
            currentState.templates.map { template ->
                if (template.id != templateId) return@map template
                template.copy(isMarked = isMarked)
            }
        updateState { copy(templates = updated) }
    }

    private fun observeAllBookmarks() {
        observeAllBookmarksUseCase()
            .onEach { bookmarks ->
                val updated =
                    currentState.templates.map { template ->
                        val isMarked = bookmarks.any { bookmark -> bookmark.templateId == template.id }
                        template.copy(isMarked = isMarked)
                    }
                updateState { copy(templates = updated) }
            }.launchIn(viewModelScope)
    }

    companion object {
        private const val DEBOUND_SEARCH_DELAY = 300L
    }
}
