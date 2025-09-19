package com.bottari.presentation.view.home.template

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import com.bottari.presentation.util.debounce

class TemplateViewModel(
    private val fetchBottariTemplatesUseCase: FetchBottariTemplatesUseCase,
    private val searchBottariTemplatesUseCase: SearchBottariTemplatesUseCase,
) : BaseViewModel<TemplateUiState, TemplateUiEvent>(TemplateUiState()) {
    private val debouncedSearch: (String) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { searchWord -> performSearch(searchWord) }

    init {
        fetchBottariTemplates()
    }

    fun searchTemplates(searchWord: String) {
        debouncedSearch(searchWord)
    }

    private fun fetchBottariTemplates() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariTemplatesUseCase()
                .onSuccess { templates ->
                    val templateUiModels = templates.map { BottariTemplateUiModel.fromDomain(it) }
                    updateState { copy(templates = templateUiModels) }
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.InvalidException -> emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure.InvalidException)
                        else -> emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure.UnexpectedException)
                }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    private fun performSearch(searchWord: String) {
        if (searchWord.isEmpty()) {
            fetchBottariTemplates()
            return
        }
        launch {
            searchBottariTemplatesUseCase(searchWord)
                .onSuccess { templates ->
                    logPerformSearch(searchWord, templates.size)
                    val templateUiModels = templates.map { BottariTemplateUiModel.fromDomain(it) }
                    updateState { copy(templates = templateUiModels) }
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.InvalidException -> emitEvent(TemplateUiEvent.SearchBottariTemplatesFailure.InvalidException)
                        else -> emitEvent(TemplateUiEvent.SearchBottariTemplatesFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(TemplateUiEvent.SearchBottariTemplatesFailure.UnexpectedException)
                }
        }
    }

    private fun logPerformSearch(
        searchWord: String,
        resultSize: Int,
    ) {
        BottariLogger.ui(
            UiEventType.TEMPLATE_SEARCH,
            mapOf("query" to searchWord, "result_count" to resultSize),
        )
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L

        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TemplateViewModel(
                        BottariTemplateUseCaseProvider.fetchBottariTemplatesUseCase,
                        BottariTemplateUseCaseProvider.searchBottariTemplatesUseCase,
                    )
                }
            }
    }
}
