package com.bottari.data.source.local.tooltip

import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow

interface TooltipLocalDataSource {
    suspend fun updateStatus(tooltipType: TooltipType): Result<Unit>

    fun isTooltipDismissed(type: TooltipType): Flow<Boolean>
}
