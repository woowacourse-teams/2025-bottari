package com.bottari.presentation.compose.home.team

sealed interface MyBottariUiEvent {
    data object PersonalBottariFetchFailure : MyBottariUiEvent

    data object TeamBottariFetchFailure : MyBottariUiEvent

    data class CreatePersonalBottariSuccess(
        val bottariId: Long,
    ) : MyBottariUiEvent

    data class CreateTeamBottariSuccess(
        val bottariId: Long,
    ) : MyBottariUiEvent
}
