package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.member.Nickname

data class TeamMemberStatus(
    val nickname: Nickname,
    val isHost: Boolean,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
)
