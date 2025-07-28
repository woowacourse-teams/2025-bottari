package com.bottari.domain.usecase.member

import com.bottari.domain.model.member.Member
import com.bottari.domain.repository.MemberRepository

class UpdateMemberNicknameUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        nickname: String,
    ): Result<Unit> =
        runCatching {
            val member = Member(ssaid, nickname)
            memberRepository.updateMemberNickname(ssaid, member).getOrThrow()
        }
}
