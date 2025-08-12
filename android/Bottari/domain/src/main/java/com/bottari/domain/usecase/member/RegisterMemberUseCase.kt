package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository

class RegisterMemberUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<Long?> = memberRepository.registerMember()
}
