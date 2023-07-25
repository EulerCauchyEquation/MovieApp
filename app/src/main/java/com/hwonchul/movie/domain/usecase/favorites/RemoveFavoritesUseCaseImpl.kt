package com.hwonchul.movie.domain.usecase.favorites

import com.hwonchul.movie.domain.model.Favorites
import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class RemoveFavoritesUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : RemoveFavoritesUseCase {
    override suspend fun invoke(favorites: Favorites): Result<Unit> =
        kotlin.runCatching { userRepository.deleteFavorites(favorites) }
}