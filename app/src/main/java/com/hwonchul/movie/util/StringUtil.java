package com.hwonchul.movie.util;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class StringUtil {
    private static final int SEC = 60;
    private static final int MIN = 60;
    private static final int HOUR = 24;
    private static final int DAY = 30;
    private static final int MONTH = 12;

    public static String formatDate(@Nullable LocalDate date) {
        return Optional.ofNullable(date)
                .map(_date -> _date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .orElse("yyyy.MM.dd");
    }

    private static String formatTime(long diffTime) {
        if (diffTime < SEC) {
            return diffTime + "초 전";
        }
        diffTime /= SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime /= MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime /= HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }
        diffTime /= DAY;
        if (diffTime < MONTH) {
            return diffTime + "달 전";
        }
        diffTime /= MONTH;
        return diffTime + "년 전";
    }

    public static String formatCountTime(Long timeMillis) {
        // 밀리초 단위를 00:00 으로 환산. ex) 1800000L -> 02:00
        final long seconds = (timeMillis / 1000) % 60;
        final long minutes = (timeMillis / 1000) / 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
