package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.item.DeleteItemUseCase
import com.bottari.domain.usecase.item.FetchItemsUseCase
import com.bottari.domain.usecase.item.ResetItemsCheckStateUseCase
import com.bottari.domain.usecase.item.SaveItemUseCase
import com.bottari.domain.usecase.item.UpdateItemCheckStateUseCase

object BottariItemUseCaseProvider {
    val fetchItemsUseCase: FetchItemsUseCase by lazy {
        FetchItemsUseCase(
            RepositoryProvider.bottariItemRepository,
        )
    }
    val saveItemUseCase: SaveItemUseCase by lazy {
        SaveItemUseCase(
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
