package com.bottari.presentation.fixture

import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.HeadCount
import com.bottari.domain.model.team.TeamMembers

val TEAM_MEMBERS_FIXTURE =
    TeamMembers(
        inviteCode = "INVITE123",
        teamMemberHeadCount = HeadCount(3),
        hostName = Nickname("Host"),
        memberNicknames = listOf(Nickname("test1"), Nickname("test2"), Nickname("Host")),
    )
