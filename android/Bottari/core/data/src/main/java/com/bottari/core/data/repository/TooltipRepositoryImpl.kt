package com.bottari.core.data.repository

import com.bottari.core.data.source.local.TooltipLocalDataSource
import com.bottari.core.domain.model.tooltip.TooltipType
import com.bottari.core.domain.repository.TooltipRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TooltipRepositoryImpl @Inject constructor(
    private val tooltipLocalDataSource: TooltipLocalDataSource,
) : TooltipRepository {
    override suspend fun updateStatus(type: TooltipType): Result<Unit> = tooltipLocalDataSource.updateStatus(type)

    override fun fetchStatus(type: TooltipType): Flow<Boolean> = tooltipLocalDataSource.isTooltipDismissed(type)
}
