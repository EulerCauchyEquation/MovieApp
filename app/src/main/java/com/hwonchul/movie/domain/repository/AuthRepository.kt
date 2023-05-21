package com.hwonchul.movie.domain.repository

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.domain.model.PhoneAuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flow<PhoneAuthResult>
}