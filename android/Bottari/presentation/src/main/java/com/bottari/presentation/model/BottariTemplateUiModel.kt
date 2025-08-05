package com.bottari.presentation.model

data class BottariTemplateUiModel(
    val id: Long,
    val title: String,
    val items: List<BottariTemplateItemUiModel>,
    val author: String,
    val takenCount: Int,
)
