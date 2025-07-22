package com.bottari.domain.usecase.member

import com.bottari.domain.model.member.Member
import com.bottari.domain.repository.MemberRepository
import com.bottari.domain.util.NicknameGenerator

class RegisterMemberUseCase(
    private val memberRepository: MemberRepository,
    private val nicknameGenerator: NicknameGenerator,
) {
    suspend operator fun invoke(ssaid: String): Result<Boolean> {
        val member = Member(ssaid, nicknameGenerator.generate())
        return memberRepository.registerMember(member)
    }
}
