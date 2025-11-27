package com.bottari.core.network.dto.fcm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenSaveRequest(
    @SerialName("fcmToken")
    val fcmToken: String,
)
