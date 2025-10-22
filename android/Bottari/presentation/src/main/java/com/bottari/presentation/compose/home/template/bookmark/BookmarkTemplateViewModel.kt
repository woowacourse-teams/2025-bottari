package com.bottari.presentation.compose.home.template.bookmark

import com.bottari.presentation.common.base.FlowBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkTemplateViewModel @Inject constructor() :
    FlowBaseViewModel<BookmarkTemplateUiState, BookmarkTemplateEvent>(
        BookmarkTemplateUiState(),
    )
