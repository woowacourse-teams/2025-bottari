package com.bottari.data.source.local.tooltip

import com.bottari.data.model.local.tooltip.TooltipEntity
import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow

interface TooltipLocalDataSource {
    suspend fun updateStatus(tooltipEntity: TooltipEntity): Result<Unit>

    fun isTooltipDismissed(type: TooltipType): Flow<Boolean>
}
