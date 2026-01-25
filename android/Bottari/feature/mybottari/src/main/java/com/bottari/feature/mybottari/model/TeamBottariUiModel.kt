package com.bottari.feature.mybottari.model

import com.bottari.core.domain.model.team.bottari.TeamBottari

data class TeamBottariUiModel(
    override val id: Long,
    override val title: String,
    override val totalQuantity: Int,
    override val checkedQuantity: Int,
    override val alarm: AlarmUiModel?,
    val memberCount: Int,
) : MyBottariUiModel {
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
