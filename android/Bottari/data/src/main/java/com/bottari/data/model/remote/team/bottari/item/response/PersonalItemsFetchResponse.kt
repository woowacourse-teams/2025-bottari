package com.bottari.data.model.remote.team.bottari.item.response

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
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
