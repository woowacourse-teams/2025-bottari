package com.bottari.data.model.remote.team.member

import com.bottari.domain.model.team.member.TeamMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberNameFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
) {
    fun toDomain(): TeamMember =
        TeamMember(
            memberId = id,
            nickname = name,
        )
}
