package com.bottari.presentation.compose.edit.team.shared

sealed interface TeamSharedEditUiEvent {
    data object FetchTeamSharedItemsFailure : TeamSharedEditUiEvent

    data object DeleteItemFailure : TeamSharedEditUiEvent

    data object CreateItemFailure : TeamSharedEditUiEvent
}
