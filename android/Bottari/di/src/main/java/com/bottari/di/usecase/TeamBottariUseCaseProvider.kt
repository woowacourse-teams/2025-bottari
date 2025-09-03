package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.domain.usecase.team.ExitTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase

object TeamBottariUseCaseProvider {
    val createTeamBottariUseCase: CreateTeamBottariUseCase by lazy {
        CreateTeamBottariUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase by lazy {
        FetchTeamBottariesUseCase(RepositoryProvider.teamBottariRepository)
    }
    val fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase by lazy {
        FetchTeamBottariDetailUseCase(RepositoryProvider.teamBottariRepository)
    }
    val exitTeamBottariUseCase: ExitTeamBottariUseCase by lazy {
        ExitTeamBottariUseCase(RepositoryProvider.teamBottariRepository)
    }
}
