package com.bottari.presentation.compose.edit.team.shared

sealed interface TeamSharedItemEditEvent {
    data object FetchTeamSharedItemsFailure : TeamSharedItemEditEvent

    data object DeleteItemFailure : TeamSharedItemEditEvent

    data object CreateItemFailure : TeamSharedItemEditEvent
}
