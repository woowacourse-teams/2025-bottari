package com.bottari.data.mapper

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.domain.model.member.RegisteredMember

object MemberMapper {
    fun CheckRegisteredMemberResponse.toDomain(): RegisteredMember = RegisteredMember(name = name, id = id, isRegistered = isRegistered)
}
