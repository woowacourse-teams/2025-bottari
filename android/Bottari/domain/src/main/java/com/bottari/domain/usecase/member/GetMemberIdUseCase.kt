package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository

class GetMemberIdUseCase(
    private val memberRepository: MemberRepository,
) {
    operator fun invoke(): Result<Long> = memberRepository.getMemberId()
}
