package com.bottari.data.model.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterMemberRequest(
    @SerialName("name")
    val name: String,
    @SerialName("ssaid")
    val ssaid: String,
)
