package com.bottari.feature.team.checklist.checklist

sealed interface ComposeTeamChecklistUiEvent {
    data object FetchChecklistFailure : ComposeTeamChecklistUiEvent

    data object CheckItemFailure : ComposeTeamChecklistUiEvent
}
