package com.bottari.presentation.view.checklist.personal

sealed interface ChecklistUiEvent {
    data object FetchChecklistFailure : ChecklistUiEvent

    data object CheckItemFailure : ChecklistUiEvent
}
