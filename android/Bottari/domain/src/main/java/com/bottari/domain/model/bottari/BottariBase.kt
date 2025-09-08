package com.bottari.domain.model.bottari

import com.bottari.domain.model.alarm.Alarm

data class BottariBase(
    val id: Long,
    val title: String,
    val alarm: Alarm?,
)
