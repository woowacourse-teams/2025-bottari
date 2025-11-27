package com.bottari.core.domain.model.team.bottari

import com.bottari.core.domain.model.bottari.item.BottariItemCount
import com.bottari.core.domain.model.team.member.MemberCheckStatus

data class TeamBottariProductStatus(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatus>,
    val itemCount: BottariItemCount,
)
