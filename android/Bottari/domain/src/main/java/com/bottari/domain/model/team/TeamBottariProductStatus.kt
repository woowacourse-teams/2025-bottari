package com.bottari.domain.model.team

data class TeamBottariProductStatus(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatus>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
)
