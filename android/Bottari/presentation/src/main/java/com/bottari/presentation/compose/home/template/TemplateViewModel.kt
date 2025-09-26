package com.bottari.presentation.compose.home.template

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import com.bottari.presentation.util.debounce

class TemplateViewModel(
    private val fetchBottariTemplatesUseCase: FetchBottariTemplatesUseCase,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
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
        debouncedSearch(Unit)
        updateState { copy(searchWord = searchWord) }
    }

    fun fetchTemplates() {
        val isSearched = currentState.searchWord.isNotEmpty()
        val currentPageable = if (isSearched) searchPageable else mainPageable

        updateState { copy(isLoading = true) }

        launch {
            fetchBottariTemplatesUseCase(
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

        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TemplateViewModel(
                        BottariTemplateUseCaseProvider.fetchBottariTemplatesUseCase,
                        BottariTemplateUseCaseProvider.fetchMyBottariTemplatesUseCase,
                    )
                }
            }
    }
}
