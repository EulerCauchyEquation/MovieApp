package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : UpdateUserUseCase {

    override fun invoke(user: User): Completable {
        return userRepository.insertOrUpdate(user)
    }
}