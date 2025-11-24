package com.bottari.core.domain.usecase.tooltip

import com.bottari.core.domain.model.tooltip.TooltipType
import com.bottari.core.domain.repository.TooltipRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchTooltipStatusUseCase @Inject constructor(
    private val tooltipRepository: TooltipRepository,
) {
    operator fun invoke(type: TooltipType): Flow<Boolean> = tooltipRepository.fetchStatus(type)
}
