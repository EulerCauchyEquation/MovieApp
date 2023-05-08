package com.hwonchul.movie.domain.usecase.login

import com.google.firebase.auth.PhoneAuthCredential
import io.reactivex.rxjava3.core.Completable

interface SignInWithPhoneAuthUseCase {

    operator fun invoke(credential: PhoneAuthCredential): Completable
}