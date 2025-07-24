package com.bottari.di

import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.domain.usecase.bottariDetail.FindBottariDetailUseCase
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
    val findBottariDetailUseCase by lazy {
        FindBottariDetailUseCase(RepositoryProvider.bottariDetailRepository)
    }

    val toggleAlarmStateUseCase by lazy {
        ToggleAlarmStateUseCase(RepositoryProvider.alarmRepository)
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
}
