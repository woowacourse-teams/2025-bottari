package com.bottari.di

import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
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
    val fetchChecklistUseCase by lazy {
        FetchChecklistUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val unCheckBottariItemUseCase by lazy {
        UnCheckBottariItemUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val checkBottariItemUseCase by lazy {
        CheckBottariItemUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val createBottariUseCase by lazy {
        CreateBottariUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val fetchBottariTemplatesUseCase by lazy {
        FetchBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val searchBottariTemplatesUseCase by lazy {
        SearchBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
}
