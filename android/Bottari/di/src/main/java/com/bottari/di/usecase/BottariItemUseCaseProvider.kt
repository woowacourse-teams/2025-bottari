package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.item.DeleteItemUseCase
import com.bottari.domain.usecase.item.FetchItemsUseCase
import com.bottari.domain.usecase.item.ResetItemsCheckStateUseCase
import com.bottari.domain.usecase.item.SaveItemsUseCase
import com.bottari.domain.usecase.item.UpdateItemCheckStateUseCase

object BottariItemUseCaseProvider {
    val fetchItemsUseCase: FetchItemsUseCase by lazy {
        FetchItemsUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val saveItemsUseCase: SaveItemsUseCase by lazy {
        SaveItemsUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }

    val deleteItemUseCase: DeleteItemUseCase by lazy {
        DeleteItemUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }

    val updateItemCheckStateUseCase: UpdateItemCheckStateUseCase by lazy {
        UpdateItemCheckStateUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }

    val resetItemsCheckStateUseCase: ResetItemsCheckStateUseCase by lazy {
        ResetItemsCheckStateUseCase(RepositoryProvider.bottariItemRepository)
    }
}
