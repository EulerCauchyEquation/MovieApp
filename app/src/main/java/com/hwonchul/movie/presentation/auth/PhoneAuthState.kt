package com.hwonchul.movie.presentation.auth

sealed class PhoneAuthState {
    data class VerificationCompleted(val smsCode: String?) : PhoneAuthState()
    object SmsCodeSent : PhoneAuthState()
}
