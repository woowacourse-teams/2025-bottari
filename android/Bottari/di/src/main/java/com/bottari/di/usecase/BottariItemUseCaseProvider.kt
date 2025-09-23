package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.ResetBottariItemCheckStateUseCase
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.domain.usecase.item.UpdateItemCheckStateUseCase

object BottariItemUseCaseProvider {
    val fetchChecklistUseCase: FetchChecklistUseCase by lazy {
        FetchChecklistUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val saveBottariItemsUseCase: SaveBottariItemsUseCase by lazy {
        SaveBottariItemsUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }

    val updateItemCheckStateUseCase: UpdateItemCheckStateUseCase by lazy {
        UpdateItemCheckStateUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }

    val resetBottariItemCheckStateUseCase: ResetBottariItemCheckStateUseCase by lazy {
        ResetBottariItemCheckStateUseCase(RepositoryProvider.bottariItemRepository)
    }
}
