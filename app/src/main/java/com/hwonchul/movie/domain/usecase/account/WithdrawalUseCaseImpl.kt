package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class WithdrawalUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : WithdrawalUseCase {

    override operator fun invoke(): Completable {
        return userRepository.deleteUser()
    }
}