package com.hwonchul.movie.domain.model

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class PhoneAuthResult {
    data class VerificationCompleted(val credential: PhoneAuthCredential) : PhoneAuthResult()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : PhoneAuthResult()

    data class CodeAutoRetrievalTimeOut(val verificationId: String) : PhoneAuthResult()
}
