package com.bottari.data.model.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckRegisteredMemberResponse(
    @SerialName("isRegistered")
    val isRegistered: Boolean,
    @SerialName("id")
    val id: Long?,
    @SerialName("name")
    val name: String?,
)
