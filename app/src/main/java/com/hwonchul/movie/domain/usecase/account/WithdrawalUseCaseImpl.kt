package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class WithdrawalUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : WithdrawalUseCase {

    override suspend operator fun invoke(): Result<Unit> =
        runCatching { userRepository.deleteUser() }
}