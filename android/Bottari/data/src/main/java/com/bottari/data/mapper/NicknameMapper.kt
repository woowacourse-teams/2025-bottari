package com.bottari.data.mapper

import com.bottari.data.model.member.MemberSaveNicknameRequest
import com.bottari.domain.model.member.Nickname

object NicknameMapper {
    fun Nickname.toRequest(): MemberSaveNicknameRequest = MemberSaveNicknameRequest(value)
}
