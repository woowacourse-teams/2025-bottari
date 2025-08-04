package com.bottari.presentation.model

data class MyBottariUiModel(
    val id: Long,
    val title: String,
    val isSelected: Boolean,
    val items: List<BottariItemUiModel>,
)
