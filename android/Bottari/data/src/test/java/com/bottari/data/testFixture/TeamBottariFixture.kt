package com.bottari.data.testFixture

import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamBottariDetail

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
