package com.bottari.domain.model.bottari

import com.bottari.domain.model.team.TeamMember

sealed interface BottariItemType {
    data object PERSONAL : BottariItemType

    data object SHARED : BottariItemType

    data class ASSIGNED(
        val members: List<TeamMember> = emptyList(),
    ) : BottariItemType
}
