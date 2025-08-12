package com.bottari.data.model.team

import kotlinx.serialization.Serializable

@Serializable
class TeamMemberItemResponse(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
)
