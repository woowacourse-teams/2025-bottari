package com.bottari.presentation.view.template.my

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

class MyTemplateViewModel(
    stateHandle: SavedStateHandle,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<MyTemplateUiState> =
        MutableLiveData(MyTemplateUiState())
    val uiState: LiveData<MyTemplateUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<MyTemplateUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<MyTemplateUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_SSAID_MISSING)

    init {
        fetchMyBottariTemplates()
    }

    fun deleteBottariTemplate(bottariTemplateId: Long) {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            deleteMyBottariTemplateUseCase(ssaid, bottariTemplateId)
                .onSuccess {
                    _uiState.update { copy(bottariTemplates = bottariTemplates.filterNot { it.id == bottariTemplateId }) }
                    _uiEvent.value = MyTemplateUiEvent.DeleteMyTemplateSuccess
                }.onFailure {
                    _uiEvent.value = MyTemplateUiEvent.DeleteMyTemplateFailure
                }

            _uiState.update { copy(isLoading = false) }
        }
    }

    private fun fetchMyBottariTemplates() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            fetchMyBottariTemplatesUseCase(ssaid)
                .onSuccess { _uiState.update { copy(bottariTemplates = it.map { it.toUiModel() }) } }
                .onFailure { _uiEvent.value = MyTemplateUiEvent.FetchMyTemplateFailure }

            _uiState.update { copy(isLoading = false) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val ERROR_SSAID_MISSING = "[ERROR] SSAID를 확인할 수 없습니다"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    MyTemplateViewModel(
                        stateHandle,
                        UseCaseProvider.fetchMyBottariTemplatesUseCase,
                        UseCaseProvider.deleteMyBottariTemplateUseCase,
                    )
                }
            }
    }
}
