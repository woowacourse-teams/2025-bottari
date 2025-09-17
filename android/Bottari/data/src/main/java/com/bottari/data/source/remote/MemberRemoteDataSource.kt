package com.bottari.data.source.remote

import com.bottari.data.model.remote.member.MemberNicknameSaveRequest
import com.bottari.data.model.remote.member.MemberRegisterCheckResponse
import com.bottari.data.model.remote.member.MemberRegisterRequest
import com.bottari.domain.model.exception.BottariResult

interface MemberRemoteDataSource {
    suspend fun registerMember(request: MemberRegisterRequest): BottariResult<Long>

    suspend fun saveMemberNickname(request: MemberNicknameSaveRequest): BottariResult<Unit>

    suspend fun checkRegisteredMember(): BottariResult<MemberRegisterCheckResponse>
}
