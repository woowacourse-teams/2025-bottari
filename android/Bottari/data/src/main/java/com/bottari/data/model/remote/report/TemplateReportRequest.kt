package com.bottari.data.model.remote.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TemplateReportRequest(
    @SerialName("reason")
    val reason: String,
)
