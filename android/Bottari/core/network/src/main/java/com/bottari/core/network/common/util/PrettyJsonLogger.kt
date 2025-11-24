package com.bottari.data.common.util

import com.bottari.logger.BottariLogger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.logging.HttpLoggingInterceptor

@OptIn(ExperimentalSerializationApi::class)
class PrettyJsonLogger : HttpLoggingInterceptor.Logger {
    private val json =
        Json {
            prettyPrint = true
            prettyPrintIndent = JSON_INDENT
            isLenient = true
        }

    override fun log(message: String) {
        val trimmed = message.trim()

        when {
            trimmed.startsWith(REQUEST_PREFIX) || trimmed.startsWith(RESPONSE_PREFIX) -> {
                val isLast = isLastLine(trimmed)
                if (!isLast) BottariLogger.log(TAG, SEPARATOR)
                BottariLogger.log(TAG, trimmed)
                if (isLast) BottariLogger.log(TAG, SEPARATOR)

                if (!isLast) BottariLogger.network(trimmed)
            }

            isJson(trimmed) -> {
                try {
                    val parsed = json.parseToJsonElement(trimmed)
                    val pretty = json.encodeToString(JsonElement.serializer(), parsed)
                    BottariLogger.log(TAG, pretty)
                } catch (_: Exception) {
                    BottariLogger.log(TAG, trimmed)
                }
            }

            else -> {
                BottariLogger.log(TAG, trimmed)
            }
        }
    }

    private fun isJson(text: String): Boolean =
        (text.startsWith(JSON_OBJECT_START) && text.endsWith(JSON_OBJECT_END)) ||
            (text.startsWith(JSON_ARRAY_START) && text.endsWith(JSON_ARRAY_END))

    private fun isLastLine(text: String): Boolean =
        text.startsWith(REQUEST_END_PREFIX) ||
            text.startsWith(
                RESPONSE_END_PREFIX,
            )

    companion object {
        private const val TAG = "LOGGING_INTERCEPTOR"
        private const val JSON_INDENT = "\t"

        private const val SEPARATOR =
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

        private const val REQUEST_PREFIX = "-->"
        private const val REQUEST_END_PREFIX = "--> END"
        private const val RESPONSE_PREFIX = "<--"
        private const val RESPONSE_END_PREFIX = "<-- END"

        private const val JSON_OBJECT_START = "{"
        private const val JSON_OBJECT_END = "}"
        private const val JSON_ARRAY_START = "["
        private const val JSON_ARRAY_END = "]"
    }
}
