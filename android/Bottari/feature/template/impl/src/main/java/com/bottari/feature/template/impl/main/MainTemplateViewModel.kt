package com.bottari.feature.template.impl.main

import androidx.lifecycle.viewModelScope
import com.bottari.common.util.debounce
import com.bottari.core.domain.model.bottari.template.BookmarkTemplate
import com.bottari.core.domain.model.bottari.template.BottariTemplate
import com.bottari.core.domain.model.bottari.template.PopularHashtag
import com.bottari.core.domain.model.common.Pageable
import com.bottari.core.domain.network.NetworkManager
import com.bottari.core.domain.usecase.bookmark.AddBookmarkUseCase
import com.bottari.core.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.bottari.core.domain.usecase.bookmark.ObserveAllBookmarksUseCase
import com.bottari.core.domain.usecase.hashtag.FetchPopularHashtagsUseCase
import com.bottari.core.domain.usecase.template.SearchTemplatesByHashtagUseCase
import com.bottari.core.domain.usecase.template.SearchTemplatesByTitleUseCase
import com.bottari.core.ui.base.NetworkBaseViewModel
import com.bottari.core.ui.model.template.BottariTemplateHashtagUiModel
import com.bottari.core.ui.model.template.BottariTemplateUiModel
import com.bottari.logger.BottariLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainTemplateViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val searchTemplatesByTitleUseCase: SearchTemplatesByTitleUseCase,
    private val searchTemplatesByHashtagUseCase: SearchTemplatesByHashtagUseCase,
    private val fetchPopularHashtagsUseCase: FetchPopularHashtagsUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val observeAllBookmarksUseCase: ObserveAllBookmarksUseCase,
) : NetworkBaseViewModel<MainTemplateUiState, MainTemplateUiEvent>(
        initialState = MainTemplateUiState(),
        networkManager = networkManager,
    ) {
    private var pageable: Pageable<BottariTemplate> = Pageable()
    private val debouncedSearch: (Unit) -> Unit =
        viewModelScope.debounce(DEBOUND_SEARCH_DELAY) { loadNextPageBySearchWord(reset = true) }

    init {
        loadNextPageBySearchWord(reset = true)
        fetchPopularHashtags()
        observeAllBookmarks()
    }

    fun refresh() {
        if (isConnected.value.not()) {
            updateState { copy(isFetched = true) }
            return
        }

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
        if (currentState.chip == chip) return updateChip(null)

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
        if (isConnected.value.not()) {
            updateState { copy(isFetched = true) }
            return
        }

        val wordPageable = if (reset) Pageable() else pageable.nextRequest()

        launch {
            updateState { copy(isLoading = true) }
            searchTemplatesByTitleUseCase(currentState.searchWord, wordPageable)
                .onSuccess { newPageable -> handleLoadNextPageSuccess(reset, newPageable) }
                .onFailure(::handleLoadNextPageFailure)
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    private fun loadNextPageByHashtag(reset: Boolean) {
        val chip = currentState.chip ?: return loadNextPageBySearchWord(reset = true)
        val chipPageable = if (reset) Pageable() else pageable.nextRequest()

        launch {
            updateState { copy(isLoading = true) }
            searchTemplatesByHashtagUseCase(chip.id, chipPageable)
                .onSuccess { newPageable -> handleLoadNextPageSuccess(reset, newPageable) }
                .onFailure(::handleLoadNextPageFailure)
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun handleLoadNextPageSuccess(
        reset: Boolean,
        newPageable: Pageable<BottariTemplate>,
    ) {
        pageable = newPageable
        val newContents = newPageable.contents.map(BottariTemplateUiModel::fromDomain)
        updateState { copy(templates = newContents) }

        if (reset) emitEvent(MainTemplateUiEvent.SearchTemplateSuccess)
    }

    private fun handleLoadNextPageFailure(exception: Throwable) {
        emitEvent(MainTemplateUiEvent.FetchBottariTemplatesFailure)
        BottariLogger.error(exception.message, exception)
    }

    private fun fetchPopularHashtags() {
        launch { fetchPopularHashtagsUseCase().onSuccess(::handleFetchPopularHashtagsSuccess) }
    }

    private fun handleFetchPopularHashtagsSuccess(hashtags: List<PopularHashtag>) {
        hashtags
            .sortedByDescending { hashtag -> hashtag.usageCount }
            .map(BottariTemplateHashtagUiModel::fromDomain)
            .also { uiModels -> updateState { copy(popularHashtags = uiModels) } }
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
        currentState.templates
            .map { template ->
                if (template.id != templateId) return@map template
                template.copy(isMarked = isMarked)
            }.also { updated -> updateState { copy(templates = updated) } }
    }

    private fun observeAllBookmarks() {
        observeAllBookmarksUseCase()
            .catch { exception -> BottariLogger.error(exception.message, exception) }
            .onEach(::handleObserveAllBookmarks)
            .launchIn(viewModelScope)
    }

    private fun handleObserveAllBookmarks(bookmarks: List<BookmarkTemplate>) {
        currentState.templates
            .map { template ->
                val isMarked = bookmarks.any { bookmark -> bookmark.templateId == template.id }
                template.copy(isMarked = isMarked)
            }.also { updated -> updateState { copy(templates = updated) } }
    }

    companion object {
        private const val DEBOUND_SEARCH_DELAY = 300L
    }
}
