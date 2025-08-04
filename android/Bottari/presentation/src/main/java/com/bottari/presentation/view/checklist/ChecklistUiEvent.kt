package com.bottari.presentation.view.checklist

sealed interface ChecklistUiEvent {
    data object FetchChecklistFailure : ChecklistUiEvent

    data object CheckItemFailure : ChecklistUiEvent

    data object AllSwipedAllChecked : ChecklistUiEvent

    data object AllSwipedNotAllChecked : ChecklistUiEvent
}
