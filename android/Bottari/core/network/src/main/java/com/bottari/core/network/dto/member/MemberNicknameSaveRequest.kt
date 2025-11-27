package com.bottari.core.network.dto.member

import com.bottari.core.domain.model.member.Nickname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberNicknameSaveRequest(
    @SerialName("name")
    val value: String,
) {
    companion object {
        fun fromDomain(nickname: Nickname): MemberNicknameSaveRequest = MemberNicknameSaveRequest(nickname.value)
    }
}
