package com.bottari.data.source.local.tooltip

import com.bottari.data.local.tooltip.TooltipDatabase
import com.bottari.data.local.tooltip.TooltipDismissalDao
import com.bottari.data.model.local.tooltip.TooltipEntity
import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow

class TooltipLocalDataSourceImpl(
    database: TooltipDatabase,
) : TooltipLocalDataSource {
    private val dao: TooltipDismissalDao = database.tooltipDao()

    override suspend fun updateStatus(tooltipEntity: TooltipEntity): Result<Unit> =
        runCatching {
            dao.insert(tooltipEntity)
        }

    override fun isTooltipDismissed(type: TooltipType): Flow<Boolean> = dao.isTooltipDismissed(type)
}
