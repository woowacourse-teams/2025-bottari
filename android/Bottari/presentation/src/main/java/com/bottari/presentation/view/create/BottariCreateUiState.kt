package com.bottari.presentation.view.create

import com.bottari.domain.model.bottari.BottariType

data class BottariCreateUiState(
    val bottariType: BottariType,
    val bottariTitle: String = "",
) {
    val isCanCreate: Boolean = bottariTitle.trim().isNotBlank()
}
