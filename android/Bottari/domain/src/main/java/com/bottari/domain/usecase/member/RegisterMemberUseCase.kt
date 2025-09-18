package com.bottari.domain.usecase.member

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.MemberRepository

class RegisterMemberUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(fcmToken: String): BottariResult<Long> = memberRepository.registerMember(fcmToken)
}
