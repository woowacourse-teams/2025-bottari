package com.bottari.presentation.base

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : UiState<T>()

    data class Failure(
        val message: String?,
    ) : UiState<Nothing>()
}
