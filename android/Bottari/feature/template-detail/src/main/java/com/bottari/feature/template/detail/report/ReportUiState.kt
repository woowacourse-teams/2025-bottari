package com.bottari.feature.template.detail.report

data class ReportUiState(
    val isLoading: Boolean = false,
    val reason: String = "",
) {
    val isButtonEnabled: Boolean = reason.isNotEmpty()
}
