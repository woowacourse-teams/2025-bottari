package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository
import javax.inject.Inject

class RegisterMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(fcmToken: String): Result<Long?> = memberRepository.registerMember(fcmToken)
}
