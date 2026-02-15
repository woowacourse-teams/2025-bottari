package com.bottari.feature.template.detail.report

import androidx.compose.runtime.Stable
import com.bottari.core.domain.usecase.report.ReportTemplateUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@Stable
@HiltViewModel(assistedFactory = ReportViewModel.Factory::class)
class ReportViewModel @AssistedInject constructor(
    @Assisted private val templateId: Long,
    private val reportTemplateUseCase: ReportTemplateUseCase,
) : BaseViewModel<ReportUiState, ReportUiEvent>(ReportUiState()) {
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

    @AssistedFactory
    interface Factory {
        fun create(templateId: Long): ReportViewModel
    }
}
