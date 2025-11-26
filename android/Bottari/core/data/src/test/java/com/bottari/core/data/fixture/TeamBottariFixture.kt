package com.bottari.core.data.fixture

import com.bottari.core.domain.model.bottari.Bottari
import com.bottari.core.domain.model.bottari.item.BottariItemCount
import com.bottari.core.domain.model.member.Nickname
import com.bottari.core.domain.model.team.bottari.TeamBottari
import com.bottari.core.domain.model.team.bottari.TeamBottariDetail
import com.bottari.core.domain.model.team.member.HeadCount
import com.bottari.core.domain.model.team.member.TeamMember
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.core.network.dto.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.core.network.dto.team.bottari.TeamBottariFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberNameFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberStatusFetchResponse

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
        1L,
        "test",
        null,
        BottariItemCount(10, 10),
        HeadCount(3),
    )
}

val TEAM_BOTTARI_DETAIL_RESPONSE =
    TeamBottariDetailFetchResponse(
        1L,
        "test",
        null,
        emptyList(),
        emptyList(),
        emptyList(),
    )
val TEAM_BOTTARI_DETAIL =
    TeamBottariDetail(
        bottari =
            Bottari(
                id = 1L,
                title = "test",
                alarm = null,
                items = emptyList(),
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
            BottariItemCount(0, 0),
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
