package com.bottari.presentation.view.market

interface MarketNavigator {
    fun navigateToDetail(
        bottariTemplateId: Long,
        isMyTemplate: Boolean = false,
    )

    fun navigateToMyTemplate()
}
