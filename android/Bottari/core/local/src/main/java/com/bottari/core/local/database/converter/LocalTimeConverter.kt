package com.bottari.core.local.database.converter

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {
    @TypeConverter
    fun fromTime(time: LocalTime): String = time.toString()

    @TypeConverter
    fun toTime(value: String): LocalTime = LocalTime.parse(value)
}
