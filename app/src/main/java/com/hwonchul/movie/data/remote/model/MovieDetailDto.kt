package com.hwonchul.movie.data.remote.model

import com.google.gson.annotations.SerializedName
import com.hwonchul.movie.data.local.model.MovieDetailEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MovieDetailDto(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: Any?,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("revenue") val revenue: Long,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("status") val status: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
)

fun MovieDetailDto.toEntity(): MovieDetailEntity {
    return MovieDetailEntity(
        id = id,
        title = title,
        originalTitle = originalTitle,
        releaseDate = releaseDate?.let { dateFormat(it) },
        voteAverage = voteAverage,
        popularity = popularity,
        mainPosterPath = posterPath,
        mainBackdropPath = backdropPath,
        runtime = runtime,
        overview = overview,
        status = status,
        genres = genres?.map(GenreDto::name)?.joinToString(","),
    )
}

private fun dateFormat(it: String) =
    LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))