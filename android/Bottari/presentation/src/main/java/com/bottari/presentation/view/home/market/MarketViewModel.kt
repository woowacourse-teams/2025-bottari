package com.bottari.presentation.view.home.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import com.bottari.presentation.model.BottariTemplateUiModel
import kotlinx.coroutines.launch

class MarketViewModel(
    private val bottariTemplatesUseCase: FetchBottariTemplatesUseCase,
) : ViewModel() {
    private val _bottariTemplates: MutableLiveData<UiState<List<BottariTemplateUiModel>>> =
        MutableLiveData()
    val bottariTemplates: LiveData<UiState<List<BottariTemplateUiModel>>> get() = _bottariTemplates

    init {
        fetchBottariTemplates()
    }

    private fun fetchBottariTemplates() {
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

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MarketViewModel(UseCaseProvider.fetchBottariTemplatesUseCase)
                }
            }
    }
}
