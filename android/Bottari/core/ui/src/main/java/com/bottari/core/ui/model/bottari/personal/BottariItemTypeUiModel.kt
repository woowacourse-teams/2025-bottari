package com.bottari.core.ui.model.bottari.personal

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
sealed interface BottariItemTypeUiModel : Parcelable {
    data object PERSONAL : BottariItemTypeUiModel

    data object SHARED : BottariItemTypeUiModel

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
                is TeamBottariItemType.ASSIGNED ->
                    ASSIGNED(teamBottariItemType.members.map { TeamMemberUiModel.fromDomain(it) })
            }
    }
}
