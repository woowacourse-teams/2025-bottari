package com.bottari.presentation.model

data class BottariUiModel(
    val id: Long,
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val alarmTypeUiModel: AlarmTypeUiModel?,
    val items: List<ItemUiModel> = emptyList(),
)
