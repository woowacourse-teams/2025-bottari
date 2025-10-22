package com.bottari.presentation.model.template

data class BookmarkedTemplateUiModel(
    val id: Long,
    val templateId: Long,
    val title: String,
    val description: String,
    val items: List<String>,
    val hashtags: List<String>,
    val author: String,
)
