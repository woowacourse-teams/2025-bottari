package com.bottari.presentation.view.home.market.main

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
import com.bottari.presentation.base.UiState
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.launch

class MarketViewModel(
    private val bottariTemplatesUseCase: FetchBottariTemplatesUseCase,
    private val searchBottariTemplatesUseCase: SearchBottariTemplatesUseCase,
) : ViewModel() {
    private val _bottariTemplates: MutableLiveData<UiState<List<BottariTemplateUiModel>>> =
        MutableLiveData(UiState.Loading)
    val bottariTemplates: LiveData<UiState<List<BottariTemplateUiModel>>> get() = _bottariTemplates

    private val debouncedSearch: (String) -> Unit =
        debounce(
            timeMillis = TIME_MILLIS,
            coroutineScope = viewModelScope,
        ) { searchWord -> performSearch(searchWord) }

    init {
        fetchBottariTemplates()
    }

    fun searchTemplates(searchWord: String) {
        debouncedSearch(searchWord)
    }

    private fun fetchBottariTemplates() {
        _bottariTemplates.value = UiState.Loading
        viewModelScope.launch {
            bottariTemplatesUseCase()
                .onSuccess { templates ->
                    _bottariTemplates.value =
                        UiState.Success(templates.map { template -> template.toUiModel() })
                }.onFailure { error ->
                    _bottariTemplates.value = UiState.Failure(error.message)
                }
        }
    }

    private fun performSearch(searchWord: String) {
        if (searchWord.isEmpty()) {
            fetchBottariTemplates()
            return
        }
        _bottariTemplates.value = UiState.Loading
        viewModelScope.launch {
            searchBottariTemplatesUseCase(searchWord)
                .onSuccess { templates ->
                    _bottariTemplates.value =
                        UiState.Success(templates.map { template -> template.toUiModel() })
                }.onFailure { error ->
                    _bottariTemplates.value = UiState.Failure(error.message)
                }
        }
    }

    companion object {
        private const val TIME_MILLIS = 500L

        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MarketViewModel(
                        UseCaseProvider.fetchBottariTemplatesUseCase,
                        UseCaseProvider.searchBottariTemplatesUseCase,
                    )
                }
            }
    }
}
