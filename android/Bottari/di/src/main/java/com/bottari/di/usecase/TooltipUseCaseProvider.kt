package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.tooltip.FetchTooltipStatusUseCase
import com.bottari.domain.usecase.tooltip.UpdateTooltipStatusUseCase

object TooltipUseCaseProvider {
    val fetchTooltipStatusUseCase = FetchTooltipStatusUseCase(RepositoryProvider.tooltipRepository)

    val updateTooltipStatusUseCase =
        UpdateTooltipStatusUseCase(RepositoryProvider.tooltipRepository)
}
