package com.bottari.data.mapper

import com.bottari.data.model.member.MemberResponse
import com.bottari.domain.model.member.RegisteredMember

object MemberMapper {
    fun MemberResponse.toDomain(): RegisteredMember = RegisteredMember(name = name, id = id, isRegistered = isRegistered)
}
