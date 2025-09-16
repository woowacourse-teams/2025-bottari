package com.bottari.presentation.model

import com.bottari.domain.model.bottari.BottariState

data class BottariUiModel(
    val id: Long,
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val alarm: AlarmUiModel?,
) {
    companion object {
        fun fromDomain(bottariState: BottariState): BottariUiModel =
            BottariUiModel(
                id = bottariState.bottari.id,
                title = bottariState.bottari.title,
                totalQuantity = bottariState.itemCount.totalQuantity,
                checkedQuantity = bottariState.itemCount.checkedQuantity,
                alarm = bottariState.bottari.alarm?.let { AlarmUiModel.fromDomain(it) },
            )
    }
}
