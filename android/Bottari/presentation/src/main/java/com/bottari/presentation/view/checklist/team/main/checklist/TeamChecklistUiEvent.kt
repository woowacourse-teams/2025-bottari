package com.bottari.presentation.view.checklist.team.main.checklist

sealed interface TeamChecklistUiEvent {
    data object FetchChecklistFailure : TeamChecklistUiEvent

    data object CheckItemFailure : TeamChecklistUiEvent

    data class UpdateChecklistHeader(
        val position: Int,
        val isExpanded: Boolean,
    ) : TeamChecklistUiEvent
}
