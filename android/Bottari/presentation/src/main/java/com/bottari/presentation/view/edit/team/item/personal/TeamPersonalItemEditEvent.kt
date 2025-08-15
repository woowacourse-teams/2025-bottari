package com.bottari.presentation.view.edit.team.item.personal

sealed interface TeamPersonalItemEditEvent {
    data object FetchTeamPersonalItemsFailure : TeamPersonalItemEditEvent

    data object DeleteItemFailure : TeamPersonalItemEditEvent

    data object AddItemFailure : TeamPersonalItemEditEvent
}
