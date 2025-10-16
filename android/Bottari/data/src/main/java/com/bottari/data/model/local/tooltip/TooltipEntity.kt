package com.bottari.data.model.local.tooltip

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.bottari.domain.model.tooltip.TooltipType

@Entity(
    tableName = "tooltip_dismissals",
    primaryKeys = ["tooltip_type"],
)
data class TooltipEntity(
    @ColumnInfo(name = "tooltip_type")
    val tooltipType: TooltipType,
    @ColumnInfo(name = "dismissed_at")
    val dismissedAt: Long = System.currentTimeMillis(),
) {
    companion object {
        fun fromType(tooltipType: TooltipType): TooltipEntity = TooltipEntity(tooltipType)
    }
}
