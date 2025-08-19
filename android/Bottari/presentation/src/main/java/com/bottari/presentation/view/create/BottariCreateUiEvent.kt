package com.bottari.presentation.view.create

sealed interface BottariCreateUiEvent {
    data class CreatePersonalBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    data class CreateTeamBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    data object CreateBottariFailure : BottariCreateUiEvent
}
