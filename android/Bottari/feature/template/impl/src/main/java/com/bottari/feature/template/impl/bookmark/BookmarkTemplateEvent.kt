package com.bottari.feature.template.impl.bookmark

sealed interface BookmarkTemplateEvent {
    data object FetchBookmarkTemplateFailure : BookmarkTemplateEvent

    data object DeleteBookmarkTemplateFailure : BookmarkTemplateEvent
}
