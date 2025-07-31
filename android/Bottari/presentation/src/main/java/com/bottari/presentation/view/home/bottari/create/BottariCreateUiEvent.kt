package com.bottari.presentation.view.home.bottari.create

sealed interface BottariCreateUiEvent {
    data class CreateBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    data object CreateBottariFailure : BottariCreateUiEvent
}
