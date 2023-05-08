package com.hwonchul.movie.domain.usecase.login

import io.reactivex.rxjava3.core.Completable

interface CheckUserRegistrationUseCase {

    operator fun invoke(phoneNumber: String): Completable
}