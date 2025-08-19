package com.bottari.presentation.view.join

sealed interface TeamBottariJoinUiEvent {
    data object JoinTeamBottariSuccess : TeamBottariJoinUiEvent

    data object JoinTeamBottariFailure : TeamBottariJoinUiEvent
}
