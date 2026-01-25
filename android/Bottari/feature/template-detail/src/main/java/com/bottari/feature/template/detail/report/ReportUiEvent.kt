package com.bottari.feature.template.detail.report

sealed interface ReportUiEvent {
    data object ReportTemplateSuccess : ReportUiEvent

    data object ReportTemplateFailure : ReportUiEvent
}
