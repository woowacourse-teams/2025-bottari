package com.bottari.domain.usecase.member

import com.bottari.domain.extension.mapCatching
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.getOrThrow
import com.bottari.domain.model.exception.toBottariResult
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.repository.MemberRepository

class SaveMemberNicknameUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(newNickname: String): BottariResult<Unit> =
        runCatching {
            Nickname(newNickname)
        }.mapCatching { nickname ->
            memberRepository.saveMemberNickname(nickname).getOrThrow()
        }.toBottariResult()
}
