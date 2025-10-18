package com.bottari.domain.usecase.member

import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository
import javax.inject.Inject

class CheckRegisteredMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<RegisteredMember> = memberRepository.checkRegisteredMember()
}
