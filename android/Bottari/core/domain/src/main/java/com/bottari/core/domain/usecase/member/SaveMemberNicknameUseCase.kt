package com.bottari.core.domain.usecase.member

import com.bottari.core.domain.model.member.Nickname
import com.bottari.core.domain.repository.MemberRepository
import javax.inject.Inject

class SaveMemberNicknameUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(newNickname: String): Result<Unit> =
        runCatching {
            val nickname = Nickname(newNickname)
            memberRepository.saveMemberNickname(nickname).getOrThrow()
        }
}
