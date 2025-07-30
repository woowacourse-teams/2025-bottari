package com.bottari.presentation.view.home.market

sealed interface MarketUiEvent {
    data object FetchBottariTemplatesSuccess : MarketUiEvent

    data object FetchBottariTemplatesFailure : MarketUiEvent
}
