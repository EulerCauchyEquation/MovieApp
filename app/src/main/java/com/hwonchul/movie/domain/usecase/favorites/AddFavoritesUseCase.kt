package com.hwonchul.movie.domain.usecase.favorites

import com.hwonchul.movie.domain.model.Favorites

interface AddFavoritesUseCase {

    suspend operator fun invoke(favorites: Favorites): Result<Unit>
}