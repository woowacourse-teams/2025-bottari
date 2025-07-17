package com.bottari.presentation.model

data class BottariUiModel(
    val title: String,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val alarmTypeUiModel: AlarmTypeUiModel,
)
