package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CheckUserRegistrationUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : CheckUserRegistrationUseCase {

    override operator fun invoke(phoneNumber: String): Completable {
        return userRepository.hasUserAlreadyRegisteredWithPhone(phoneNumber)
    }
}