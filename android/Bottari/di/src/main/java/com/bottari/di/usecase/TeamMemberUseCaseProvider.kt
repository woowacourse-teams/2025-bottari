package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.team.FetchTeamBottariMembersUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.domain.usecase.team.SendRemindByMemberMessageUseCase

object TeamMemberUseCaseProvider {
    val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase by lazy {
        SendRemindByMemberMessageUseCase(RepositoryProvider.teamBottariRepository)
    }
    val joinTeamBottariUseCase: JoinTeamBottariUseCase by lazy {
        JoinTeamBottariUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase by lazy {
        FetchTeamMembersStatusUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamStatusUseCase: FetchTeamStatusUseCase by lazy {
        FetchTeamStatusUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamMembersUseCase: FetchTeamMembersUseCase by lazy {
        FetchTeamMembersUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamBottariMembersUseCase: FetchTeamBottariMembersUseCase by lazy {
        FetchTeamBottariMembersUseCase(RepositoryProvider.teamBottariRepository)
    }
}
