package com.bottari.data.common.util

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toDate(value: String?): LocalDate? = value?.takeIf { it != NULL_STRING }?.let(LocalDate::parse)

    companion object {
        private const val NULL_STRING = "null"
    }
}
