package com.bottari.core.domain.usecase.member

import com.bottari.core.domain.repository.MemberRepository
import javax.inject.Inject

class RegisterMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(fcmToken: String): Result<Long?> = memberRepository.registerMember(fcmToken)
}
