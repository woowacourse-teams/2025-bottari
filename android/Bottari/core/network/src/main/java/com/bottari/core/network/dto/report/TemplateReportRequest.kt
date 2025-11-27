package com.bottari.core.network.dto.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TemplateReportRequest(
    @SerialName("reason")
    val reason: String,
)
