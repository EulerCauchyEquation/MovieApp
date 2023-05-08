package com.hwonchul.movie.domain.usecase.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.domain.model.PhoneAuthResult
import com.hwonchul.movie.domain.repository.AuthRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class VerifyPhoneNumberUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : VerifyPhoneNumberUseCase {

    override operator fun invoke(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flowable<PhoneAuthResult> {
        return authRepository.verifyPhoneNumber(
            phoneNumber,
            activity,
            timeOutMillis,
            resendingToken
        )
    }
}