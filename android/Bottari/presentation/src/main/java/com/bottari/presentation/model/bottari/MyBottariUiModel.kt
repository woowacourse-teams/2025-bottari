package com.bottari.presentation.model.bottari

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.alarm.AlarmUiModel

@Immutable
interface MyBottariUiModel {
    val id: Long
    val title: String
    val totalQuantity: Int
    val checkedQuantity: Int
    val alarm: AlarmUiModel?
}
