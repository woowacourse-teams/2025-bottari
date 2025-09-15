package com.bottari.data.mapper.member

import com.bottari.data.model.member.MemberNicknameSaveRequest
import com.bottari.data.model.member.MemberRegisterCheckResponse
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.member.RegisteredMember

object MemberMapper {
    fun MemberRegisterCheckResponse.toRegisteredMember(): RegisteredMember =
        RegisteredMember(name = name, id = id, isRegistered = isRegistered)

    fun Nickname.toMemberSaveNicknameRequest(): MemberNicknameSaveRequest = MemberNicknameSaveRequest(value)
}
