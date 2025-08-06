package com.bottari.presentation.view.common.report

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
import com.bottari.domain.usecase.report.ReportTemplateUseCase
import com.bottari.presentation.common.extension.update
import kotlinx.coroutines.launch

class ReportViewModel(
    stateHandle: SavedStateHandle,
    private val reportTemplateUseCase: ReportTemplateUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<ReportUiState> = MutableLiveData(ReportUiState())
    val uiState: LiveData<ReportUiState> = _uiState

    private val _uiEvent: MutableLiveData<ReportUiEvent> = MutableLiveData()
    val uiEvent: LiveData<ReportUiEvent> = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID]!!
    private val templateId: Long = stateHandle[KEY_TEMPLATE_ID]!!

    fun updateSelectedReason(reason: String) {
        _uiState.update { copy(reason = reason) }
    }

    fun reportTemplate() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            reportTemplateUseCase(ssaid, templateId, _uiState.value!!.reason)
                .onSuccess { _uiEvent.value = ReportUiEvent.ReportTemplateSuccess }
                .onFailure { _uiEvent.value = ReportUiEvent.ReportTemplateFailure }

            _uiState.update { copy(isLoading = false) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_TEMPLATE_ID = "KEY_TEMPLATE_ID"

        fun Factory(
            ssaid: String,
            templateId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
                    stateHandle[KEY_TEMPLATE_ID] = templateId
                    ReportViewModel(stateHandle, UseCaseProvider.reportTemplateUseCase)
                }
            }
    }
}
