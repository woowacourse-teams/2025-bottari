package com.bottari.di

import com.bottari.domain.usecase.alarm.CreateAlarmUseCase
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.bottariDetail.FindBottariDetailUseCase
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase
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
        FindBottariDetailUseCase(RepositoryProvider.bottariRepository)
    }
    val saveAlarmUseCase by lazy {
        SaveAlarmUseCase(RepositoryProvider.alarmRepository)
    }
    val createAlarmUseCase by lazy {
        CreateAlarmUseCase(RepositoryProvider.alarmRepository)
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
    val createBottariTemplateUseCase by lazy {
        CreateBottariTemplateUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val fetchBottariTemplateDetailUseCase by lazy {
        FetchBottariTemplateDetailUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val takeBottariTemplateDetailUseCase by lazy {
        TakeBottariTemplateDetailUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val saveBottariItemsUseCase by lazy {
        SaveBottariItemsUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val checkRegisteredMemberUseCase by lazy {
        CheckRegisteredMemberUseCase(
            RepositoryProvider.memberRepository,
        )
    }

    val deleteBottariUseCase by lazy {
        DeleteBottariUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
}
