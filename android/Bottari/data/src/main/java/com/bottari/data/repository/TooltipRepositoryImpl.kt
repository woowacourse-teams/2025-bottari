package com.bottari.data.repository

import com.bottari.data.source.local.tooltip.TooltipLocalDataSource
import com.bottari.domain.model.tooltip.TooltipType
import com.bottari.domain.repository.TooltipRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TooltipRepositoryImpl @Inject constructor(
    private val tooltipLocalDataSource: TooltipLocalDataSource,
) : TooltipRepository {
    override suspend fun updateStatus(type: TooltipType): Result<Unit> = tooltipLocalDataSource.updateStatus(type)

    override fun fetchStatus(type: TooltipType): Flow<Boolean> = tooltipLocalDataSource.isTooltipDismissed(type)
}
