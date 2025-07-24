package com.bottari.data.source.remote

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: RegisterMemberRequest): Result<Unit>

    suspend fun checkRegisteredMember(ssaid: String): Result<CheckRegisteredMemberResponse>
}
