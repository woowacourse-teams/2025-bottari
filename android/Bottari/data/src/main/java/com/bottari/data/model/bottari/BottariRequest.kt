package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface BottariRequest {
    @Serializable
    data class CreateBottariRequest(
        @SerialName("title")
        val title: String,
    )

    @Serializable
    data class UpdateBottariTitleRequest(
        @SerialName("title")
        val title: String,
    )
}
