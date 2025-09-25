package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchBottariTemplatesUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase

object BottariTemplateUseCaseProvider {
    val fetchBottariTemplatesUseCase: FetchBottariTemplatesUseCase by lazy {
        FetchBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val searchBottariTemplatesUseCase: SearchBottariTemplatesUseCase by lazy {
        SearchBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val createBottariTemplateUseCase: CreateBottariTemplateUseCase by lazy {
        CreateBottariTemplateUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase by lazy {
        FetchBottariTemplateDetailUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase by lazy {
        TakeBottariTemplateDetailUseCase(
            RepositoryProvider.bottariTemplateRepository,
            RepositoryProvider.bottariRepository,
            RepositoryProvider.bottariItemRepository,
        )
    }
    val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase by lazy {
        FetchMyBottariTemplatesUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
    val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase by lazy {
        DeleteMyBottariTemplateUseCase(
            RepositoryProvider.bottariTemplateRepository,
        )
    }
}
