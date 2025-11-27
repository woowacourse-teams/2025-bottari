package com.bottari.presentation.model.template

import com.bottari.core.domain.model.bottari.template.Hashtag
import com.bottari.core.domain.model.bottari.template.PopularHashtag

data class BottariTemplateHashtagUiModel(
    val id: Long,
    val name: String,
) {
    companion object {
        fun fromDomain(hashtag: Hashtag): BottariTemplateHashtagUiModel =
            BottariTemplateHashtagUiModel(
                id = hashtag.id,
                name = hashtag.name.value,
            )

        fun fromDomain(hashtag: PopularHashtag): BottariTemplateHashtagUiModel =
            BottariTemplateHashtagUiModel(
                id = hashtag.hashtag.id,
                name = hashtag.hashtag.name.value,
            )
    }
}
