package com.hwonchul.movie.domain.usecase.login

import com.google.firebase.auth.PhoneAuthCredential
import com.hwonchul.movie.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SignInWithPhoneAuthUseCaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
) : SignInWithPhoneAuthUseCase {

    override operator fun invoke(credential: PhoneAuthCredential): Completable {
        return loginRepository.loginWithCredential(credential)
    }
}