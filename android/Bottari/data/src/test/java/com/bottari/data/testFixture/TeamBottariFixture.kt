package com.bottari.data.testFixture

import com.bottari.data.model.team.bottari.TeamBottariFetchDetailResponse
import com.bottari.data.model.team.bottari.TeamBottariFetchResponse
import com.bottari.data.model.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusFetchResponse
import com.bottari.domain.model.bottari.BottariBase
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.model.team.TeamMemberStatus

val TEAM_BOTTARI_RESPONSE: TeamBottariFetchResponse by lazy {
    TeamBottariFetchResponse(
        1L,
        "test",
        null,
        10,
        10,
        3,
    )
}
val TEAM_BOTTARI: TeamBottari by lazy {
    TeamBottari(
        BottariBase(1L, "test", null),
        10,
        10,
        3,
    )
}

val TEAM_BOTTARI_DETAIL_RESPONSE =
    TeamBottariFetchDetailResponse(
        1L,
        "test",
        null,
        emptyList(),
        emptyList(),
        emptyList(),
    )
val TEAM_BOTTARI_DETAIL =
    TeamBottariDetail(
        base =
            BottariBase(
                id = 1L,
                title = "test",
                alarm = null,
            ),
        personalItems = emptyList(),
        assignedItems = emptyList(),
        sharedItems = emptyList(),
    )

val TEAM_MEMBERS_STATUS =
    listOf(
        TeamMemberStatus(
            1L,
            Nickname("Test"),
            true,
            0,
            0,
            emptyList(),
            emptyList(),
        ),
    )

val TEAM_MEMBERS_STATUS_RESPONSE =
    listOf(
        TeamMemberStatusFetchResponse(
            1L,
            "Test",
            true,
            0,
            0,
            emptyList(),
            emptyList(),
        ),
    )

val TEAM_MEMBER_RESPONSE =
    TeamMemberNameFetchResponse(
        1L,
        "member1",
    )

val TEAM_MEMBER = TeamMember(1L, "member1")
