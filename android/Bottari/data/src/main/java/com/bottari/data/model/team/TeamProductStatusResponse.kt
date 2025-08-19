package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamProductStatusResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("memberCheckStatus")
    val memberCheckStatus: List<MemberCheckStatusResponse>,
    @SerialName("checkItemsCount")
    val checkItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)
