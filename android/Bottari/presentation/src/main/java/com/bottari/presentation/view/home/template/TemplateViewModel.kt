package com.bottari.presentation.view.home.template

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.launch

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
                    val templateUiModels = templates.map { it.toUiModel() }
                    updateState { copy(templates = templateUiModels) }
                }.onFailure {
                    emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    private fun performSearch(searchWord: String) {
        if (searchWord.isEmpty()) {
            fetchBottariTemplates()
            return
        }
        viewModelScope.launch {
            searchBottariTemplatesUseCase(searchWord)
                .onSuccess { templates ->
                    val templateUiModels = templates.map { it.toUiModel() }
                    updateState { copy(templates = templateUiModels) }
                }.onFailure {
                    emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure)
                }
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L

        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TemplateViewModel(
                        UseCaseProvider.fetchBottariTemplatesUseCase,
                        UseCaseProvider.searchBottariTemplatesUseCase,
                    )
                }
            }
    }
}
