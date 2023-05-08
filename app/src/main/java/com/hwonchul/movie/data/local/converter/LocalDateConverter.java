package com.hwonchul.movie.data.local.converter;

import androidx.annotation.Nullable;
import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ProvidedTypeConverter
public class LocalDateConverter {
    @TypeConverter
    public LocalDate stringToLocalDate(@Nullable String date) {
        return Optional.ofNullable(date)
                .map(val -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orElse(null);
    }

    @TypeConverter
    public String localDateToString(@Nullable LocalDate date) {
        return Optional.ofNullable(date)
                .map(LocalDate::toString)
                .orElse(null);
    }
}
