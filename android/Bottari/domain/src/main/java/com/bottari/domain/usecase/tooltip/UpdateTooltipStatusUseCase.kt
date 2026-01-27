package com.bottari.domain.usecase.tooltip

import com.bottari.domain.model.tooltip.TooltipType
import com.bottari.domain.repository.TooltipRepository
import javax.inject.Inject

class UpdateTooltipStatusUseCase @Inject constructor(
    private val tooltipRepository: TooltipRepository,
) {
    suspend operator fun invoke(type: TooltipType): Result<Unit> = tooltipRepository.updateStatus(type)
}
