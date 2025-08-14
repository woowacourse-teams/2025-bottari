package com.bottari.domain.model.team

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.bottari.BottariItem

data class TeamBottariDetail(
    val id: Long,
    val title: String,
    val alarm: Alarm?,
    val personalItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
    val sharedItems: List<BottariItem>,
)
