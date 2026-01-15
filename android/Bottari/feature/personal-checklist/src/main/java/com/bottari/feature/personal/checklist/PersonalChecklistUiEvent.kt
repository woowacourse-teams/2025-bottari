package com.bottari.feature.personal.checklist

sealed interface PersonalChecklistUiEvent {
    data object FetchChecklistFailure : PersonalChecklistUiEvent

    data object ResetCheckStateFailure : PersonalChecklistUiEvent
}
