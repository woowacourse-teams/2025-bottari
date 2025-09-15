package com.bottari.data.testFixture

import com.bottari.data.model.bottari.BottariAlarmFetchResponse
import com.bottari.data.model.bottari.BottariAlarmRoutineFetchResponse
import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariesFetchResponse
import com.bottari.data.model.team.bottari.item.request.AssignedItemsUpdateRequest
import com.bottari.data.model.team.bottari.item.response.AssignedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.response.PersonalItemsFetchResponse
import com.bottari.data.model.team.bottari.item.response.SharedItemsFetchResponse
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.model.team.member.TeamMember
import io.mockk.every
import io.mockk.mockk

fun bottariAlarmResponseFixture(
    id: Long = 1,
    isActive: Boolean = true,
    routineType: String = "NON_REPEAT",
): BottariAlarmFetchResponse {
    val routine =
        mockk<BottariAlarmRoutineFetchResponse>(relaxed = true).apply {
            every { type } returns routineType
        }

    return mockk(relaxed = true) {
        every { this@mockk.id } returns id
        every { this@mockk.isActive } returns isActive
        every { this@mockk.routine } returns routine
    }
}

fun fetchBottariesResponseFixture(): List<BottariesFetchResponse> =
    listOf(
        BottariesFetchResponse(
            id = 2,
            title = "title1",
            alarm = null,
            checkedItemsCount = 0,
            totalItemsCount = 0,
        ),
        BottariesFetchResponse(
            id = 3,
            title = "title2",
            alarm = null,
            checkedItemsCount = 1,
            totalItemsCount = 3,
        ),
    )

fun bottariResponseFixture(
    id: Long = 100L,
    title: String = "detail",
    alarm: BottariAlarmFetchResponse = bottariAlarmResponseFixture(),
): BottariFetchResponse =
    BottariFetchResponse(
        id = id,
        title = title,
        items = emptyList(),
        alarm = alarm,
    )

val BOTTARI_PERSONAL_ITEM_RESPONSE_FIXTURE =
    PersonalItemsFetchResponse(
        1L,
        "item 1",
    )

val BOTTARI_SHARED_ITEM_RESPONSE_FIXTURE =
    SharedItemsFetchResponse(
        1L,
        "item 1",
    )

val BOTTARI_ASSIGNED_ITEM_RESPONSE_FIXTURE =
    AssignedItemsFetchResponse(
        1L,
        "item 1",
        listOf(
            AssignedItemsFetchResponse.Assignee(1L, "member 1"),
            AssignedItemsFetchResponse.Assignee(2L, "member 2"),
        ),
    )

val BOTTARI_PERSONAL_ITEM_FIXTURE =
    BottariItem(
        1L,
        "item 1",
        TeamBottariItemType.PERSONAL,
    )

val BOTTARI_SHARED_ITEM_FIXTURE =
    BottariItem(
        1L,
        "item 1",
        TeamBottariItemType.SHARED,
    )

val BOTTARI_ASSIGNED_ITEM_FIXTURE =
    BottariItem(
        1L,
        "item 1",
        TeamBottariItemType.ASSIGNED(
            listOf(
                TeamMember(1L, "member 1"),
                TeamMember(2L, "member 2"),
            ),
        ),
    )

val SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE =
    AssignedItemsUpdateRequest(
        name = "new name",
        assigneeIds = listOf(1L, 2L),
    )
