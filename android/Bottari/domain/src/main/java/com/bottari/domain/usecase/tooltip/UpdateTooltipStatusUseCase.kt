package com.bottari.domain.usecase.tooltip

import com.bottari.domain.model.tooltip.TooltipType
import com.bottari.domain.repository.TooltipRepository

class UpdateTooltipStatusUseCase(
    private val tooltipRepository: TooltipRepository,
) {
    suspend operator fun invoke(type: TooltipType): Result<Unit> = tooltipRepository.updateStatus(type)
}
