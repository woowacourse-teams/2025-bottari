package com.bottari.feature.mybottari.model

import com.bottari.core.domain.model.bottari.BottariState
import com.bottari.core.domain.model.bottari.personal.PersonalBottari

data class BottariUiModel(
    override val id: Long,
    override val title: String,
    override val totalQuantity: Int,
    override val checkedQuantity: Int,
    override val alarm: AlarmUiModel?,
) : MyBottariUiModel {
    companion object {
        fun fromDomain(bottariState: BottariState): BottariUiModel =
            BottariUiModel(
                id = bottariState.bottari.id,
                title = bottariState.bottari.title,
                totalQuantity = bottariState.itemCount.totalQuantity,
                checkedQuantity = bottariState.itemCount.checkedQuantity,
                alarm = bottariState.bottari.alarm?.let { AlarmUiModel.fromDomain(it) },
            )

        fun fromPersonalBottari(bottari: PersonalBottari): BottariUiModel =
            BottariUiModel(
                id = bottari.id,
                title = bottari.title,
                totalQuantity = bottari.totalQuantity,
                checkedQuantity = bottari.checkedQuantity,
                alarm = bottari.alarm?.let { alarm -> AlarmUiModel.fromDomain(alarm) },
            )
    }
}
