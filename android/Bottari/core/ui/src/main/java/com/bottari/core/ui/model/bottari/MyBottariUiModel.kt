package com.bottari.core.ui.model.bottari

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.alarm.AlarmUiModel

@Immutable
interface MyBottariUiModel {
    val id: Long
    val title: String
    val totalQuantity: Int
    val checkedQuantity: Int
    val alarm: AlarmUiModel?
}
