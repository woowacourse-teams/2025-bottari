package com.bottari.presentation.compose.template

import com.bottari.core.domain.model.bottari.template.HashtagName
import com.bottari.core.domain.model.bottari.template.HashtagNameError
import com.bottari.core.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.core.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class CreateTemplateViewModel @Inject constructor(
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
    private val fetchBottariesUseCase: FetchBottariesUseCase,
) : FlowBaseViewModel<CreateTemplateUiState, CreateTemplateUiEvent>(CreateTemplateUiState()) {
    init {
        fetchBottaries()
    }

    fun updateSelectedBottari(bottariId: Long) {
        val selectedBottari = currentState.myTemplates.find { it.id == bottariId } ?: return
        updateState {
            copy(
                selectedBottariTitle = selectedBottari.title,
                selectedBottariItems = selectedBottari.items.map { item -> item.name },
            )
        }
    }

    fun updateDescription(newDescription: String) {
        if (newDescription.length > 30) return
        updateState { copy(description = newDescription.normalizeWhitespace()) }
    }

    fun updateHashtag(newHashtag: String) {
        when (HashtagName.validate(newHashtag)) {
            null -> updateHashtag(newHashtag, true)
            is HashtagNameError.TooShort,
            is HashtagNameError.ContainsHangulJamo,
            -> updateHashtag(newHashtag, false)

            else -> Unit
        }
    }

    fun addHashtag() {
        val hashtag = currentState.writingHashtag
        if (checkCanAddHashtag(hashtag).not()) return

        updateState { copy(writingHashtag = "", hashtags = hashtags + hashtag) }
    }

    fun updateHashtags(newHashtags: List<String>) {
        updateState { copy(hashtags = newHashtags) }
    }

    fun createTemplate() {
        if (currentState.canCreate.not()) return

        updateState { copy(isLoading = true) }

        launch {
            createBottariTemplateUseCase(
                title = currentState.selectedBottariTitle,
                description = currentState.description,
                items = currentState.selectedBottariItems,
                hashtag = currentState.hashtags,
            ).onSuccess {
                emitEvent(CreateTemplateUiEvent.CreateTemplateSuccess)
            }.onFailure {
                emitEvent(CreateTemplateUiEvent.CreateTemplateFailure)
            }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun fetchBottaries() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariesUseCase()
                .firstOrNull()
                .orEmpty()
                .filterNot { template -> template.items.isEmpty() }
                .map(BottariDetailUiModel::fromDomain)
                .also { uiModels -> updateState { copy(myTemplates = uiModels) } }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    private fun updateHashtag(
        newHashtag: String,
        canAddHashtag: Boolean,
    ) {
        val isNotExistingHashtag = currentState.hashtags.contains(newHashtag).not()
        updateState {
            copy(
                writingHashtag = newHashtag,
                canAddHashtag = canAddHashtag && isNotExistingHashtag,
            )
        }
    }

    private fun checkCanAddHashtag(newHashtag: String): Boolean =
        runCatching {
            HashtagName.create(newHashtag)
        }.getOrNull() != null

    private fun String.normalizeWhitespace(): String = replace("\n", "").replace(Regex(" {2,}"), " ")
}
