package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.BottariItemCount

data class TeamBottariProductStatus(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatus>,
    val itemCount: BottariItemCount,
)
