package com.bottari.presentation.view.common.report

sealed interface ReportUiEvent {
    data object ReportTemplateSuccess : ReportUiEvent

    data object ReportTemplateFailure : ReportUiEvent
}
