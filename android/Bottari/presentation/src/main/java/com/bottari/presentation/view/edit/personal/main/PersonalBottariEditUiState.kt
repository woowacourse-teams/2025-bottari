package com.bottari.presentation.view.edit.personal.main

import com.bottari.presentation.model.BottariDetailUiModel

data class PersonalBottariEditUiState(
    val isLoading: Boolean = false,
    val bottariId : Long = -1L,
    val bottari: BottariDetailUiModel? = null,
)
