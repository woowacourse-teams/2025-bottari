package com.bottari.presentation.model

data class TeamBottariUiModel(
    val id: Long,
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val memberCount: Int,
    val alarm: AlarmUiModel?,
)
