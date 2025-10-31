package com.bottari.presentation.compose.edit.team.main

sealed interface ComposeTeamBottariEditUiEvent {
    data object FetchComposeTeamBottariDetailFailure : ComposeTeamBottariEditUiEvent

    data object ToggleAlarmStateFailure : ComposeTeamBottariEditUiEvent
}
