package com.bottari.data.common.util

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromDate(date: LocalDate?): String? = date.toString()

    @TypeConverter
    fun toDate(value: String?): LocalDate? = LocalDate.parse(value)
}
