package com.hwonchul.movie.domain.usecase.auth

interface ValidateSmsCodeUseCase {

    operator fun invoke(smsCode: String): Result<Unit>
}