package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LogOutUseCaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
) : LogOutUseCase {

    override fun invoke(): Completable {
        return loginRepository.logout()
    }
}