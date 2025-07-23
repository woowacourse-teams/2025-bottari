package com.bottari.di

import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.domain.util.NicknameGenerator

object UseCaseProvider {
    val registerMemberUseCase by lazy {
        RegisterMemberUseCase(
            RepositoryProvider.memberRepository,
            NicknameGenerator(),
        )
    }
    val fetchBottariesUseCase by lazy {
        FetchBottariesUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
}
