package com.bottari.core.data.source.local

import com.bottari.core.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow

interface TooltipLocalDataSource {
    suspend fun updateStatus(tooltipType: TooltipType): Result<Unit>

    fun isTooltipDismissed(type: TooltipType): Flow<Boolean>
}
