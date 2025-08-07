package com.bottari.logger.model

import org.json.JSONObject

data class LogEventData(
    val eventName: String,
    val params: Map<String, Any>,
) {
    fun serialize(): String =
        JSONObject(
            mapOf(
                LogEventData::eventName.name to eventName,
                LogEventData::params.name to params,
            ),
        ).toString()

    companion object {
        fun deserialize(json: String): LogEventData? =
            runCatching {
                val obj = JSONObject(json)
                val eventName = obj.getString(LogEventData::eventName.name)
                val params =
                    obj.optJSONObject(LogEventData::params.name)?.let { paramObj ->
                        paramObj.keys().asSequence().associateWith { paramObj.get(it) }
                    } ?: emptyMap()
                LogEventData(eventName, params)
            }.getOrNull()
    }
}
