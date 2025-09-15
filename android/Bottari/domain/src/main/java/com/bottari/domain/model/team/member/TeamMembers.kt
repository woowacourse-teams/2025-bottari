package com.bottari.domain.model.team.member

import com.bottari.domain.model.member.Nickname

data class TeamMembers(
    val inviteCode: String,
    val teamMemberHeadCount: HeadCount,
    val hostName: Nickname,
    val memberNicknames: List<Nickname>,
)
