package com.bottari.presentation.model

import com.bottari.domain.model.team.member.MemberCheckStatus

data class MemberCheckStatusUiModel(
    val name: String,
    val checked: Boolean,
) {
    companion object {
        fun fromDomain(memberCheckStatus: MemberCheckStatus): MemberCheckStatusUiModel =
            MemberCheckStatusUiModel(
                name = memberCheckStatus.name,
                checked = memberCheckStatus.checked,
            )
    }
}
