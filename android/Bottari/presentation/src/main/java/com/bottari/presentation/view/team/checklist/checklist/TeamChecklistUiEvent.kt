package com.bottari.presentation.view.team.checklist.checklist

sealed interface TeamChecklistUiEvent {
    data object FetchChecklistFailure : TeamChecklistUiEvent

    data object CheckItemFailure : TeamChecklistUiEvent
}
