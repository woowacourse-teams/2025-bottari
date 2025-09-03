package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.domain.usecase.bottariDetail.FetchBottariDetailUseCase

object BottariUseCaseProvider {
    val fetchBottariesUseCase: FetchBottariesUseCase by lazy {
        FetchBottariesUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
    val fetchBottariDetailUseCase: FetchBottariDetailUseCase by lazy {
        FetchBottariDetailUseCase(RepositoryProvider.bottariRepository)
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
    val SaveBottariTitleUseCase: SaveBottariTitleUseCase by lazy {
        SaveBottariTitleUseCase(
            RepositoryProvider.bottariRepository,
        )
    }
}
