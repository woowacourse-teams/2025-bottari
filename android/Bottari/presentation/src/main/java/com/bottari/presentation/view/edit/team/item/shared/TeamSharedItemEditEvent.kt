package com.bottari.presentation.view.edit.team.item.shared

sealed interface TeamSharedItemEditEvent {
    data object FetchTeamPersonalItemsFailure : TeamSharedItemEditEvent

    data object DeleteItemFailure : TeamSharedItemEditEvent

    data object AddItemFailure : TeamSharedItemEditEvent
}
