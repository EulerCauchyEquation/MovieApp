package com.hwonchul.movie.presentation.viewModel.login

sealed class PhoneAuthState {
    object Loading : PhoneAuthState()
    data class VerificationCompleted(val smsCode: String?) : PhoneAuthState()
    object SmsCodeSent : PhoneAuthState()
    data class VerificationFailed(val message: Int) : PhoneAuthState()
}
