package com.hwonchul.movie.domain.usecase.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.domain.model.PhoneAuthResult
import kotlinx.coroutines.flow.Flow

interface VerifyPhoneNumberUseCase {

    suspend operator fun invoke(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken? = null,
    ): Flow<Result<PhoneAuthResult>>
}