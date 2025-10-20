package com.bottari.presentation.compose.home.template

import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchTemplatesByHashtagUseCase
import com.bottari.domain.usecase.template.SearchTemplatesByTitleUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val searchTemplatesByTitleUseCase: SearchTemplatesByTitleUseCase,
    private val searchTemplatesByHashtagUseCase: SearchTemplatesByHashtagUseCase,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : BaseViewModel<TemplateUiState, TemplateUiEvent>(TemplateUiState()) {
    private val debouncedSearch: (Unit) -> Unit

    private var mainPageable: Pageable<BottariTemplate> = Pageable()
    private var searchPageable: Pageable<BottariTemplate> = Pageable()

    init {
        fetchTemplates()
        fetchMyUploadTemplates()
        debouncedSearch = viewModelScope.debounce(DEBOUNCE_DELAY) { fetchTemplates() }
    }

    fun updateSearchWord(searchWord: String) {
        if (currentState.chips.isNotEmpty()) return

        debouncedSearch(Unit)
        updateState { copy(searchWord = searchWord) }
    }

    fun fetchTemplates() {
        val isSearched = currentState.searchWord.isNotEmpty()
        val currentPageable = if (isSearched) searchPageable else mainPageable

        updateState { copy(isLoading = true) }

        launch {
            searchTemplatesByTitleUseCase(
                query = currentState.searchWord,
                pageable = currentPageable.nextRequest(),
            ).onSuccess { pageable ->
                if (isSearched) {
                    handleSearchTemplatesSuccess(pageable)
                } else {
                    handleFetchTemplatesSuccess(pageable)
                }
            }.onFailure {
                emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure)
            }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    fun searchByChip(chips: List<BottariTemplateHashtagUiModel>) {
        if (chips.isEmpty()) {
            searchPageable = Pageable()
            val uiModels = mainPageable.contents.map { BottariTemplateUiModel.fromDomain(it) }
            updateState { copy(templates = uiModels, chips = emptyList()) }
            return
        }

        if (currentState.chips == chips) return

        updateState { copy(searchWord = "", chips = chips, isLoading = true) }
        launch {
            searchTemplatesByHashtagUseCase(
                hashtagId = chips.first().id,
                pageable = searchPageable.nextRequest(),
            ).onSuccess { pageable ->
                handleSearchTemplatesSuccess(pageable)
            }.onFailure {
                emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure)
            }

            updateState { copy(isLoading = false) }
        }
    }

    fun deleteTemplate(templateId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteMyBottariTemplateUseCase(templateId)
                .onSuccess {
                    updateState { copy(myTemplates = myTemplates.filterNot { it.id == templateId }) }
                    emitEvent(TemplateUiEvent.DeleteBottariTemplateSuccess)
                }.onFailure {
                    emitEvent(TemplateUiEvent.DeleteBottariTemplateFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun handleSearchTemplatesSuccess(pageable: Pageable<BottariTemplate>) {
        searchPageable = searchPageable.merge(pageable)
        val uiModels = searchPageable.contents.map { BottariTemplateUiModel.fromDomain(it) }
        updateState { copy(templates = uiModels) }
        emitEvent(TemplateUiEvent.SearchTemplateSuccess)
    }

    private fun handleFetchTemplatesSuccess(pageable: Pageable<BottariTemplate>) {
        searchPageable = Pageable()
        mainPageable = mainPageable.merge(pageable)
        val uiModels = mainPageable.contents.map { BottariTemplateUiModel.fromDomain(it) }
        updateState { copy(templates = uiModels) }
    }

    private fun fetchMyUploadTemplates() {
        updateState { copy(isLoading = true) }

        launch {
            fetchMyBottariTemplatesUseCase()
                .onSuccess { templates ->
                    val uiModels = templates.map { BottariTemplateUiModel.fromDomain(it) }
                    updateState { copy(myTemplates = uiModels) }
                }.onFailure {
                    emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
