package com.bottari.domain.usecase.member

import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository

class CheckRegisteredMemberUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(ssaid: String): Result<RegisteredMember> = memberRepository.checkRegisteredMember(ssaid)
}
