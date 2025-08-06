package com.bottari.presentation.view.common.report

data class ReportUiState(
    val isLoading: Boolean = false,
    val reason: String = "",
) {
    val isButtonEnabled: Boolean = reason.isNotEmpty()
}
