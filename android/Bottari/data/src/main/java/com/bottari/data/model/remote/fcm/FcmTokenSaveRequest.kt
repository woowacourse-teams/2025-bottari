package com.bottari.data.model.remote.fcm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenSaveRequest(
    @SerialName("fcmToken")
    val fcmToken: String,
)
