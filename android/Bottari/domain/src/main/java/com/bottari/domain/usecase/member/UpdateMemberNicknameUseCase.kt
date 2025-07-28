package com.bottari.domain.usecase.member

import com.bottari.domain.model.member.Member
import com.bottari.domain.repository.MemberRepository

class UpdateMemberNicknameUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(member: Member): Result<Unit> = memberRepository.updateMemberNickname(member.ssaid, member)
}
