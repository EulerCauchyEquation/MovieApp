package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import java.time.LocalDate

// 영화 부분 정보
data class MovieProjection(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "original_title") val originalTitle: String?,
    @TypeConverters(LocalDateConverter::class)
    @ColumnInfo(name = "release_date")
    val releaseDate: LocalDate?,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "popularity") val popularity: Double,
    @ColumnInfo(name = "main_poster_path") val mainPosterPath: String?,
    @ColumnInfo(name = "main_backdrop_path") val mainBackdropPath: String?,
)
