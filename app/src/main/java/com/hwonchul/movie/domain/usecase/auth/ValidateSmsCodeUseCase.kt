package com.hwonchul.movie.domain.usecase.auth

import io.reactivex.rxjava3.core.Completable

interface ValidateSmsCodeUseCase {

    operator fun invoke(smsCode: String): Completable
}