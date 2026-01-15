package com.bottari.feature.template.bookmark

sealed interface BookmarkTemplateEvent {
    data object FetchBookmarkTemplateFailure : BookmarkTemplateEvent

    data object DeleteBookmarkTemplateFailure : BookmarkTemplateEvent
}
