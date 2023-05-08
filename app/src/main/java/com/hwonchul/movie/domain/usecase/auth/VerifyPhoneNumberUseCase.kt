package com.hwonchul.movie.domain.usecase.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.domain.model.PhoneAuthResult
import io.reactivex.rxjava3.core.Flowable

interface VerifyPhoneNumberUseCase {

    operator fun invoke(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken? = null,
    ) : Flowable<PhoneAuthResult>
}