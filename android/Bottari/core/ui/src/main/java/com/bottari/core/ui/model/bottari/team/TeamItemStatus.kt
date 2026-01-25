package com.bottari.core.ui.model.bottari.team

import androidx.compose.runtime.Immutable
import com.bottari.core.domain.model.team.bottari.TeamBottariProductStatus
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.model.bottari.team.member.MemberCheckStatusUiModel
import kotlin.math.roundToInt

sealed interface TeamItemStatus

@Immutable
data class TeamBottariUiModelStatus(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatusUiModel>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
    val type: BottariItemTypeUiModel,
) : TeamItemStatus {
    val isAllChecked: Boolean =
        memberCheckStatus.isNotEmpty() && memberCheckStatus.all { it.checked }

    val checkedMember: List<String> = memberCheckStatus.filter { it.checked }.map { it.name }
    val uncheckedMember: List<String> = memberCheckStatus.filter { !it.checked }.map { it.name }

    val checkedProgress: Int =
        if (totalItemsCount > 0) {
            ((checkItemsCount.toDouble() / totalItemsCount.toDouble()) * 100).roundToInt()
        } else {
            0
        }

    companion object {
        fun fromDomain(
            teamBottariProductStatus: TeamBottariProductStatus,
            type: BottariItemTypeUiModel,
        ): TeamBottariUiModelStatus =
            TeamBottariUiModelStatus(
                id = teamBottariProductStatus.id,
                name = teamBottariProductStatus.name,
                memberCheckStatus =
                    teamBottariProductStatus.memberCheckStatus.map { item ->
                        MemberCheckStatusUiModel.fromDomain(item)
                    },
                checkItemsCount = teamBottariProductStatus.itemCount.checkedQuantity,
                totalItemsCount = teamBottariProductStatus.itemCount.totalQuantity,
                type = type,
            )
    }
}
