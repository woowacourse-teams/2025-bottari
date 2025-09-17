package com.bottari.domain.model.bottari

import com.bottari.domain.model.bottari.item.BottariItemCount

data class BottariState(
    val bottari: Bottari,
    val itemCount: BottariItemCount,
)
