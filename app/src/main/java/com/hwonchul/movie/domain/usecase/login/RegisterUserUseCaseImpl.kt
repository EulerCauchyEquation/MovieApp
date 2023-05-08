package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : RegisterUserUseCase {

    override operator fun invoke(user: User): Completable {
        return userRepository.insertOrUpdate(user)
    }
}