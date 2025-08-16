package com.bottari.di

import com.bottari.domain.usecase.alarm.CreateAlarmUseCase
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.domain.usecase.bottariDetail.FetchBottariDetailUseCase
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.GetMemberIdentifierUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.domain.usecase.member.SaveMemberNicknameUseCase
import com.bottari.domain.usecase.report.ReportTemplateUseCase
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.domain.usecase.team.SendRemindByMemberMessageUseCase
import com.bottari.domain.usecase.team.UnCheckTeamBottariItemUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase

object UseCaseProvider {
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
    val fetchBottariesUseCase: FetchBottariesUseCase by lazy {
        FetchBottariesUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val fetchBottariDetailUseCase: FetchBottariDetailUseCase by lazy {
        FetchBottariDetailUseCase(RepositoryProvider.bottariRepository)
    }
    val saveAlarmUseCase: SaveAlarmUseCase by lazy {
        SaveAlarmUseCase(RepositoryProvider.alarmRepository)
    }
    val createAlarmUseCase: CreateAlarmUseCase by lazy {
        CreateAlarmUseCase(RepositoryProvider.alarmRepository)
    }
    val toggleAlarmStateUseCase: ToggleAlarmStateUseCase by lazy {
        ToggleAlarmStateUseCase(RepositoryProvider.alarmRepository)
    }
    val fetchChecklistUseCase: FetchChecklistUseCase by lazy {
        FetchChecklistUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val unCheckBottariItemUseCase: UnCheckBottariItemUseCase by lazy {
        UnCheckBottariItemUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val checkBottariItemUseCase: CheckBottariItemUseCase by lazy {
        CheckBottariItemUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val createBottariUseCase: CreateBottariUseCase by lazy {
        CreateBottariUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val fetchBottariTemplatesUseCase: FetchBottariTemplatesUseCase by lazy {
        FetchBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val searchBottariTemplatesUseCase: SearchBottariTemplatesUseCase by lazy {
        SearchBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val createBottariTemplateUseCase: CreateBottariTemplateUseCase by lazy {
        CreateBottariTemplateUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase by lazy {
        FetchBottariTemplateDetailUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase by lazy {
        TakeBottariTemplateDetailUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val saveBottariItemsUseCase: SaveBottariItemsUseCase by lazy {
        SaveBottariItemsUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase by lazy {
        FetchMyBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase by lazy {
        DeleteMyBottariTemplateUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val deleteBottariUseCase: DeleteBottariUseCase by lazy {
        DeleteBottariUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val SaveBottariTitleUseCase: SaveBottariTitleUseCase by lazy {
        SaveBottariTitleUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val fetchBottariDetailsUseCase by lazy {
        FetchBottariDetailsUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val savePermissionFlagUseCase: SavePermissionFlagUseCase by lazy {
        SavePermissionFlagUseCase(RepositoryProvider.appConfigRepository)
    }
    val getPermissionFlagUseCase: GetPermissionFlagUseCase by lazy {
        GetPermissionFlagUseCase(RepositoryProvider.appConfigRepository)
    }
    val reportTemplateUseCase: ReportTemplateUseCase by lazy {
        ReportTemplateUseCase(RepositoryProvider.reportRepository)
    }
    val createTeamBottariUseCase: CreateTeamBottariUseCase by lazy {
        CreateTeamBottariUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamChecklistUseCase: FetchTeamChecklistUseCase by lazy {
        FetchTeamChecklistUseCase(RepositoryProvider.teamBottariRepository)
    }
    val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase by lazy {
        CheckTeamBottariItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val unCheckTeamBottariItemUseCase: UnCheckTeamBottariItemUseCase by lazy {
        UnCheckTeamBottariItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase by lazy {
        FetchTeamBottariesUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamMembersUseCase: FetchTeamMembersUseCase by lazy {
        FetchTeamMembersUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase by lazy {
        FetchTeamBottariDetailUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase by lazy {
        FetchTeamMembersStatusUseCase(RepositoryProvider.teamBottariRepository)
    }
    val saveFcmTokenUseCase: SaveFcmTokenUseCase by lazy {
        SaveFcmTokenUseCase(RepositoryProvider.fcmRepository)
    }
    val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase by lazy {
        SendRemindByMemberMessageUseCase(RepositoryProvider.teamBottariRepository)
    }
    val getMemberIdentifierUseCase: GetMemberIdentifierUseCase by lazy {
        GetMemberIdentifierUseCase(RepositoryProvider.memberRepository)
    }
}
