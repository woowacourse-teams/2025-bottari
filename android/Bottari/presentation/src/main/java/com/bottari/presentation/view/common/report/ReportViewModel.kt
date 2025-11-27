package com.bottari.presentation.view.common.report

import androidx.lifecycle.SavedStateHandle
import com.bottari.core.domain.usecase.report.ReportTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val reportTemplateUseCase: ReportTemplateUseCase,
) : BaseViewModel<ReportUiState, ReportUiEvent>(ReportUiState()) {
    private val templateId: Long = stateHandle[KEY_TEMPLATE_ID] ?: error(ERROR_TEMPLATE_ID_EMPTY)

    fun updateSelectedReason(reason: String) {
        updateState { copy(reason = reason) }
    }

    fun reportTemplate() {
        updateState { copy(isLoading = true) }

        launch {
            reportTemplateUseCase(templateId, currentState.reason)
                .onSuccess {
                    emitEvent(ReportUiEvent.ReportTemplateSuccess)
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_REPORT,
                        mapOf("template_id" to templateId, "reason" to currentState.reason),
                    )
                }.onFailure { emitEvent(ReportUiEvent.ReportTemplateFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        const val KEY_TEMPLATE_ID = "KEY_TEMPLATE_ID"
        private const val ERROR_TEMPLATE_ID_EMPTY = "[ERROR] TemplateId를 확인할 수 없습니다"
    }
}
