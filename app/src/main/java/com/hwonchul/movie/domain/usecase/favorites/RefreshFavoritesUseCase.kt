package com.hwonchul.movie.domain.usecase.favorites

import com.hwonchul.movie.domain.model.User

interface RefreshFavoritesUseCase {

    suspend operator fun invoke(user: User): Result<Unit>
}