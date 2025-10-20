package com.bottari.presentation.model.template

import com.bottari.domain.model.bottari.template.BottariTemplateHashtag

data class BottariTemplateHashtagUiModel(
    val id: Long,
    val name: String,
) {
    companion object {
        fun fromDomain(bottariTemplateHashtag: BottariTemplateHashtag): BottariTemplateHashtagUiModel =
            BottariTemplateHashtagUiModel(
                id = bottariTemplateHashtag.id,
                name = bottariTemplateHashtag.name,
            )
    }
}
