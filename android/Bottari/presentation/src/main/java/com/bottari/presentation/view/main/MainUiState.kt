package com.bottari.presentation.view.main

data class MainUiState(
    val isLoading: Boolean = false,
    val hasPermissionFlag: Boolean = false,
    val isReady: Boolean = false,
)
