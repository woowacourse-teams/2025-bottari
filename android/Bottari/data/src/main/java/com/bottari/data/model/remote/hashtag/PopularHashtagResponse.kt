package com.bottari.data.model.remote.hashtag

import com.bottari.domain.model.bottari.template.Hashtag
import com.bottari.domain.model.bottari.template.HashtagName
import com.bottari.domain.model.bottari.template.PopularHashtag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularHashtagResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("usageCount")
    val usageCount: Int,
) {
    fun toDomain(): PopularHashtag =
        PopularHashtag(
            hashtag =
                Hashtag(
                    id = id,
                    name = HashtagName.create(name).getOrDefault(HashtagName.UNKNOWN_HASHTAG_NAME),
                ),
            usageCount = usageCount,
        )
}
