package com.bottari.core.network.dto.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberRegisterRequest(
    @SerialName("ssaid")
    val ssaid: String,
    @SerialName("fcmToken")
    val fcmToken: String,
)
