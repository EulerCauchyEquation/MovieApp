package com.hwonchul.movie.data.remote.model

import com.google.gson.annotations.SerializedName
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.domain.model.MovieListType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MovieDto(
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("overview") val overview: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
)

fun MovieDto.toEntity(listType: MovieListType): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        originalTitle = originalTitle,
        releaseDate = releaseDate?.takeIf { it.isNotBlank() }?.let { dateFormat(it) },
        voteAverage = voteAverage,
        popularity = popularity,
        mainPosterPath = posterPath,
        mainBackdropPath = backdropPath,
        listType = listType,
    )
}

private fun dateFormat(date: String) =
    LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))