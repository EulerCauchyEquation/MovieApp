package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : UpdateUserUseCase {

    override suspend fun invoke(user: User): Result<Unit> =
        runCatching { userRepository.insertOrUpdate(user) }
}