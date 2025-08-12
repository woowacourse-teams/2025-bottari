package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.domain.model.bottari.TeamBottari

object TeamBottariMapper {
    fun FetchTeamBottariResponse.toDomain(): TeamBottari =
        TeamBottari(
            alarm = alarmResponse?.toDomain(),
            checkedQuantity = checkedItemsCount,
            id = id,
            title = title,
            totalQuantity = totalItemsCount,
            memberCount = memberCount,
        )
}
