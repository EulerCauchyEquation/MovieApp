package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class CheckUserRegistrationUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : CheckUserRegistrationUseCase {

    override suspend operator fun invoke(phoneNumber: String): Result<Boolean> =
        runCatching { userRepository.hasUserAlreadyRegisteredWithPhone(phoneNumber) }
}