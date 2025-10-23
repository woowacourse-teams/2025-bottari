package com.bottari.presentation.compose.home.template.bookmark

sealed interface BookmarkTemplateEvent {
    data object FetchBookmarkTemplateFailure : BookmarkTemplateEvent

    data object DeleteBookmarkTemplateFailure : BookmarkTemplateEvent
}
