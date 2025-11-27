package com.bottari.core.domain.usecase.tooltip

import com.bottari.core.domain.model.tooltip.TooltipType
import com.bottari.core.domain.repository.TooltipRepository
import javax.inject.Inject

class UpdateTooltipStatusUseCase @Inject constructor(
    private val tooltipRepository: TooltipRepository,
) {
    suspend operator fun invoke(type: TooltipType): Result<Unit> = tooltipRepository.updateStatus(type)
}
