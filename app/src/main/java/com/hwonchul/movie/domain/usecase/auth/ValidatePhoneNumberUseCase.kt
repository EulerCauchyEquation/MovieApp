package com.hwonchul.movie.domain.usecase.auth

interface ValidatePhoneNumberUseCase {

    operator fun invoke(phoneNumber: String, countryCode: String = "+82"): Result<Unit>
}