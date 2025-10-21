package com.bottari.presentation.compose.team.item

sealed interface ComposeTeamBottariItemStatusUiEvent {
    data object FetchTeamBottariItemStatusFailure : ComposeTeamBottariItemStatusUiEvent

    data object SendRemindSuccess : ComposeTeamBottariItemStatusUiEvent

    data object SendRemindFailure : ComposeTeamBottariItemStatusUiEvent
}
