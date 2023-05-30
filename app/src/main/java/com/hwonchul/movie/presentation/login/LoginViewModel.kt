package com.hwonchul.movie.presentation.login

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.PhoneAuthResult
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.usecase.account.RefreshUserInfoUseCase
import com.hwonchul.movie.domain.usecase.auth.TimerUseCase
import com.hwonchul.movie.domain.usecase.auth.ValidatePhoneNumberUseCase
import com.hwonchul.movie.domain.usecase.auth.ValidateSmsCodeUseCase
import com.hwonchul.movie.domain.usecase.auth.VerifyPhoneNumberUseCase
import com.hwonchul.movie.domain.usecase.login.CheckDuplicateNickNameUseCase
import com.hwonchul.movie.domain.usecase.login.CheckUserRegistrationUseCase
import com.hwonchul.movie.domain.usecase.login.RegisterUserUseCase
import com.hwonchul.movie.domain.usecase.login.SignInWithPhoneAuthUseCase
import com.hwonchul.movie.domain.usecase.login.ValidateNickNameUseCase
import com.hwonchul.movie.presentation.auth.PhoneAuthState
import com.hwonchul.movie.presentation.login.LoginContract.LoginData
import com.hwonchul.movie.presentation.login.LoginContract.LoginState
import com.hwonchul.movie.util.TextFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validateSmsCodeUseCase: ValidateSmsCodeUseCase,
    private val validateNickNameUseCase: ValidateNickNameUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val signInWithPhoneAuthUseCase: SignInWithPhoneAuthUseCase,
    private val checkUserRegistrationUseCase: CheckUserRegistrationUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val checkDuplicateNickNameUseCase: CheckDuplicateNickNameUseCase,
    private val refreshUserInfoUseCase: RefreshUserInfoUseCase,
    private val timerUseCase: TimerUseCase,
) : BaseViewModel<LoginData, LoginState>(LoginData(), LoginState.Idle) {

    // 국제 전화번호
    private val internationalNumber
        get() = uiData.value!!.let {
            it.phoneCountryCode +
                    if (it.phoneNumber.startsWith("0")) it.phoneNumber.substring(1)
                    else it.phoneNumber
        }

    fun requestSmsCodeToPhoneNumber(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            validatePhoneNumberUseCase(phoneNumber)
                .onSuccess {
                    setData { copy(phoneNumber = phoneNumber) }
                    withContext(Dispatchers.IO) {
                        verifyPhoneNumber(
                            internationalNumber = internationalNumber,
                            activity = activity,
                            resendingToken = uiData.value!!.forceResendingToken
                        )
                    }
                }
                .onFailure { setState { LoginState.Error(R.string.phone_number_invalid) } }
        }
    }

    private suspend fun verifyPhoneNumber(
        internationalNumber: String,
        resendingToken: PhoneAuthProvider.ForceResendingToken? = null,
        activity: Activity
    ) {
        setState { LoginState.Loading }
        verifyPhoneNumberUseCase(internationalNumber, activity, TIME_OUT, resendingToken)
            .collect { result ->
                result
                    .onSuccess { phoneAuthResult ->
                        when (phoneAuthResult) {
                            is PhoneAuthResult.VerificationCompleted -> {
                                setState {
                                    LoginState.PhoneAuth(
                                        PhoneAuthState.VerificationCompleted(
                                            phoneAuthResult.credential.smsCode
                                        )
                                    )
                                }
                                loginWithPhoneAuth(phoneAuthResult.credential)
                            }

                            is PhoneAuthResult.CodeSent -> {
                                setData {
                                    copy(
                                        verificationId = phoneAuthResult.verificationId,
                                        forceResendingToken = phoneAuthResult.token,
                                    )
                                }
                                setState { LoginState.PhoneAuth(PhoneAuthState.SmsCodeSent) }
                                startTimer()
                            }

                            is PhoneAuthResult.CodeAutoRetrievalTimeOut ->
                                setState { LoginState.Error(R.string.all_auth_time_out) }
                        }
                    }
                    .onFailure { handlePhoneAuthException(it as Exception) }
            }
    }

    // 1. 인증 성공한 번호로 기존 유저 검색
    // 2. 기존 유저가 있으면, 유저 정보 업데이트
    // 3. 없으면 신규 유저 이므로 신규 등록 진행
    private suspend fun loginWithPhoneAuth(credential: PhoneAuthCredential) {
        setState { LoginState.Loading }
        signInWithPhoneAuthUseCase(credential)
            // 인증 성공한 번호로 기존 유저 검색
            .onSuccess { syncOrRegisterUser() }
            .onFailure { handleLoginException(it as Exception) }
    }

    private suspend fun syncOrRegisterUser() {
        checkUserRegistrationUseCase(internationalNumber)
            .onSuccess { doesExist ->
                if (doesExist) {
                    // 인증 성공한 번호로 사용자 정보가 있다면 (기존 유저)
                    refreshUser(internationalNumber)
                } else {
                    // 신규 사용자인 경우
                    setState { LoginState.SignUp }
                }
            }
            .onFailure { setState { LoginState.Error(R.string.check_user_registration_failure) } }
    }

    private suspend fun refreshUser(phone: String) {
        refreshUserInfoUseCase(phone)
            .onSuccess { setState { LoginState.LoginSuccess } }
            .onFailure { setState { LoginState.Error(R.string.user_refresh_failed) } }
    }

    private fun handlePhoneAuthException(e: Exception) {
        when (e) {
            is FirebaseNetworkException ->
                // 네트워크 연결 실패
                setState { LoginState.Error(R.string.all_response_failed) }

            is FirebaseTooManyRequestsException ->
                // 과도한 인증코드 요청으로 인증 제한
                setState { LoginState.Error(R.string.phone_auth_invalid_too_many) }

            else -> setState { LoginState.Error(R.string.phone_auth_failed) }
        }
    }

    private fun handleLoginException(e: Exception) {
        when (e) {
            is FirebaseNetworkException ->
                // 네트워크 연결 실패
                setState { LoginState.Error(R.string.all_response_failed) }

            else -> setState { LoginState.Error(R.string.phone_auth_failed) }
        }
    }

    private suspend fun startTimer() {
        viewModelScope.launch {
            timerUseCase.start(TIME_OUT)
                .collect { result ->
                    result
                        .onSuccess { time -> setData { copy(timeRemaining = time) } }
                        .onFailure { }
                }
        }
    }

    fun verifyPhoneWithSmsCode(smsCode: String) {
        viewModelScope.launch {
            validateSmsCodeUseCase(smsCode)
                .onSuccess {
                    setState { LoginState.PhoneAuth(PhoneAuthState.VerificationCompleted(smsCode)) }
                    val credential =
                        PhoneAuthProvider.getCredential(uiData.value!!.verificationId, smsCode)
                    withContext(Dispatchers.IO) {
                        loginWithPhoneAuth(credential)
                    }
                }
                .onFailure { setState { LoginState.Error(R.string.sms_code_invalid) } }
        }
    }

    fun reRequestSmsCode(activity: Activity) {
        viewModelScope.launch {
            timerUseCase.stop()
            withContext(Dispatchers.IO) {
                verifyPhoneNumber(internationalNumber, uiData.value!!.forceResendingToken, activity)
            }
        }
    }

    fun registerUser(nickName: String) {
        viewModelScope.launch {
            setState { LoginState.Loading }
            checkDuplicateNickNameUseCase(nickName)
                .onSuccess { doesExist ->
                    if (doesExist) {
                        setState { LoginState.Error(R.string.nick_unique_invalid) }
                    } else {
                        withContext(Dispatchers.IO) {
                            registerInternal(nickName)
                        }
                    }
                }
                .onFailure { setState { LoginState.Error(R.string.nick_connect_failed) } }
        }
    }

    private suspend fun registerInternal(nickName: String) {
        val newUser = User(phone = internationalNumber, nickname = nickName)
        registerUserUseCase(newUser)
            .onSuccess { setState { LoginState.LoginSuccess } }
            .onFailure { setState { LoginState.Error(R.string.user_refresh_failed) } }
    }

    fun phoneNumberChanged(phoneNumber: String) {
        validatePhoneNumberUseCase(phoneNumber)
            .onSuccess { setData { copy(phoneNumberFormState = TextFormState(true)) } }
            .onFailure { setData { copy(phoneNumberFormState = TextFormState(R.string.phone_number_invalid)) } }
    }

    fun smsCodeChanged(smsCode: String) {
        validateSmsCodeUseCase(smsCode)
            .onSuccess { setData { copy(smsCodeFormState = TextFormState(true)) } }
            .onFailure { setData { copy(smsCodeFormState = TextFormState(R.string.sms_code_invalid)) } }
    }

    fun nickNameChanged(phoneNumber: String) {
        validateNickNameUseCase(phoneNumber)
            .onSuccess { setData { copy(nickNameFormState = TextFormState(true)) } }
            .onFailure { setData { copy(nickNameFormState = TextFormState(R.string.nick_invalid)) } }
    }

    override fun onCleared() {
        super.onCleared()
        timerUseCase.stop()
    }

    companion object {
        const val TIME_OUT = 120 * 1000L    /* 2분 */
    }
}