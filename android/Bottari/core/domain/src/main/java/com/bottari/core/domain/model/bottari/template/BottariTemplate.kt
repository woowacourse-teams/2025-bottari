package com.bottari.core.domain.model.bottari.template

data class BottariTemplate(
    val id: Long,
    val title: String,
    val description: String,
    val items: List<BottariTemplateItem>,
    val author: String,
    val takenCount: Int,
    val hashtags: List<Hashtag>,
    val isMarked: Boolean = false,
)
