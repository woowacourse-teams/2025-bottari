package com.bottari.presentation.compose.home.team

sealed interface MyBottariUiEvent {
    data object FetchPersonalBottariFailure : MyBottariUiEvent

    data object FetchTeamBottariFailure : MyBottariUiEvent

    data object JoinTeamBottariFailure : MyBottariUiEvent

    data object ExitTeamBottariFailure : MyBottariUiEvent

    data object DeletePersonalBottariFailure : MyBottariUiEvent

    data object ExitTeamBottariSuccess : MyBottariUiEvent

    data object DeletePersonalBottariSuccess : MyBottariUiEvent

    data class CreatePersonalBottariSuccess(
        val bottariId: Long,
    ) : MyBottariUiEvent

    data class CreateTeamBottariSuccess(
        val bottariId: Long,
    ) : MyBottariUiEvent

    data object CreatePersonalBottariFailure : MyBottariUiEvent

    data object CreateTeamBottariFailure : MyBottariUiEvent
}
