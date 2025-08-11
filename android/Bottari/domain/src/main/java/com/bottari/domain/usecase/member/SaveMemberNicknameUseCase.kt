package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository

class SaveMemberNicknameUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(nickname: String): Result<Unit> =
        runCatching {
            memberRepository.saveMemberNickname(nickname).getOrThrow()
        }
}
