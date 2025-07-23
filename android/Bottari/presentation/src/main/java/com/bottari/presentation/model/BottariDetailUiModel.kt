package com.bottari.presentation.model

data class BottariDetailUiModel(
    val id: Long,
    val title: String,
    val alarm: AlarmUiModel?,
    val items: List<BottariItemUiModel> = emptyList(),
)
