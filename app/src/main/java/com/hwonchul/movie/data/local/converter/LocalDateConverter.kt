package com.hwonchul.movie.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
class LocalDateConverter {
    @TypeConverter
    fun stringToLocalDate(date: String?): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date?.let { LocalDate.parse(date, formatter) }
    }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? {
        return date?.toString()
    }
}