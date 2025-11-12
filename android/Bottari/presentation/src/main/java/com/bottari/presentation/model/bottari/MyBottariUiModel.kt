package com.bottari.presentation.model.bottari

import com.bottari.presentation.model.alarm.AlarmUiModel

interface MyBottariUiModel {
    val id: Long
    val title: String
    val totalQuantity: Int
    val checkedQuantity: Int
    val alarm: AlarmUiModel?
}
