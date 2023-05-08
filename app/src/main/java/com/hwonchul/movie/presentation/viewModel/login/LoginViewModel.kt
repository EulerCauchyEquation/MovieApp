package com.hwonchul.movie.presentation.viewModel.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.model.PhoneAuthResult
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.usecase.login.CheckUserRegistrationUseCase
import com.hwonchul.movie.domain.usecase.account.RefreshUserInfoUseCase
import com.hwonchul.movie.domain.usecase.auth.*
import com.hwonchul.movie.domain.usecase.login.RegisterUserUseCase
import com.hwonchul.movie.domain.usecase.login.SignInWithPhoneAuthUseCase
import com.hwonchul.movie.domain.usecase.login.ValidateNickNameUseCase
import com.hwonchul.movie.domain.usecase.login.ValidateUniqueNickNameUseCase
import com.hwonchul.movie.exception.DuplicateNicknameException
import com.hwonchul.movie.util.SingleLiveEvent
import com.hwonchul.movie.presentation.viewModel.model.TextFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
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
    private val validateUniqueNickNameUseCase: ValidateUniqueNickNameUseCase,
    private val refreshUserInfoUseCase: RefreshUserInfoUseCase,
    private val timerUseCase: TimerUseCase,
) : ViewModel() {

    private val _loginResult = SingleLiveEvent<LoginState>()
    val loginResult: LiveData<LoginState> = _loginResult

    private val _verificationResult = SingleLiveEvent<PhoneAuthState>()
    val verificationResult: LiveData<PhoneAuthState> = _verificationResult

    private val _timeRemaining = MutableLiveData<Long>()
    val timeRemaining: LiveData<Long> = _timeRemaining

    private val _phoneNumberFormState: MutableLiveData<TextFormState> = SingleLiveEvent()
    val phoneNumberFormState: LiveData<TextFormState> = _phoneNumberFormState

    private val _smsCodeFormState: MutableLiveData<TextFormState> = SingleLiveEvent()
    val smsCodeFormState: LiveData<TextFormState> = _smsCodeFormState

    private val _nickNameFormState: MutableLiveData<TextFormState> = SingleLiveEvent()
    val nickNameFormState: LiveData<TextFormState> = _nickNameFormState

    private var verificationId: String = "noting"
    private var phoneNumber: String = "00000000000"
    private var phoneCountryCode: String = "+82"

    // 국제 전화번호
    private val internationalNumber
        get() = phoneCountryCode + if (phoneNumber.startsWith("0")) phoneNumber.substring(1) else phoneNumber
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private val compositeDisposable = CompositeDisposable()

    fun requestSmsCodeToPhoneNumber(phoneNumber: String, activity: Activity) {
        val disposable = validatePhoneNumberUseCase(phoneNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유효성 통과
                {
                    this.phoneNumber = phoneNumber
                    verifyPhoneNumber(
                        internationalNumber = internationalNumber,
                        activity = activity,
                        resendingToken = forceResendingToken
                    )
                },
                // 유효성 불합격
                {
                    _verificationResult.value =
                        PhoneAuthState.VerificationFailed(R.string.phone_number_invalid)
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun verifyPhoneNumber(
        internationalNumber: String,
        resendingToken: PhoneAuthProvider.ForceResendingToken? = null,
        activity: Activity
    ) {
        _verificationResult.value = PhoneAuthState.Loading
        val disposable =
            verifyPhoneNumberUseCase(internationalNumber, activity, TIME_OUT, resendingToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        when (result) {
                            is PhoneAuthResult.VerificationCompleted -> {
                                _verificationResult.value =
                                    PhoneAuthState.VerificationCompleted(result.credential.smsCode)
                                loginWithPhoneAuth(result.credential)
                            }
                            is PhoneAuthResult.CodeSent -> {
                                this.verificationId = result.verificationId
                                this.forceResendingToken = result.token
                                _verificationResult.value = PhoneAuthState.SmsCodeSent
                                startTimer()
                            }
                            is PhoneAuthResult.CodeAutoRetrievalTimeOut ->
                                _verificationResult.value =
                                    PhoneAuthState.VerificationFailed(R.string.all_auth_time_out)
                        }
                    },
                    // 실패 시
                    { handlePhoneAuthException(it as FirebaseException) }
                )
        compositeDisposable.add(disposable)
    }

    // 1. 인증 성공한 번호로 기존 유저 검색
    // 2. 기존 유저가 있으면, 유저 정보 업데이트
    // 3. 없으면 신규 유저 이므로 신규 등록 진행
    private fun loginWithPhoneAuth(credential: PhoneAuthCredential) {
        _loginResult.value = LoginState.Loading
        val disposable = signInWithPhoneAuthUseCase(credential)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 인증 성공한 번호로 기존 유저 검색
                { syncOrRegisterUser() },
                // 실패
                { exception -> handleLoginException(exception as FirebaseException) }
            )
        compositeDisposable.add(disposable)
    }

    private fun syncOrRegisterUser() {
        val disposable = checkUserRegistrationUseCase(internationalNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 인증 성공한 번호로 사용자 정보가 있다면 (기존 유저)
                { refreshUser(internationalNumber) },
                // 신규 사용자인 경우
                { _loginResult.value = LoginState.SignUp }
            )
        compositeDisposable.add(disposable)
    }

    private fun refreshUser(phone: String) {
        val disposable = refreshUserInfoUseCase(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유저 정보 업데이트 성공
                { _loginResult.value = LoginState.LoginSuccess },
                // 실패
                { _loginResult.value = LoginState.Failure(R.string.user_refresh_failed) }
            )
        compositeDisposable.add(disposable)
    }

    private fun handlePhoneAuthException(e: FirebaseException) {
        when (e) {
            is FirebaseNetworkException ->
                // 네트워크 연결 실패
                _verificationResult.value =
                    PhoneAuthState.VerificationFailed(R.string.all_response_failed)
            is FirebaseTooManyRequestsException ->
                // 과도한 인증코드 요청으로 인증 제한
                _verificationResult.value =
                    PhoneAuthState.VerificationFailed(R.string.phone_auth_invalid_too_many)
            else -> _verificationResult.value =
                PhoneAuthState.VerificationFailed(R.string.phone_auth_invalid)
        }
    }

    private fun handleLoginException(e: FirebaseException) {
        when (e) {
            is FirebaseNetworkException ->
                // 네트워크 연결 실패
                _loginResult.value =
                    LoginState.Failure(R.string.all_response_failed)
            else -> _loginResult.value = LoginState.Failure(R.string.phone_auth_invalid)
        }
    }

    private fun startTimer() {
        val disposable = timerUseCase.start(TIME_OUT)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { time -> _timeRemaining.value = time }
        compositeDisposable.add(disposable)
    }

    fun verifyPhoneWithSmsCode(smsCode: String) {
        // 입력받은 인증코드 인증하기
        val disposable = validateSmsCodeUseCase(smsCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유효성 통과
                {
                    _verificationResult.value =
                        PhoneAuthState.VerificationCompleted(smsCode)
                    val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
                    loginWithPhoneAuth(credential)
                },
                // 유효성 불합격
                {
                    _verificationResult.value =
                        PhoneAuthState.VerificationFailed(R.string.sms_code_invalid)
                }
            )
        compositeDisposable.add(disposable)
    }

    fun reRequestSmsCode(activity: Activity) {
        timerUseCase.stop()
        verifyPhoneNumber(internationalNumber, forceResendingToken, activity)
    }

    fun registerUser(nickName: String) {
        _loginResult.value = LoginState.Loading
        val disposable = validateUniqueNickNameUseCase(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 중복 닉네임 체크
                { registerInternal(nickName) },
                // 실패
                { exception -> handleNickNameException(exception) }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleNickNameException(exception: Throwable) {
        when (exception) {
            is DuplicateNicknameException -> _loginResult.value =
                LoginState.Failure(R.string.nick_unique_invalid)
            else -> _loginResult.value = LoginState.Failure(R.string.nick_connect_failed)
        }
    }

    private fun registerInternal(nickName: String) {
        val newUser = User(phone = internationalNumber, nickname = nickName)
        val disposable = registerUserUseCase(newUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유저 정보 업데이트 성공
                { _loginResult.value = LoginState.LoginSuccess },
                // 실패
                { _loginResult.value = LoginState.Failure(R.string.user_refresh_failed) }
            )
        compositeDisposable.add(disposable)
    }

    fun phoneNumberChanged(phoneNumber: String) {
        val disposable = validatePhoneNumberUseCase(phoneNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유효성 통과
                { _phoneNumberFormState.value = TextFormState(true) },
                // 유효성 불합격
                { _phoneNumberFormState.value = TextFormState(R.string.phone_number_invalid) }
            )
        compositeDisposable.add(disposable)
    }

    fun smsCodeChanged(smsCode: String) {
        val disposable = validateSmsCodeUseCase(smsCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유효성 통과
                { _smsCodeFormState.value = TextFormState(true) },
                // 유효성 불합격
                { _smsCodeFormState.value = TextFormState(R.string.sms_code_invalid) }
            )
        compositeDisposable.add(disposable)
    }

    fun nickNameChanged(phoneNumber: String) {
        val disposable = validateNickNameUseCase(phoneNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유효성 통과
                { _nickNameFormState.value = TextFormState(true) },
                // 유효성 불합격
                { _nickNameFormState.value = TextFormState(R.string.nick_invalid) }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        timerUseCase.stop()
        compositeDisposable.clear()
    }

    companion object {
        const val TIME_OUT = 120 * 1000L    /* 2분 */
    }
}