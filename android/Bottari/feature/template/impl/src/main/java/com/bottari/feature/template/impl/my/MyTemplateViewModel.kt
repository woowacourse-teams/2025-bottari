package com.bottari.feature.template.impl.my

import com.bottari.core.domain.network.NetworkManager
import com.bottari.core.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.core.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.core.ui.base.NetworkBaseViewModel
import com.bottari.core.ui.model.template.BottariTemplateUiModel
import com.bottari.logger.BottariLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTemplateViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : NetworkBaseViewModel<MyTemplateUiState, MyTemplateUiEvent>(
        initialState = MyTemplateUiState(),
        networkManager = networkManager,
    ) {
    init {
        fetchMyTemplates()
    }

    fun fetchMyTemplates() {
        if (isConnected.value.not()) {
            updateState { copy(isFetched = true) }
            return
        }

        launch {
            updateState { copy(isLoading = true) }

            fetchMyBottariTemplatesUseCase()
                .onSuccess { templates ->
                    val newTemplates = templates.map(BottariTemplateUiModel::fromDomain)
                    updateState { copy(templates = newTemplates) }
                }.onFailure { exception ->
                    emitEvent(MyTemplateUiEvent.FetchBottariTemplatesFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    fun deleteTemplate(templateId: Long) {
        if (findTemplateById(templateId) == null) return

        launch {
            updateState { copy(isLoading = true) }

            deleteMyBottariTemplateUseCase(templateId)
                .onSuccess {
                    val filtered =
                        currentState.templates.filterNot { template -> template.id == templateId }
                    updateState { copy(templates = filtered) }
                }.onFailure { exception ->
                    emitEvent(MyTemplateUiEvent.DeleteTemplateFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun findTemplateById(templateId: Long): BottariTemplateUiModel? =
        currentState.templates.find { template -> template.id == templateId }
}
