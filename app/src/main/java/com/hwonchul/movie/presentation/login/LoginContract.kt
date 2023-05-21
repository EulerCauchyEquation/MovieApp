package com.hwonchul.movie.presentation.login

import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.presentation.auth.PhoneAuthState
import com.hwonchul.movie.util.TextFormState

class LoginContract {

    data class LoginData(
        val verificationId: String = "noting",
        val phoneNumber: String = "00000000000",
        val phoneCountryCode: String = "+82",
        val forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null,
        val timeRemaining: Long = 0,
        val phoneNumberFormState: TextFormState = TextFormState(false),
        val smsCodeFormState: TextFormState = TextFormState(false),
        val nickNameFormState: TextFormState = TextFormState(false),
    ) : UiData

    sealed class LoginState : UiState {
        object Loading : LoginState()
        object Idle : LoginState()
        object SignUp : LoginState()
        object LoginSuccess : LoginState()
        data class PhoneAuth(val phoneAuthState: PhoneAuthState) : LoginState()
        data class Error(val message: Int) : LoginState()
    }
}