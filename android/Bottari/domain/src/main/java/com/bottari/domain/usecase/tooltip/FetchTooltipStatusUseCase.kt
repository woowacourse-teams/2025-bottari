package com.bottari.domain.usecase.tooltip

import com.bottari.domain.model.tooltip.TooltipType
import com.bottari.domain.repository.TooltipRepository
import kotlinx.coroutines.flow.Flow

class FetchTooltipStatusUseCase(
    private val tooltipRepository: TooltipRepository,
) {
    operator fun invoke(type: TooltipType): Flow<Boolean> = tooltipRepository.fetchStatus(type)
}
