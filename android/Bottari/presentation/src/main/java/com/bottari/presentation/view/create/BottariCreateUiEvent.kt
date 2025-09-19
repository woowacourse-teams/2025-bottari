package com.bottari.presentation.view.create

sealed interface BottariCreateUiEvent {
    data class CreatePersonalBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    data class CreateTeamBottariSuccess(
        val bottariId: Long?,
    ) : BottariCreateUiEvent

    sealed interface CreateBottariFailure : BottariCreateUiEvent {
        data object InvalidException : CreateBottariFailure

        data object NotFoundException : CreateBottariFailure

        data object UnexpectedException : CreateBottariFailure
    }
}
