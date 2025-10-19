package com.bottari.data.source.local.tooltip

import com.bottari.data.local.tooltip.TooltipDataStore
import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TooltipLocalDataSourceImpl @Inject constructor(
    private val dataStore: TooltipDataStore,
) : TooltipLocalDataSource {
    override suspend fun updateStatus(tooltipType: TooltipType): Result<Unit> =
        runCatching {
            dataStore.setTooltipDismissed(tooltipType)
        }

    override fun isTooltipDismissed(type: TooltipType): Flow<Boolean> = dataStore.isTooltipDismissed(type)
}
