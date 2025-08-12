package com.bottari.presentation.view.create

sealed interface BottariCreateUiEvent {
    data class CreateBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    data object CreateBottariFailure : BottariCreateUiEvent
}
