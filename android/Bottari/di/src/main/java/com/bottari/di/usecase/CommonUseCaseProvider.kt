package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.domain.usecase.notification.FetchNotificationsUseCase
import com.bottari.domain.usecase.report.ReportTemplateUseCase

object CommonUseCaseProvider {
    val savePermissionFlagUseCase: SavePermissionFlagUseCase by lazy {
        SavePermissionFlagUseCase(RepositoryProvider.appConfigRepository)
    }
    val getPermissionFlagUseCase: GetPermissionFlagUseCase by lazy {
        GetPermissionFlagUseCase(RepositoryProvider.appConfigRepository)
    }
    val reportTemplateUseCase: ReportTemplateUseCase by lazy {
        ReportTemplateUseCase(RepositoryProvider.reportRepository)
    }
    val fetchNotificationsUseCase: FetchNotificationsUseCase by lazy {
        FetchNotificationsUseCase(RepositoryProvider.bottariRepository)
    }
    val saveFcmTokenUseCase: SaveFcmTokenUseCase by lazy {
        SaveFcmTokenUseCase(RepositoryProvider.fcmRepository)
    }
    val checkForceUpdateUseCase: CheckForceUpdateUseCase by lazy {
        CheckForceUpdateUseCase(RepositoryProvider.remoteConfigRepository)
    }
    val connectTeamEventUseCase: ConnectTeamEventUseCase by lazy {
        ConnectTeamEventUseCase(RepositoryProvider.eventRepository)
    }
    val disconnectTeamEventUseCase: DisconnectTeamEventUseCase by lazy {
        DisconnectTeamEventUseCase(RepositoryProvider.eventRepository)
    }
}
