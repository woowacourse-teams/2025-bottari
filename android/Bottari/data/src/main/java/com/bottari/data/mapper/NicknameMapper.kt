package com.bottari.data.mapper

import com.bottari.data.model.member.SaveMemberNicknameRequest
import com.bottari.domain.model.member.Nickname

object NicknameMapper {
    fun Nickname.toRequest(): SaveMemberNicknameRequest = SaveMemberNicknameRequest(value)
}
