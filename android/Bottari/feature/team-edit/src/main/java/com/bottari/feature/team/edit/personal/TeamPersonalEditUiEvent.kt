package com.bottari.feature.team.edit.personal

sealed interface TeamPersonalEditUiEvent {
    data object FetchTeamPersonalItemsFailure : TeamPersonalEditUiEvent

    data object DeleteItemFailureCompose : TeamPersonalEditUiEvent

    data object CreateItemFailureCompose : TeamPersonalEditUiEvent
}
