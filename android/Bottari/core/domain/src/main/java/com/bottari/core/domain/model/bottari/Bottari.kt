package com.bottari.core.domain.model.bottari

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.bottari.item.BottariItem

data class Bottari(
    val id: Long,
    val title: String,
    val alarm: Alarm?,
    val items: List<BottariItem>,
)
