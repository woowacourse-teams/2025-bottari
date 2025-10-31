package com.bottari.presentation.compose.edit.team.personal

sealed interface ComposeTeamPersonalItemEditEvent {
    data object FetchComposeTeamPersonalItemsFailure : ComposeTeamPersonalItemEditEvent

    data object DeleteItemFailureCompose : ComposeTeamPersonalItemEditEvent

    data object CreateItemFailureCompose : ComposeTeamPersonalItemEditEvent

    data object CreateItemSuccessCompose : ComposeTeamPersonalItemEditEvent
}
