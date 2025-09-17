package com.bottari.data.model.remote.team.member

import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.member.HeadCount
import com.bottari.domain.model.team.member.TeamStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberFetchResponse(
    @SerialName("inviteCode")
    val inviteCode: String,
    @SerialName("teamMemberCount")
    val teamMemberCount: Int,
    @SerialName("ownerName")
    val ownerName: String,
    @SerialName("teamMemberNames")
    val teamMemberNames: List<String>,
) {
    fun toDomain(): TeamStatus =
        TeamStatus(
            inviteCode = inviteCode,
            memberCount = HeadCount(teamMemberCount),
            hostName = Nickname(ownerName),
            nicknames = teamMemberNames.map { name -> Nickname(name) },
        )
}
