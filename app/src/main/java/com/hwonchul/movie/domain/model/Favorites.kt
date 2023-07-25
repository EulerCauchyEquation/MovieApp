package com.hwonchul.movie.domain.model

import com.hwonchul.movie.data.remote.model.FavoritesDto

data class Favorites(
    val movieId: Int,
    val userId: String,
    val isFavorites: Boolean = false,
)

fun Favorites.toDto() = FavoritesDto(
    userId = userId,
    movieId = movieId.toString(),
)