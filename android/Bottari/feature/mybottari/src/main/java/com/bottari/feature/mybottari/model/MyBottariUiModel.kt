package com.bottari.feature.mybottari.model

import androidx.compose.runtime.Immutable

@Immutable
interface MyBottariUiModel {
    val id: Long
    val title: String
    val totalQuantity: Int
    val checkedQuantity: Int
    val alarm: AlarmUiModel?
}
