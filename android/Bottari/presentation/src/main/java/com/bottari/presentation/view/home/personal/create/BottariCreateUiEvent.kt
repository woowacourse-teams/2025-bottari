package com.bottari.presentation.view.home.personal.create

sealed interface BottariCreateUiEvent {
    data class CreateBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    data object CreateBottariFailure : BottariCreateUiEvent
}
