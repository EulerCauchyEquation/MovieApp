package com.hwonchul.movie.domain.usecase.login

import com.google.firebase.auth.PhoneAuthCredential

interface SignInWithPhoneAuthUseCase {

    suspend operator fun invoke(credential: PhoneAuthCredential): Result<Unit>
}