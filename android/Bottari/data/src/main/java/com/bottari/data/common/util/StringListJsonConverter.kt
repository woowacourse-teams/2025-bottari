package com.bottari.data.common.util

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class StringListJsonConverter {
    private val serializer = ListSerializer(String.serializer())

    @TypeConverter
    fun toJson(value: List<String>): String = RoomJson.encodeToString(serializer, value)

    @TypeConverter
    fun fromJson(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        if (raw.trim().startsWith("[")) {
            return runCatching { RoomJson.decodeFromString(serializer, raw) }
                .getOrElse { emptyList() }
        }
        return raw
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    companion object {
        val RoomJson: Json =
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = false
                explicitNulls = false
            }
    }
}
