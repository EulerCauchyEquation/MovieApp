package com.hwonchul.movie.domain.usecase.auth

import io.reactivex.rxjava3.core.Completable

interface ValidatePhoneNumberUseCase {

    operator fun invoke(phoneNumber: String, countryCode: String = "+82"): Completable
}