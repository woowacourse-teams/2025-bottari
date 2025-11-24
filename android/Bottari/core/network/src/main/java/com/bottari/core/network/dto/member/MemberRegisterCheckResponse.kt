package com.bottari.core.network.dto.member

import com.bottari.core.domain.model.member.RegisteredMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberRegisterCheckResponse(
    @SerialName("isRegistered")
    val isRegistered: Boolean,
    @SerialName("id")
    val id: Long?,
    @SerialName("name")
    val name: String?,
) {
    fun toDomain(): RegisteredMember = RegisteredMember(name = name, id = id, isRegistered = isRegistered)
}
