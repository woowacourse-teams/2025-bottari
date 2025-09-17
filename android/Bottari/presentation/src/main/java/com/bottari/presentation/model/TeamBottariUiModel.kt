package com.bottari.presentation.model

import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.presentation.model.alarm.AlarmUiModel

data class TeamBottariUiModel(
    val id: Long,
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val memberCount: Int,
    val alarm: AlarmUiModel?,
) {
    companion object {
        fun fromDomain(teamBottari: TeamBottari): TeamBottariUiModel =
            TeamBottariUiModel(
                id = teamBottari.id,
                title = teamBottari.title,
                totalQuantity = teamBottari.itemCount.totalQuantity,
                checkedQuantity = teamBottari.itemCount.checkedQuantity,
                memberCount = teamBottari.memberCount.value,
                alarm = teamBottari.alarm?.let { AlarmUiModel.fromDomain(it) },
            )
    }
}
