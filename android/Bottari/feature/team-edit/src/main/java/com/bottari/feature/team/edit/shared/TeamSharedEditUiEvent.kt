package com.bottari.feature.team.edit.shared

sealed interface TeamSharedEditUiEvent {
    data object FetchTeamSharedItemsFailure : TeamSharedEditUiEvent

    data object DeleteItemFailure : TeamSharedEditUiEvent

    data object CreateItemFailure : TeamSharedEditUiEvent
}
