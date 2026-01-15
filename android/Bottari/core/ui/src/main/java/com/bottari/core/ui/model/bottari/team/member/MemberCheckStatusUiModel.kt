package com.bottari.core.ui.model.bottari.team.member

import com.bottari.core.domain.model.team.member.MemberCheckStatus

data class MemberCheckStatusUiModel(
    val name: String,
    val checked: Boolean,
) {
    companion object {
        fun fromDomain(memberCheckStatus: MemberCheckStatus): MemberCheckStatusUiModel =
            MemberCheckStatusUiModel(
                name = memberCheckStatus.itemName,
                checked = memberCheckStatus.checked,
            )
    }
}
