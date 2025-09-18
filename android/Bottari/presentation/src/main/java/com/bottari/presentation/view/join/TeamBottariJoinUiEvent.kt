package com.bottari.presentation.view.join

sealed interface TeamBottariJoinUiEvent {
    data object JoinTeamBottariSuccess : TeamBottariJoinUiEvent

    sealed interface JoinTeamBottariFailure : TeamBottariJoinUiEvent {
        data object NotFoundException : JoinTeamBottariFailure

        data object DuplicatedException : JoinTeamBottariFailure
    }

    data object UnexpectedException : TeamBottariJoinUiEvent
}
