package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.model.Movie

data class MovieWithFavorites(
    @Embedded val movie: MovieEntity,
    @ColumnInfo(name = "favorites") val isFavorite: Boolean,
)

fun MovieWithFavorites.toDomain(): Movie {
    return Movie(
        id = movie.id,
        title = movie.title,
        originalTitle = movie.originalTitle,
        releaseDate = movie.releaseDate,
        rating = movie.voteAverage,
        popularity = movie.popularity,
        backdrop = Image(movie.mainBackdropPath ?: ""),
        poster = Image(movie.mainPosterPath ?: ""),
        isFavorite = isFavorite,
    )
}

fun List<MovieWithFavorites>.toDomain() =
    this.map { it.toDomain() }