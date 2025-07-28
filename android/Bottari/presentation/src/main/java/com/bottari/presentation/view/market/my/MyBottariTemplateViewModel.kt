package com.bottari.presentation.view.market.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import com.bottari.presentation.model.BottariTemplateUiModel
import kotlinx.coroutines.launch

class MyBottariTemplateViewModel(
    stateHandle: SavedStateHandle,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
) : ViewModel() {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_SSAID_MISSING)

    private val _myBottariTemplates: MutableLiveData<UiState<List<BottariTemplateUiModel>>> =
        MutableLiveData(UiState.Loading)
    val myBottariTemplates: LiveData<UiState<List<BottariTemplateUiModel>>> get() = _myBottariTemplates

    init {
        fetchMyBottariTemplates()
    }

    fun deleteBottariTemplate(bottariTemplateId: Long) {
        viewModelScope.launch { }
    }

    private fun fetchMyBottariTemplates() {
        _myBottariTemplates.value = UiState.Loading

        viewModelScope.launch {
            fetchMyBottariTemplatesUseCase(ssaid)
                .onSuccess { templates ->
                    _myBottariTemplates.value = UiState.Success(templates.map { it.toUiModel() })
                }.onFailure { error ->
                    _myBottariTemplates.value = UiState.Failure(error.message)
                }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val ERROR_SSAID_MISSING = "SSAID를 확인할 수 없습니다"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    MyBottariTemplateViewModel(
                        stateHandle,
                        UseCaseProvider.fetchMyBottariTemplatesUseCase,
                    )
                }
            }
    }
}
