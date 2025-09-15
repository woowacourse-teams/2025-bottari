package com.bottari.domain.model.team.bottari

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.team.member.HeadCount

data class TeamBottari(
    val bottari: Bottari,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val memberCount: HeadCount,
)
