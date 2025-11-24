package com.bottari.core.domain.model.team.bottari

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.bottari.item.BottariItemCount
import com.bottari.core.domain.model.team.member.HeadCount

data class TeamBottari(
    val id: Long,
    val title: String,
    val alarm: Alarm? = null,
    val itemCount: BottariItemCount,
    val memberCount: HeadCount,
)
