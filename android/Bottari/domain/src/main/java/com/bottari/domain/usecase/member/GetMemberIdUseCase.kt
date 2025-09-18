package com.bottari.domain.usecase.member

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.MemberRepository

class GetMemberIdUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): BottariResult<Long> = memberRepository.getMemberId()
}
