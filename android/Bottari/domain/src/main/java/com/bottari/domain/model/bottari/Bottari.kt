package com.bottari.domain.model.bottari

import com.bottari.domain.model.alarm.Alarm

data class Bottari(
    val id: Long,
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val alarm: Alarm?,
)
