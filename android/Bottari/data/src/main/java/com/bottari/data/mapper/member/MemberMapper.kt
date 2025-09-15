package com.bottari.data.mapper.member

import com.bottari.data.model.member.MemberResponse
import com.bottari.data.model.member.MemberSaveNicknameRequest
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.member.RegisteredMember

object MemberMapper {
    fun MemberResponse.toDomain(): RegisteredMember = RegisteredMember(name = name, id = id, isRegistered = isRegistered)

    fun Nickname.toRequest(): MemberSaveNicknameRequest = MemberSaveNicknameRequest(value)
}
