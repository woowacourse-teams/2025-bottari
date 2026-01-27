package com.bottari.domain.repository

import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow

interface TooltipRepository {
    suspend fun updateStatus(type: TooltipType): Result<Unit>

    fun fetchStatus(type: TooltipType): Flow<Boolean>
}
