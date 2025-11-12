package com.bottari.presentation.compose.personal

sealed interface PersonalChecklistUiEvent {
    data object FetchChecklistFailure : PersonalChecklistUiEvent

    data object ResetCheckStateFailure : PersonalChecklistUiEvent
}
