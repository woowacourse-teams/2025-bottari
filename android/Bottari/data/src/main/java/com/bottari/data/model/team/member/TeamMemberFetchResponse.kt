package com.bottari.data.model.team.member

import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.member.HeadCount
import com.bottari.domain.model.team.member.TeamMembers
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
    fun toDomain(): TeamMembers =
        TeamMembers(
            inviteCode = inviteCode,
            teamMemberHeadCount = HeadCount(teamMemberCount),
            hostName = Nickname(ownerName),
            memberNicknames = teamMemberNames.map { name -> Nickname(name) },
        )
}
