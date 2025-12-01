package com.bottari.core.domain.extension

import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.toTimeMillis(): Long =
    this
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
