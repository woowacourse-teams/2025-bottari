package com.bottari.presentation.view.edit.team.item.shared

sealed interface TeamSharedItemEditEvent {
    data object FetchTeamSharedItemsFailure : TeamSharedItemEditEvent

    data object DeleteItemFailure : TeamSharedItemEditEvent

    data object CreateItemFailure : TeamSharedItemEditEvent

    data object CreateItemSuccuss : TeamSharedItemEditEvent
}
