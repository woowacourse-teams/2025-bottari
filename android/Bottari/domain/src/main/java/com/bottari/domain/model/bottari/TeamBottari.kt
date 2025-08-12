package com.bottari.domain.model.bottari

import com.bottari.domain.model.alarm.Alarm

data class TeamBottari(
    val id: Long,
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val memberCount: Int,
    val alarm: Alarm?,
)
