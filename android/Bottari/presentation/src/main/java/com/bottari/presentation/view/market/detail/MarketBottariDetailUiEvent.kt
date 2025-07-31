package com.bottari.presentation.view.market.detail

sealed interface MarketBottariDetailUiEvent {
    data object FetchBottariDetailFailure : MarketBottariDetailUiEvent

    data class TakeBottariTemplateSuccess(
        val bottariId: Long?,
    ) : MarketBottariDetailUiEvent

    data object TakeBottariTemplateFailure : MarketBottariDetailUiEvent
}
