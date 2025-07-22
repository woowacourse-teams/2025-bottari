package com.bottari.data.mapper

import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.domain.model.member.Member

object MemberMapper {
    fun Member.toRequest(): RegisterMemberRequest =
        RegisterMemberRequest(name = nickname, ssaid = ssaid)
}
