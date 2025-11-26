package com.bottari.core.data.source.local

import com.bottari.core.domain.model.tooltip.TooltipType
import com.bottari.core.local.datastore.TooltipDataStore
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
