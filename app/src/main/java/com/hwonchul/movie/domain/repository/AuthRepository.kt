package com.hwonchul.movie.domain.repository

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.domain.model.PhoneAuthResult
import io.reactivex.rxjava3.core.Flowable

interface AuthRepository {

    fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flowable<PhoneAuthResult>
}