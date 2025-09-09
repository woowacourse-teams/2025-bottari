package com.bottari.data.testFixture

import com.bottari.data.model.alarm.AlarmResponse
import com.bottari.data.model.alarm.RoutineResponse
import com.bottari.data.model.bottari.FetchBottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.teamItem.FetchTeamAssignedItemResponse
import com.bottari.data.model.teamItem.FetchTeamPersonalItemResponse
import com.bottari.data.model.teamItem.FetchTeamSharedItemResponse
import com.bottari.data.model.teamItem.SaveTeamAssignedItemRequest
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.team.TeamMember
import io.mockk.every
import io.mockk.mockk

fun alarmResponseFixture(
    id: Long = 1,
    isActive: Boolean = true,
    routineType: String = "NON_REPEAT",
): AlarmResponse {
    val routine =
        mockk<RoutineResponse>(relaxed = true).apply {
            every { type } returns routineType
        }

    return mockk(relaxed = true) {
        every { this@mockk.id } returns id
        every { this@mockk.isActive } returns isActive
        every { this@mockk.routine } returns routine
    }
}

fun fetchBottariesResponseFixture(): List<FetchBottariesResponse> =
    listOf(
        FetchBottariesResponse(
            id = 2,
            title = "title1",
            alarm = null,
            checkedItemsCount = 0,
            totalItemsCount = 0,
        ),
        FetchBottariesResponse(
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
    alarm: AlarmResponse = alarmResponseFixture(),
): FetchBottariResponse =
    FetchBottariResponse(
        id = id,
        title = title,
        items = emptyList(),
        alarm = alarm,
    )

val BOTTARI_PERSONAL_ITEM_RESPONSE_FIXTURE =
    FetchTeamPersonalItemResponse(
        1L,
        "item 1",
    )

val BOTTARI_SHARED_ITEM_RESPONSE_FIXTURE =
    FetchTeamSharedItemResponse(
        1L,
        "item 1",
    )

val BOTTARI_ASSIGNED_ITEM_RESPONSE_FIXTURE =
    FetchTeamAssignedItemResponse(
        1L,
        "item 1",
        listOf(
            FetchTeamAssignedItemResponse.Assignee(1L, "member 1"),
            FetchTeamAssignedItemResponse.Assignee(2L, "member 2"),
        ),
    )

val BOTTARI_PERSONAL_ITEM_FIXTURE =
    BottariItem(
        1L,
        "item 1",
        BottariItemType.PERSONAL,
    )

val BOTTARI_SHARED_ITEM_FIXTURE =
    BottariItem(
        1L,
        "item 1",
        BottariItemType.SHARED,
    )

val BOTTARI_ASSIGNED_ITEM_FIXTURE =
    BottariItem(
        1L,
        "item 1",
        BottariItemType.ASSIGNED(
            listOf(
                TeamMember(1L, "member 1"),
                TeamMember(2L, "member 2"),
            ),
        ),
    )

val SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE =
    SaveTeamAssignedItemRequest(
        name = "new name",
        assigneeIds = listOf(1L, 2L),
    )
