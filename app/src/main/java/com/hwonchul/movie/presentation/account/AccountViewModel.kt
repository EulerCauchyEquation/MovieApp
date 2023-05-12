package com.hwonchul.movie.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.account.LogOutUseCase
import com.hwonchul.movie.domain.usecase.account.WithdrawalUseCase
import com.hwonchul.movie.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
) : ViewModel() {

    private val _userInfo = MutableLiveData<AccountState>()
    val userInfo: LiveData<AccountState> = _userInfo

    private val _logoutResult = SingleLiveEvent<AccountState>()
    val logoutResult: LiveData<AccountState> = _logoutResult

    private val _withdrawalResult = SingleLiveEvent<AccountState>()
    val withdrawalResult: LiveData<AccountState> = _withdrawalResult

    private val compositeDisposable = CompositeDisposable()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        _userInfo.value = AccountState.Loading
        val disposable = getCurrentUserInfoUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 현재 내정보 가져오기
                { user -> _userInfo.value = AccountState.Success(user) },
                // 실패
                { _userInfo.value = AccountState.Failure(R.string.user_info_get_failed) }
            )
        compositeDisposable.add(disposable)
    }

    fun logOut() {
        _withdrawalResult.value = AccountState.Loading
        val disposable = logOutUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _logoutResult.value = AccountState.Success() }
        compositeDisposable.add(disposable)
    }

    fun withdrawal() {
        _withdrawalResult.value = AccountState.Loading
        val disposable = withdrawalUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 회원탈퇴 성공
                { _withdrawalResult.value = AccountState.Success() },
                // 회원탈퇴 실패
                {
                    _withdrawalResult.value =
                        AccountState.Failure(R.string.user_withdrawal_failed)
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}