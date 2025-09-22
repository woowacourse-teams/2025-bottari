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
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.SaveTeamBottariAssignedItemUseCase
import com.bottari.domain.usecase.team.SendRemindByItemUseCase
import com.bottari.domain.usecase.team.UncheckTeamBottariItemUseCase

object TeamBottariItemsUseCaseProvider {
    val fetchTeamChecklistUseCase: FetchTeamChecklistUseCase by lazy {
        FetchTeamChecklistUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase by lazy {
        CheckTeamBottariItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val uncheckTeamBottariItemUseCase: UncheckTeamBottariItemUseCase by lazy {
        UncheckTeamBottariItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val sendRemindByItemUseCase: SendRemindByItemUseCase by lazy {
        SendRemindByItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase by lazy {
        FetchTeamPersonalItemsUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val fetchTeamAssignedItemsUseCase: FetchTeamAssignedItemsUseCase by lazy {
        FetchTeamAssignedItemsUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase by lazy {
        FetchTeamSharedItemsUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase by lazy {
        CreateTeamSharedItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase by lazy {
        CreateTeamPersonalItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val createTeamAssignedItemUseCase: CreateTeamAssignedItemUseCase by lazy {
        CreateTeamAssignedItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase by lazy {
        DeleteTeamBottariItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val saveTeamBottariAssignedItemUseCase: SaveTeamBottariAssignedItemUseCase by lazy {
        SaveTeamBottariAssignedItemUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
    val fetchTeamStatusUseCase: FetchTeamStatusUseCase by lazy {
        FetchTeamStatusUseCase(RepositoryProvider.teamBottariItemsRepository)
    }
}
