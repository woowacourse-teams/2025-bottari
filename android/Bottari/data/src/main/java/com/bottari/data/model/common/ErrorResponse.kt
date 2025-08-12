package com.bottari.data.model.common

import com.bottari.logger.BottariLogger
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody

@Serializable
data class ErrorResponse(
    val type: String,
    val status: Int,
    val detail: String,
    val title: String,
    val instance: String,
) {
    companion object {
        private val json =
            Json {
                ignoreUnknownKeys = true
            }

        fun parseErrorResponse(errorBody: ResponseBody?): ErrorResponse? {
            if (errorBody == null) return null

            return try {
                val jsonString = errorBody.string()
                json.decodeFromString(serializer(), jsonString)
            } catch (e: Exception) {
                BottariLogger.error(e.message, e)
                null
            }
        }
    }
}
