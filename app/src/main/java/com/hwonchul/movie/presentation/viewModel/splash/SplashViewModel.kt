package com.hwonchul.movie.presentation.viewModel.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.account.RefreshUserInfoUseCase
import com.hwonchul.movie.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val refreshUserInfoUseCase: RefreshUserInfoUseCase,
) : ViewModel() {
    private val _loggedInUser = SingleLiveEvent<SplashState>()
    val loggedInUser: LiveData<SplashState> = _loggedInUser

    private val compositeDisposable = CompositeDisposable()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        _loggedInUser.value = SplashState.Loading
        val disposable = getCurrentUserInfoUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 현재 내정보 가져오기
                { refreshUser() },
                // 실패
                { _loggedInUser.value = SplashState.Failure(R.string.user_info_get_failed) }
            )
        compositeDisposable.add(disposable)
    }

    private fun refreshUser() {
        val disposable = refreshUserInfoUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 내정보 동기화하기
                { _loggedInUser.value = SplashState.Success },
                // 실패
                { _loggedInUser.value = SplashState.Failure(R.string.user_info_get_failed) }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}