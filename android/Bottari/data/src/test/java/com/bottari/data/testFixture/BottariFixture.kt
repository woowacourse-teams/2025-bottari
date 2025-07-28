package com.bottari.data.testFixture

import com.bottari.data.model.bottari.AlarmResponse
import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.RoutineResponse
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
            alarmResponse = null,
            checkedItemsCount = 0,
            totalItemsCount = 0,
        ),
        FetchBottariesResponse(
            id = 3,
            title = "title2",
            alarmResponse = null,
            checkedItemsCount = 1,
            totalItemsCount = 3,
        ),
    )

fun bottariResponseFixture(
    id: Long = 100L,
    title: String = "detail",
    alarm: AlarmResponse = alarmResponseFixture(),
): BottariResponse =
    BottariResponse(
        id = id,
        title = title,
        items = emptyList(),
        alarm = alarm,
    )
