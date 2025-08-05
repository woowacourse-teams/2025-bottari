package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository

class RegisterMemberUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(ssaid: String): Result<Long?> = memberRepository.registerMember(ssaid)
}
