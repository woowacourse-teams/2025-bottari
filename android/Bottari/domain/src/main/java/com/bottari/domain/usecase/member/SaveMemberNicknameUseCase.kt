package com.bottari.domain.usecase.member

import com.bottari.domain.model.member.Nickname
import com.bottari.domain.repository.MemberRepository

class SaveMemberNicknameUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(newNickname: String): Result<Unit> =
        runCatching {
            val nickname = Nickname(newNickname)
            memberRepository.saveMemberNickname(nickname).getOrThrow()
        }
}
