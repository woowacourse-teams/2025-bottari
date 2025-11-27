package com.bottari.core.domain.usecase.member

import com.bottari.core.domain.repository.MemberRepository
import javax.inject.Inject

class GetMemberIdUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<Long> = memberRepository.getMemberId()
}
