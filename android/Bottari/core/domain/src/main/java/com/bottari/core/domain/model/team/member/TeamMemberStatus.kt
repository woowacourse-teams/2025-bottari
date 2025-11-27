package com.bottari.core.domain.model.team.member

import com.bottari.core.domain.model.bottari.item.BottariItemCount
import com.bottari.core.domain.model.bottari.item.ChecklistItem
import com.bottari.core.domain.model.member.Nickname

data class TeamMemberStatus(
    val id: Long,
    val nickname: Nickname,
    val isHost: Boolean,
    val itemCount: BottariItemCount,
    val sharedItems: List<ChecklistItem>,
    val assignedItems: List<ChecklistItem>,
)
