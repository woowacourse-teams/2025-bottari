package com.bottari.data.local.tooltip

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottari.data.model.local.tooltip.TooltipEntity
import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow

@Dao
interface TooltipDismissalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tooltipEntity: TooltipEntity)

    @Query("SELECT COUNT(tooltip_type) > 0 FROM tooltip_dismissals WHERE tooltip_type = :tooltipType")
    fun isTooltipDismissed(tooltipType: TooltipType): Flow<Boolean>
}
