package com.bottari.presentation.compose.edit.team.shared

sealed interface TeamSharedEditEvent {
    data object FetchTeamSharedItemsFailure : TeamSharedEditEvent

    data object DeleteItemFailure : TeamSharedEditEvent

    data object CreateItemFailure : TeamSharedEditEvent
}
