package com.bottari.feature.template.detail.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class TemplateDetailNavKey(
    val templateId: Long,
    val isMyTemplate: Boolean,
    val isBookmark: Boolean,
) : NavKey
