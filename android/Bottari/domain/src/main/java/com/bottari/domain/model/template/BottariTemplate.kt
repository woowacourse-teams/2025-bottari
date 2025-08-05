package com.bottari.domain.model.template

data class BottariTemplate(
    val id: Long,
    val title: String,
    val items: List<BottariTemplateItem>,
    val author: String,
    val takenCount: Int,
)
