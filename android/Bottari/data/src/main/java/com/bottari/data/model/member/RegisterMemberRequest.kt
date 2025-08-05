package com.bottari.data.model.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterMemberRequest(
    @SerialName("ssaid")
    val ssaid: String,
)
