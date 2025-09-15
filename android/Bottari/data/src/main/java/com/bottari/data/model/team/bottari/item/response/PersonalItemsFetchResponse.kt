package com.bottari.data.model.team.bottari.item.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonalItemsFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
