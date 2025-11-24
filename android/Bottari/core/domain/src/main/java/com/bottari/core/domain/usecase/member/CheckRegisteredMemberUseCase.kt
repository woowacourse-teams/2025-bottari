package com.bottari.core.domain.usecase.member

import com.bottari.core.domain.model.member.RegisteredMember
import com.bottari.core.domain.repository.MemberRepository
import javax.inject.Inject

class CheckRegisteredMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<RegisteredMember> = memberRepository.checkRegisteredMember()
}
