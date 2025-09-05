package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.CreateTeamAssignedItemUseCase
import com.bottari.domain.usecase.team.CreateTeamPersonalItemUseCase
import com.bottari.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamAssignedItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.FetchTeamPersonalItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.domain.usecase.team.SaveTeamBottariAssignedItemUseCase
import com.bottari.domain.usecase.team.SendRemindByItemUseCase
import com.bottari.domain.usecase.team.UncheckTeamBottariItemUseCase

object TeamBottariItemUseCaseProvider {
    val fetchTeamChecklistUseCase: FetchTeamChecklistUseCase by lazy {
        FetchTeamChecklistUseCase(RepositoryProvider.teamBottariRepository)
    }
    val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase by lazy {
        CheckTeamBottariItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val uncheckTeamBottariItemUseCase: UncheckTeamBottariItemUseCase by lazy {
        UncheckTeamBottariItemUseCase(RepositoryProvider.teamBottariRepository)
    }
    val sendRemindByItemUseCase: SendRemindByItemUseCase by lazy {
        SendRemindByItemUseCase(RepositoryProvider.teamBottariRepository)
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
    val saveTeamBottariAssignedItemUseCase: SaveTeamBottariAssignedItemUseCase by lazy {
        SaveTeamBottariAssignedItemUseCase(RepositoryProvider.teamBottariRepository)
    }
}
