package com.bottari.presentation.model

import com.bottari.domain.model.team.bottari.TeamBottari

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
                id = teamBottari.bottari.id,
                title = teamBottari.bottari.title,
                totalQuantity = teamBottari.totalQuantity,
                checkedQuantity = teamBottari.checkedQuantity,
                memberCount = teamBottari.memberCount.value,
                alarm = teamBottari.bottari.alarm?.let { AlarmUiModel.fromDomain(it) },
            )
    }
}
