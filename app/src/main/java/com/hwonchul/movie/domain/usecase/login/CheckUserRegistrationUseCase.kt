package com.hwonchul.movie.domain.usecase.login

interface CheckUserRegistrationUseCase {

    suspend operator fun invoke(phoneNumber: String): Result<Boolean>
}