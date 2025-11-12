package com.bottari.data.model.remote.member

import com.bottari.domain.model.member.Nickname
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
