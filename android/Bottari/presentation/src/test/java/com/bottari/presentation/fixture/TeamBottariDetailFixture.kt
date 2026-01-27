package com.bottari.presentation.fixture

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.model.team.member.TeamMember

val TEAM_BOTTARI_DETAIL_FIXTURE: TeamBottariDetail by lazy {
    TeamBottariDetail(
        bottari =
            Bottari(
                id = 1L,
                title = "test",
                alarm = null,
                items = emptyList(),
            ),
        personalItems = List(3) { BOTTARI_PERSONAL_ITEM_FIXTURE.copy(id = it.toLong()) },
        assignedItems = List(3) { BOTTARI_ASSIGNED_ITEM_FIXTURE.copy(id = it.toLong()) },
        sharedItems = List(3) { BOTTARI_SHARED_ITEM_FIXTURE.copy(id = it.toLong()) },
    )
}

val BOTTARI_PERSONAL_ITEM_FIXTURE: BottariItem by lazy {
    BottariItem(
        1L,
        "TEST_ITEM",
        TeamBottariItemType.PERSONAL,
    )
}

val BOTTARI_SHARED_ITEM_FIXTURE: BottariItem by lazy {
    BottariItem(
        1L,
        "TEST_ITEM",
        TeamBottariItemType.SHARED,
    )
}

val BOTTARI_ASSIGNED_ITEM_FIXTURE: BottariItem by lazy {
    BottariItem(
        1L,
        "TEST_ITEM",
        TeamBottariItemType.ASSIGNED(
            listOf(
                TeamMember(1L, "Member1"),
                TeamMember(2L, "Member2"),
            ),
        ),
    )
}
