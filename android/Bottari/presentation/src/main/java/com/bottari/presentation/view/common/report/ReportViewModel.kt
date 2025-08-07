package com.bottari.presentation.view.common.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.report.ReportTemplateUseCase
import com.bottari.presentation.common.base.BaseViewModel
import kotlinx.coroutines.launch

class ReportViewModel(
    stateHandle: SavedStateHandle,
    private val reportTemplateUseCase: ReportTemplateUseCase,
) : BaseViewModel<ReportUiState, ReportUiEvent>(ReportUiState()) {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_SSAID_EMPTY)
    private val templateId: Long = stateHandle[KEY_TEMPLATE_ID] ?: error(ERROR_TEMPLATE_ID_EMPTY)

    fun updateSelectedReason(reason: String) {
        updateState { copy(reason = reason) }
    }

    fun reportTemplate() {
        updateState { copy(isLoading = true) }

        launch {
            reportTemplateUseCase(ssaid, templateId, currentState.reason)
                .onSuccess { emitEvent(ReportUiEvent.ReportTemplateSuccess) }
                .onFailure { emitEvent(ReportUiEvent.ReportTemplateFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_TEMPLATE_ID = "KEY_TEMPLATE_ID"
        private const val ERROR_SSAID_EMPTY = "[ERROR] SSAID를 확인할 수 없습니다"
        private const val ERROR_TEMPLATE_ID_EMPTY = "[ERROR] TemplateId를 확인할 수 없습니다"

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
