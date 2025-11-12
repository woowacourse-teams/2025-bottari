package com.bottari.domain.model.team.member

import com.bottari.domain.model.member.Nickname

data class TeamStatus(
    val inviteCode: String,
    val memberCount: HeadCount,
    val hostName: Nickname,
    val nicknames: List<Nickname>,
)
