package com.bottari.data.model.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberNicknameSaveRequest(
    @SerialName("name")
    val name: String,
)
