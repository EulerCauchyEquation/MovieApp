package com.hwonchul.movie.data.remote.model

import com.hwonchul.movie.data.local.model.FavoritesEntity

data class FavoritesDto(
    val movieId: String,
    val userId: String,
)

fun FavoritesDto.toEntity() = FavoritesEntity(
    movieId = movieId.toInt(),
    userId = userId,
)
