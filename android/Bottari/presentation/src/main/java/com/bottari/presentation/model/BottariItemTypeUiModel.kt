package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface BottariItemTypeUiModel : Parcelable {
    @Parcelize
    data object PERSONAL : BottariItemTypeUiModel

    @Parcelize
    data object SHARED : BottariItemTypeUiModel

    @Parcelize
    data class ASSIGNED(
        val members: List<TeamMemberUiModel> = emptyList(),
    ) : BottariItemTypeUiModel

    fun toTypeString() =
        when (this) {
            is PERSONAL -> "PERSONAL"
            is SHARED -> "SHARED"
            is ASSIGNED -> "ASSIGNED"
        }

    companion object {
        fun fromDomain(teamBottariItemType: TeamBottariItemType): BottariItemTypeUiModel =
            when (teamBottariItemType) {
                TeamBottariItemType.PERSONAL -> PERSONAL
                TeamBottariItemType.SHARED -> SHARED
                is TeamBottariItemType.ASSIGNED -> ASSIGNED(teamBottariItemType.members.map { TeamMemberUiModel.fromDomain(it) })
            }
    }
}
