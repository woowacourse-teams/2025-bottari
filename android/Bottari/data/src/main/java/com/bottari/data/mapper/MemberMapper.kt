package com.bottari.data.mapper

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember

object MemberMapper {
    fun Member.toRequest(): RegisterMemberRequest = RegisterMemberRequest(name = nickname, ssaid = ssaid)

    fun CheckRegisteredMemberResponse.toDomain(): RegisteredMember = RegisteredMember(name = name, isRegistered = isRegistered)
}
