package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberCheckStatusResponse(
    @SerialName("name")
    val name: String,
    @SerialName("checked")
    val checked: Boolean,
)
