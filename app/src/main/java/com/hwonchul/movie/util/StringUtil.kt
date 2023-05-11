package com.hwonchul.movie.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object StringUtil {

    @JvmStatic
    fun formatDate(date: LocalDate?): String {
        return date?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            ?: "yyyy.MM.dd"
    }

    fun formatCountTime(timeMillis: Long): String {
        // 밀리초 단위를 00:00 으로 환산. ex) 1800000L -> 02:00
        val seconds = timeMillis / 1000 % 60
        val minutes = timeMillis / 1000 / 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}