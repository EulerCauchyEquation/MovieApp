package com.hwonchul.movie.domain.usecase.favorites

import com.hwonchul.movie.domain.model.Favorites
import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class AddFavoritesUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : AddFavoritesUseCase {
    override suspend fun invoke(favorites: Favorites): Result<Unit> =
        runCatching { userRepository.insertFavorites(favorites) }
}