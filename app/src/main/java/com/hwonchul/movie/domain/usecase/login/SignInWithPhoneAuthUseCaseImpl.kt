package com.hwonchul.movie.domain.usecase.login

import com.google.firebase.auth.PhoneAuthCredential
import com.hwonchul.movie.domain.repository.LoginRepository
import javax.inject.Inject

class SignInWithPhoneAuthUseCaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
) : SignInWithPhoneAuthUseCase {

    override suspend operator fun invoke(credential: PhoneAuthCredential): Result<Unit> =
        runCatching { loginRepository.loginWithCredential(credential) }
}