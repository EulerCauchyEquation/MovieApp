package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.model.Movie
import java.time.LocalDate

// 영화 부분 정보
@Entity(tableName = MovieEntity.TABLE_NAME)
data class MovieEntity(
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
    @ColumnInfo(name = "keyword", collate = ColumnInfo.NOCASE) val keyword: String? = null,
) {
    companion object {
        const val TABLE_NAME = "movie"
    }
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        originalTitle = originalTitle,
        releaseDate = releaseDate,
        rating = voteAverage,
        popularity = popularity,
        backdrop = Image(mainBackdropPath ?: ""),
        poster = Image(mainPosterPath ?: ""),
    )
}

fun List<MovieEntity>.toDomains(): List<Movie> {
    return this.map { it.toDomain() }
}