package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.repository.LoginRepository
import javax.inject.Inject

class LogOutUseCaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
) : LogOutUseCase {

    override suspend fun invoke(): Result<Unit> =
        runCatching { loginRepository.logout() }
}