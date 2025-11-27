package com.bottari.core.domain.model.bottari

import com.bottari.core.domain.model.bottari.item.BottariItemCount

data class BottariState(
    val bottari: Bottari,
    val itemCount: BottariItemCount,
)
