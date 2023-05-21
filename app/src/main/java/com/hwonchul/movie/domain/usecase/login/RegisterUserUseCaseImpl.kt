package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : RegisterUserUseCase {

    override suspend operator fun invoke(user: User): Result<Unit> =
        kotlin.runCatching { userRepository.insertOrUpdate(user) }
}