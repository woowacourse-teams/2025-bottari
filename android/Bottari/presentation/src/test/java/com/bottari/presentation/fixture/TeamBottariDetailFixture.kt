package com.bottari.presentation.fixture

import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.team.TeamBottariDetail

val TEAM_BOTTARI_DETAIL_FIXTURE: TeamBottariDetail by lazy {
    TeamBottariDetail(
        id = 1L,
        title = "test",
        personalItems = List(3) { BOTTARI_PERSONAL_ITEM_FIXTURE.copy(id = it.toLong()) },
        assignedItems = List(3) { BOTTARI_ASSIGNED_ITEM_FIXTURE.copy(id = it.toLong()) },
        sharedItems = List(3) { BOTTARI_SHARED_ITEM_FIXTURE.copy(id = it.toLong()) },
        alarm = null,
    )
}

val BOTTARI_PERSONAL_ITEM_FIXTURE: BottariItem by lazy {
    BottariItem(
        1L,
        "TEST_ITEM",
        BottariItemType.PERSONAL,
    )
}

val BOTTARI_SHARED_ITEM_FIXTURE: BottariItem by lazy {
    BottariItem(
        1L,
        "TEST_ITEM",
        BottariItemType.SHARED,
    )
}

val BOTTARI_ASSIGNED_ITEM_FIXTURE: BottariItem by lazy {
    BottariItem(
        1L,
        "TEST_ITEM",
        BottariItemType.ASSIGNED(
            listOf("Member1", "Member2"),
        ),
    )
}
