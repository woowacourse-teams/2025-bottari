package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface BottariRequest {
    val title: String

    @Serializable
    data class CreateBottariRequest(
        @SerialName("title")
        override val title: String,
    ) : BottariRequest

    @Serializable
    data class UpdateBottariTitleRequest(
        @SerialName("title")
        override val title: String,
    ) : BottariRequest
}
