package com.hwonchul.movie.data.repository

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.data.remote.api.firebase.FirebaseAuth
import com.hwonchul.movie.domain.model.PhoneAuthResult
import com.hwonchul.movie.domain.repository.AuthRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flowable<PhoneAuthResult> {
        return firebaseAuth.verifyPhoneNumber(phoneNumber, activity, timeOutMillis, resendingToken)
    }
}