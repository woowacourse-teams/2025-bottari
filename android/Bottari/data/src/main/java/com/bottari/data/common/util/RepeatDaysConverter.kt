package com.bottari.data.common.util

import androidx.room.TypeConverter

class RepeatDaysConverter {
    @TypeConverter
    fun fromRepeatDays(repeatDays: List<Int>): String = repeatDays.joinToString()

    @TypeConverter
    fun toRepeatDays(value: String): List<Int> {
        if (value.isEmpty()) return emptyList()
        return value.split(NUMBER_DELIMITER).map { it.trim().toInt() }
    }

    companion object {
        private const val NUMBER_DELIMITER = ","
    }
}
