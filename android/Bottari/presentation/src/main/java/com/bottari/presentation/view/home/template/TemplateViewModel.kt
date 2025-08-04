package com.bottari.presentation.view.home.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.launch

class TemplateViewModel(
    private val fetchBottariTemplatesUseCase: FetchBottariTemplatesUseCase,
    private val searchBottariTemplatesUseCase: SearchBottariTemplatesUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<TemplateUiState> = MutableLiveData(TemplateUiState())
    val uiState: LiveData<TemplateUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<TemplateUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<TemplateUiEvent> get() = _uiEvent

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
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            fetchBottariTemplatesUseCase()
                .onSuccess { templates ->
                    val templateUiModels = templates.map { it.toUiModel() }
                    _uiState.update { copy(templates = templateUiModels) }
                }.onFailure {
                    _uiEvent.value = TemplateUiEvent.FetchBottariTemplatesFailure
                }
            _uiState.update { copy(isLoading = false) }
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
                    _uiState.update { copy(templates = templateUiModels) }
                }.onFailure {
                    _uiEvent.value = TemplateUiEvent.FetchBottariTemplatesFailure
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
