package com.bottari.presentation.fixture

import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.member.HeadCount
import com.bottari.domain.model.team.member.TeamStatus

val TEAM_MEMBERS_FIXTURE =
    TeamStatus(
        inviteCode = "INVITE123",
        memberCount = HeadCount(3),
        hostName = Nickname("Host"),
        nicknames = listOf(Nickname("test1"), Nickname("test2"), Nickname("Host")),
    )
