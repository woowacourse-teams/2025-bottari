package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.domain.usecase.member.SaveMemberNicknameUseCase

object MemberUseCaseProvider {
    val registerMemberUseCase: RegisterMemberUseCase by lazy {
        RegisterMemberUseCase(
            RepositoryProvider.memberRepository,
        )
    }
    val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase by lazy {
        CheckRegisteredMemberUseCase(
            RepositoryProvider.memberRepository,
        )
    }
    val saveMemberNicknameUseCase: SaveMemberNicknameUseCase by lazy {
        SaveMemberNicknameUseCase(
            RepositoryProvider.memberRepository,
        )
    }
}
