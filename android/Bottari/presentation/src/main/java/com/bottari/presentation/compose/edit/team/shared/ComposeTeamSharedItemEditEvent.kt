package com.bottari.presentation.compose.edit.team.shared

sealed interface ComposeTeamSharedItemEditEvent {
    data object FetchComposeTeamSharedItemsFailure : ComposeTeamSharedItemEditEvent

    data object DeleteItemFailureCompose : ComposeTeamSharedItemEditEvent

    data object CreateItemFailureCompose : ComposeTeamSharedItemEditEvent

    data object CreateItemSuccessCompose : ComposeTeamSharedItemEditEvent
}
