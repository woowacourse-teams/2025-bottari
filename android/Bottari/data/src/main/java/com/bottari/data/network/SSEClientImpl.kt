package com.bottari.data.network

import com.bottari.data.BuildConfig
import com.bottari.data.model.sse.SSEEventState
import com.bottari.logger.BottariLogger
import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.launchdarkly.eventsource.background.BackgroundEventSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import java.net.URI
import java.util.concurrent.TimeUnit

class SSEClientImpl(
    private val client: OkHttpClient,
) : SSEClient,
    BackgroundEventHandler {
    private var backgroundEventSource: BackgroundEventSource? = null
    private lateinit var eventFlow: MutableStateFlow<SSEEventState>

    override fun onClosed() {
        BottariLogger.debug("onClosed")
    }

    override fun onOpen() {
        BottariLogger.debug("onOpen")
    }

    override fun onMessage(
        event: String?,
        messageEvent: MessageEvent?,
    ) {
        BottariLogger.debug(
            """
            $event
            $messageEvent
            """.trimIndent(),
        )
    }

    override fun onComment(comment: String?) {
        BottariLogger.debug("onComment")
    }

    override fun onError(t: Throwable?) {
        BottariLogger.debug(t!!.stackTraceToString())
    }

    override suspend fun connect(teamBottariId: Long): Flow<SSEEventState> {
        eventFlow = MutableStateFlow(SSEEventState.Empty)
        backgroundEventSource =
            BackgroundEventSource
                .Builder(this, getEventSourceBuilder(teamBottariId))
                .build()
        backgroundEventSource?.start()
        return eventFlow
    }

    override suspend fun disconnect() {
        backgroundEventSource?.close()
    }

    private fun getEventSourceBuilder(teamBottariId: Long): EventSource.Builder {
        val url = "${BuildConfig.BASE_URL}$PATH_TEAM_BOTTARIES/$teamBottariId$PATH_SSE"
        val uri = URI.create(url)
        return EventSource.Builder(
            ConnectStrategy
                .http(uri)
                .httpClient(client)
                .connectTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS),
        )
    }

    companion object {
        private const val PATH_TEAM_BOTTARIES = "/team-bottaries"
        private const val PATH_SSE = "/sse"
    }
}
