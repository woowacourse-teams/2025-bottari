package com.bottari.data.network

import com.bottari.data.BuildConfig
import com.bottari.data.model.sse.EventStateResponse
import com.bottari.data.model.sse.OnEventRaw
import com.bottari.data.model.sse.toEvent
import com.bottari.logger.BottariLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources

class SSEClientImpl(
    private val client: OkHttpClient,
) : EventSourceListener(),
    SSEClient {
    private var eventSource: EventSource? = null

    private val eventFlow: MutableStateFlow<EventStateResponse> =
        MutableStateFlow(
            EventStateResponse.Empty,
        )
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

    override fun onOpen(
        eventSource: EventSource,
        response: Response,
    ) {
        super.onOpen(eventSource, response)
        eventFlow.value = EventStateResponse.OnOpen
    }

    override fun onEvent(
        eventSource: EventSource,
        id: String?,
        type: String?,
        data: String,
    ) {
        super.onEvent(eventSource, id, type, data)
        runCatching {
            val rawEvent = json.decodeFromString(OnEventRaw.serializer(), data)
            rawEvent.toEvent(json)
        }.onSuccess { event ->
            eventFlow.value = event
        }.onFailure { exception ->
            BottariLogger.error(exception.message, exception)
            eventFlow.value = EventStateResponse.OnFailure(exception)
        }
    }

    override fun onFailure(
        eventSource: EventSource,
        t: Throwable?,
        response: Response?,
    ) {
        super.onFailure(eventSource, t, response)
        eventFlow.value = EventStateResponse.OnFailure(t)
        t?.let { exception ->
            BottariLogger.error(exception.message, exception)
        }
    }

    override fun onClosed(eventSource: EventSource) {
        super.onClosed(eventSource)
        eventFlow.value = EventStateResponse.OnClosed
    }

    override fun connect(teamBottariId: Long): Flow<EventStateResponse> {
        disconnect()
        val request = createRequest(teamBottariId)
        eventSource = createEventSource(request)
        return eventFlow
    }

    override fun disconnect() {
        eventSource?.cancel()
        eventSource = null
    }

    private fun createRequest(teamBottariId: Long): Request =
        Request
            .Builder()
            .url(getUrl(teamBottariId))
            .get()
            .build()

    private fun createEventSource(request: Request): EventSource =
        EventSources
            .createFactory(client)
            .newEventSource(request, this)

    private fun getUrl(teamBottariId: Long): HttpUrl =
        BuildConfig.BASE_URL
            .toHttpUrl()
            .newBuilder()
            .addPathSegment(PATH_TEAM_BOTTARIES)
            .addPathSegment(teamBottariId.toString())
            .addPathSegment(PATH_SSE)
            .build()

    companion object {
        private const val PATH_TEAM_BOTTARIES = "team-bottaries"
        private const val PATH_SSE = "sse"
    }
}
