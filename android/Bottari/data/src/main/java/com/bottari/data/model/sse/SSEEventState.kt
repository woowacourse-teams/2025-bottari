package com.bottari.data.model.sse

sealed class SSEEventState {
    object Empty : SSEEventState()

    object OnClosed : SSEEventState()

    object OnOpen : SSEEventState()

    data class OnMessage(
        val event: String?,
        val data: String?,
    ) : SSEEventState()

    data class OnComment(
        val comment: String,
    ) : SSEEventState()

    data class OnError(
        val exception: Throwable?,
    ) : SSEEventState()
}
