package com.bottari.presentation.compose.home.template

import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.domain.usecase.template.SearchTemplatesByHashtagUseCase
import com.bottari.domain.usecase.template.SearchTemplatesByTitleUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val searchTemplatesByTitleUseCase: SearchTemplatesByTitleUseCase,
    private val searchTemplatesByHashtagUseCase: SearchTemplatesByHashtagUseCase,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : FlowBaseViewModel<TemplateUiState, TemplateUiEvent>(TemplateUiState()) {
    private sealed interface Mode {
        data object Main : Mode

        data class TitleSearch(
            val query: String,
        ) : Mode

        data class ChipSearch(
            val chips: List<BottariTemplateHashtagUiModel>,
        ) : Mode
    }

    private val pages =
        mutableMapOf<Mode, Pageable<BottariTemplate>>(
            Mode.Main to Pageable(),
            Mode.TitleSearch("") to Pageable(),
        )

    private val currentMode: Mode
        get() {
            if (currentState.chips.isNotEmpty()) return Mode.ChipSearch(currentState.chips)
            if (currentState.searchWord.isNotEmpty()) return Mode.TitleSearch(currentState.searchWord)
            return Mode.Main
        }

    private var inFlight: Job? = null
    private val debouncedSearch: (Unit) -> Unit

    init {
        loadNextPage()
        loadMyTemplates()

        debouncedSearch = viewModelScope.debounce(DEBOUNCE_DELAY) { loadNextPage(reset = true) }
    }

    fun refresh(targetIsMain: Boolean) {
        resetAllPages()
        if (targetIsMain) {
            updateState { copy(isRefreshingMain = true) }
            loadNextPage(reset = true)
            return
        }
        updateState { copy(isRefreshingMy = true) }
        loadMyTemplates(refresh = true)
    }

    fun updateSearchWord(searchWord: String) {
        if (currentState.chips.isNotEmpty()) return
        updateState { copy(searchWord = searchWord) }
        debouncedSearch(Unit)
    }

    fun searchByChip(chips: List<BottariTemplateHashtagUiModel>) {
        if (currentState.chips == chips) return
        updateState { copy(searchWord = "", chips = chips) }

        if (chips.isEmpty()) {
            val main = pages[Mode.Main]?.contents.orEmpty()
            updateState { copy(templates = main.map(BottariTemplateUiModel::fromDomain)) }
            return
        }

        pages[Mode.ChipSearch(chips)] = Pageable()
        loadNextPage(reset = true)
    }

    fun loadNextPage(reset: Boolean = false) {
        if (inFlight?.isActive == true) return

        val mode = currentMode
        val pageable = if (reset) Pageable() else (pages[mode] ?: Pageable())
        if (!pageable.hasNext) return

        updateState { copy(isLoading = true) }

        inFlight =
            viewModelScope.launch(exceptionHandler) {
                fetchTemplatesByMode(mode, pageable.nextRequest())
                    .onSuccess { loaded -> handleFetchTemplateByModeSuccess(mode, pageable, loaded) }
                    .onFailure { emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure) }

                updateState { copy(isLoading = false, isRefreshingMain = false) }
                if (reset) emitEvent(TemplateUiEvent.MainTemplatesRefreshFinished)
            }
    }

    fun deleteTemplate(templateId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteMyBottariTemplateUseCase(templateId)
                .onSuccess { handleDeleteMyTemplateSuccess(templateId) }
                .onFailure { emitEvent(TemplateUiEvent.DeleteBottariTemplateFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private suspend fun fetchTemplatesByMode(
        mode: Mode,
        next: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        when (mode) {
            is Mode.Main -> searchTemplatesByTitleUseCase(query = "", pageable = next)
            is Mode.TitleSearch -> searchTemplatesByTitleUseCase(query = mode.query, pageable = next)
            is Mode.ChipSearch -> {
                val firstHashtagId = mode.chips.first().id
                searchTemplatesByHashtagUseCase(hashtagId = firstHashtagId, pageable = next)
            }
        }

    private fun handleFetchTemplateByModeSuccess(
        mode: Mode,
        pageable: Pageable<BottariTemplate>,
        loaded: Pageable<BottariTemplate>,
    ) {
        val merged = pageable.merge(loaded)
        pages[mode] = merged

        updateState {
            copy(
                templates = merged.contents.map(BottariTemplateUiModel::fromDomain),
                isFetched = true,
            )
        }

        if (mode is Mode.TitleSearch || mode is Mode.ChipSearch) {
            emitEvent(TemplateUiEvent.SearchTemplateSuccess)
        }
    }

    private fun loadMyTemplates(refresh: Boolean = false) {
        launch {
            fetchMyBottariTemplatesUseCase()
                .onSuccess { list ->
                    updateState { copy(myTemplates = list.map(BottariTemplateUiModel::fromDomain)) }
                }.onFailure { emitEvent(TemplateUiEvent.FetchBottariTemplatesFailure) }

            updateState { copy(isLoading = false, isRefreshingMy = false) }
            if (refresh) emitEvent(TemplateUiEvent.MyTemplatesRefreshFinished)
        }
    }

    private fun handleDeleteMyTemplateSuccess(templateId: Long) {
        updateState {
            copy(
                myTemplates = myTemplates.filterNot { it.id == templateId },
                templates = templates.filterNot { it.id == templateId },
            )
        }

        pages.keys.toList().forEach { key ->
            val old = pages[key] ?: return@forEach
            pages[key] = old.copy(contents = old.contents.filterNot { it.id == templateId })
        }

        emitEvent(TemplateUiEvent.DeleteBottariTemplateSuccess)
    }

    private fun resetAllPages() {
        val mode = currentMode

        if (mode == Mode.Main) {
            pages.clear()
            pages[Mode.Main] = Pageable()
            return
        }
        pages.remove(mode)
        pages[Mode.TitleSearch("")] = Pageable()
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
