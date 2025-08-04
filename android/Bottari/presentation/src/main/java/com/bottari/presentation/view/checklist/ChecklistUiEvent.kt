package com.bottari.presentation.view.checklist

sealed interface ChecklistUiEvent {
    data object FetchChecklistFailure : ChecklistUiEvent

    data object CheckItemFailure : ChecklistUiEvent
}
