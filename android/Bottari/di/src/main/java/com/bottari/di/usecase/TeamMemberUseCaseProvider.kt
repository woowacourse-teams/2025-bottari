package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.team.FetchTeamBottariMembersUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.domain.usecase.team.SendRemindByMemberMessageUseCase

object TeamMemberUseCaseProvider {
    val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase by lazy {
        SendRemindByMemberMessageUseCase(RepositoryProvider.teamMemberRepository)
    }
    val joinTeamBottariUseCase: JoinTeamBottariUseCase by lazy {
        JoinTeamBottariUseCase(RepositoryProvider.teamMemberRepository)
    }
    val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase by lazy {
        FetchTeamMembersStatusUseCase(RepositoryProvider.teamMemberRepository)
    }
    val fetchTeamMembersUseCase: FetchTeamMembersUseCase by lazy {
        FetchTeamMembersUseCase(RepositoryProvider.teamMemberRepository)
    }
    val fetchTeamBottariMembersUseCase: FetchTeamBottariMembersUseCase by lazy {
        FetchTeamBottariMembersUseCase(RepositoryProvider.teamMemberRepository)
    }
}
