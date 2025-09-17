package com.bottari.domain.model.team.bottari.item

import com.bottari.domain.model.team.member.TeamMember

sealed interface TeamBottariItemType {
    data object PERSONAL : TeamBottariItemType

    data object SHARED : TeamBottariItemType

    data class ASSIGNED(
        val members: List<TeamMember> = emptyList(),
    ) : TeamBottariItemType
}
