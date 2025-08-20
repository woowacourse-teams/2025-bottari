package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository

class GetMemberIdUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<Long> = memberRepository.getMemberId()
}
