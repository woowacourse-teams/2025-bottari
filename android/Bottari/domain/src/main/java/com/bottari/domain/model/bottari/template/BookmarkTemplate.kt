package com.bottari.domain.model.bottari.template

data class BookmarkTemplate(
    val id: Long = 0,
    val templateId: Long,
    val title: String,
    val description: String,
    val items: List<String>,
    val hashtags: List<String>,
    val createdAt: Long? = null,
)
