package com.bottari.core.network.dto.team.bottari.item

import com.bottari.core.domain.model.bottari.item.BottariItem
import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonalItemsFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
) {
    fun toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.PERSONAL,
        )
}
