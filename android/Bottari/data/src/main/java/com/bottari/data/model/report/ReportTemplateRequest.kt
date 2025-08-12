package com.bottari.data.model.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportTemplateRequest(
    @SerialName("reason")
    val reason: String,
)
