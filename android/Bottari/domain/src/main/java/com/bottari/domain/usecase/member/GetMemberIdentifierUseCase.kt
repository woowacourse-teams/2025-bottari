package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository

class GetMemberIdentifierUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<String> = memberRepository.getMemberIdentifier()
}
