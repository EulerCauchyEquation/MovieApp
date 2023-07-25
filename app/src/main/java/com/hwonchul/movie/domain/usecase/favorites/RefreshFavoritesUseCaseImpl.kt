package com.hwonchul.movie.domain.usecase.favorites

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class RefreshFavoritesUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : RefreshFavoritesUseCase {
    override suspend fun invoke(user: User): Result<Unit> =
        runCatching { userRepository.refreshFavorites(user) }
}