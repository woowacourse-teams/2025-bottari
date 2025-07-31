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
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import kotlinx.coroutines.launch

class MyBottariTemplateViewModel(
    stateHandle: SavedStateHandle,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<MyBottariTemplateUiState> =
        MutableLiveData(MyBottariTemplateUiState())
    val uiState: LiveData<MyBottariTemplateUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<MyBottariTemplateUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<MyBottariTemplateUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_SSAID_MISSING)

    init {
        fetchMyBottariTemplates()
    }

    fun deleteBottariTemplate(bottariTemplateId: Long) {
        viewModelScope.launch {
            deleteMyBottariTemplateUseCase(ssaid, bottariTemplateId)
                .onSuccess {
                    _uiState.update { copy(bottariTemplates = bottariTemplates.filterNot { it.id == bottariTemplateId }) }
                    _uiEvent.value = MyBottariTemplateUiEvent.DeleteMyTemplateSuccess
                }.onFailure {
                    _uiEvent.value = MyBottariTemplateUiEvent.DeleteMyTemplateFailure
                }
        }
    }

    private fun fetchMyBottariTemplates() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            fetchMyBottariTemplatesUseCase(ssaid)
                .onSuccess { _uiState.update { copy(bottariTemplates = it.map { it.toUiModel() }) } }
                .onFailure { _uiEvent.value = MyBottariTemplateUiEvent.FetchMyTemplateFailure }

            _uiState.update { copy(isLoading = false) }
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
                        UseCaseProvider.deleteMyBottariTemplateUseCase,
                    )
                }
            }
    }
}
