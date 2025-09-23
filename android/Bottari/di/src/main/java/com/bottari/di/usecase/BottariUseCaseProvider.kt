package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariDetailUseCase
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase

object BottariUseCaseProvider {
    val fetchBottariesUseCase: FetchBottariesUseCase by lazy {
        FetchBottariesUseCase(
            bottariRepository = RepositoryProvider.bottariRepository,
            bottariItemRepository = RepositoryProvider.bottariItemRepository,
            alarmRepository = RepositoryProvider.alarmRepository,
        )
    }
    val fetchBottariDetailUseCase: FetchBottariDetailUseCase by lazy {
        FetchBottariDetailUseCase(
            bottariRepository = RepositoryProvider.bottariRepository,
            bottariItemRepository = RepositoryProvider.bottariItemRepository,
            alarmRepository = RepositoryProvider.alarmRepository,
        )
    }
    val fetchBottariDetailsUseCase by lazy {
        FetchBottariDetailsUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val createBottariUseCase: CreateBottariUseCase by lazy {
        CreateBottariUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val deleteBottariUseCase: DeleteBottariUseCase by lazy {
        DeleteBottariUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val saveBottariTitleUseCase: SaveBottariTitleUseCase by lazy {
        SaveBottariTitleUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
}
