package com.bottari.presentation.compose.team.checklist

sealed interface ComposeTeamChecklistUiEvent {
    data object FetchChecklistFailure : ComposeTeamChecklistUiEvent

    data object CheckItemFailure : ComposeTeamChecklistUiEvent
}
