package com.bottari.data.testFixture

import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariMemberResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.model.team.TeamMemberStatus

val TEAM_BOTTARI_RESPONSE: FetchTeamBottariResponse by lazy {
    FetchTeamBottariResponse(
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
        1L,
        "test",
        10,
        10,
        3,
        null,
    )
}

val TEAM_BOTTARI_DETAIL_RESPONSE =
    FetchTeamBottariDetailResponse(
        1L,
        "test",
        null,
        emptyList(),
        emptyList(),
        emptyList(),
    )
val TEAM_BOTTARI_DETAIL =
    TeamBottariDetail(
        1L,
        "test",
        null,
        emptyList(),
        emptyList(),
        emptyList(),
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
        FetchTeamMemberStatusResponse(
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
    FetchTeamBottariMemberResponse(
        1L,
        "member1",
    )

val TEAM_MEMBER = TeamMember(1L, "member1")
