package com.bottari.di

import com.bottari.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.ResetBottariItemCheckStateUseCase
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.domain.usecase.notification.DeleteNotificationUseCase
import com.bottari.domain.usecase.notification.GetNotificationsUseCase
import com.bottari.domain.usecase.report.ReportTemplateUseCase
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.CreateTeamAssignedItemUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.domain.usecase.team.CreateTeamPersonalItemUseCase
import com.bottari.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.ExitTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamAssignedItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariMembersUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.domain.usecase.team.FetchTeamPersonalItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.domain.usecase.team.SaveTeamBottariAssignedItemUseCase
import com.bottari.domain.usecase.team.SendRemindByItemUseCase
import com.bottari.domain.usecase.team.SendRemindByMemberMessageUseCase
import com.bottari.domain.usecase.team.UnCheckTeamBottariItemUseCase

object UseCaseProvider {
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
    val saveBottariItemsUseCase: SaveBottariItemsUseCase by lazy {
        SaveBottariItemsUseCase(
            RepositoryProvider.bottariItemRepository,
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
    val getNotificationsUseCase: GetNotificationsUseCase by lazy {
        GetNotificationsUseCase(RepositoryProvider.notificationRepository)
    }
    val deleteNotificationsUseCase: DeleteNotificationUseCase by lazy {
        DeleteNotificationUseCase(RepositoryProvider.notificationRepository)
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
    val fetchTeamStatusUseCase: FetchTeamStatusUseCase by lazy {
        FetchTeamStatusUseCase(RepositoryProvider.teamBottariRepository)
    }
    val sendRemindByItemUseCase: SendRemindByItemUseCase by lazy {
        SendRemindByItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase by lazy {
        FetchTeamMembersStatusUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase by lazy {
        FetchTeamPersonalItemsUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamAssignedItemsUseCase: FetchTeamAssignedItemsUseCase by lazy {
        FetchTeamAssignedItemsUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase by lazy {
        FetchTeamSharedItemsUseCase(RepositoryProvider.teamBottariRepository)
    }
    val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase by lazy {
        CreateTeamSharedItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase by lazy {
        CreateTeamPersonalItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val createTeamAssignedItemUseCase: CreateTeamAssignedItemUseCase by lazy {
        CreateTeamAssignedItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase by lazy {
        DeleteTeamBottariItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val saveFcmTokenUseCase: SaveFcmTokenUseCase by lazy {
        SaveFcmTokenUseCase(RepositoryProvider.fcmRepository)
    }
    val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase by lazy {
        SendRemindByMemberMessageUseCase(RepositoryProvider.teamBottariRepository)
    }
    val joinTeamBottariUseCase: JoinTeamBottariUseCase by lazy {
        JoinTeamBottariUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamBottariMembersUseCase: FetchTeamBottariMembersUseCase by lazy {
        FetchTeamBottariMembersUseCase(RepositoryProvider.teamBottariRepository)
    }
    val saveTeamBottariAssignedItemUseCase: SaveTeamBottariAssignedItemUseCase by lazy {
        SaveTeamBottariAssignedItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val resetBottariItemCheckStateUseCase: ResetBottariItemCheckStateUseCase by lazy {
        ResetBottariItemCheckStateUseCase(RepositoryProvider.bottariItemRepository)
    }
    val checkForceUpdateUseCase: CheckForceUpdateUseCase by lazy {
        CheckForceUpdateUseCase(RepositoryProvider.remoteConfigRepository)
    }
    val exitTeamBottariUseCase: ExitTeamBottariUseCase by lazy {
        ExitTeamBottariUseCase(RepositoryProvider.teamBottariRepository)
    }
    val connectTeamEventUseCase: ConnectTeamEventUseCase by lazy {
        ConnectTeamEventUseCase(RepositoryProvider.eventRepository)
    }
    val disconnectTeamEventUseCase: DisconnectTeamEventUseCase by lazy {
        DisconnectTeamEventUseCase(RepositoryProvider.eventRepository)
    }
}
