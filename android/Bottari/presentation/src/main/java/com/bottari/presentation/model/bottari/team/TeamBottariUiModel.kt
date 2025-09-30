package com.bottari.presentation.model.bottari.team

import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.MyBottariUiModel

data class TeamBottariUiModel(
    override val id: Long,
    override val title: String,
    override val totalQuantity: Int,
    override val checkedQuantity: Int,
    val memberCount: Int,
    override val alarm: AlarmUiModel?,
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
