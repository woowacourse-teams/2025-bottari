package com.bottari.domain.model.team

data class TeamProductStatus(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatus>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
)
