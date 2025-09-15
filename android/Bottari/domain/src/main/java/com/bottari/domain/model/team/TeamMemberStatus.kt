package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.BottariItemCount
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.model.member.Nickname

data class TeamMemberStatus(
    val id: Long,
    val nickname: Nickname,
    val isHost: Boolean,
    val itemCount: BottariItemCount,
    val sharedItems: List<ChecklistItem>,
    val assignedItems: List<ChecklistItem>,
)
