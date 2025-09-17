package com.bottari.domain.model.team.member

import com.bottari.domain.model.bottari.item.BottariItemCount
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.member.Nickname

data class TeamMemberStatus(
    val id: Long,
    val nickname: Nickname,
    val isHost: Boolean,
    val itemCount: BottariItemCount,
    val sharedItems: List<ChecklistItem>,
    val assignedItems: List<ChecklistItem>,
)
