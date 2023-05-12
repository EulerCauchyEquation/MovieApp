package com.hwonchul.movie.presentation.account.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.account.UpdateUserUseCase
import com.hwonchul.movie.domain.usecase.login.ValidateNickNameUseCase
import com.hwonchul.movie.domain.usecase.login.ValidateUniqueNickNameUseCase
import com.hwonchul.movie.exception.DuplicateNicknameException
import com.hwonchul.movie.util.SingleLiveEvent
import com.hwonchul.movie.util.TextFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val validateNickNameUseCase: ValidateNickNameUseCase,
    private val validateUniqueNickNameUseCase: ValidateUniqueNickNameUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : ViewModel() {

    private val _user = SingleLiveEvent<User>()
    private val _profileResult = SingleLiveEvent<ProfileState>()
    val profileResult: LiveData<ProfileState> = _profileResult

    private val _nickNameFormState: MutableLiveData<TextFormState> = SingleLiveEvent()
    val nickNameFormState: LiveData<TextFormState> = _nickNameFormState

    private var nickName: String = "이름없음"

    private val compositeDisposable = CompositeDisposable()

    init {
        getUser()
    }

    private fun getUser() {
        val disposable = getCurrentUserInfoUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user -> _user.value = user },
                { _profileResult.value = ProfileState.Failure(R.string.user_info_get_failed) }
            )
        compositeDisposable.add(disposable)
    }

    fun updateUser() {
        _profileResult.value = ProfileState.Loading
        val disposable = validateUniqueNickNameUseCase(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 중복 닉네임 체크
                {
                    _user.value?.let { currentUser ->
                        updateUserInternal(currentUser.copy(nickname = nickName))
                    } ?: run {
                        // null 이면 에러
                        _profileResult.value =
                            ProfileState.Failure(R.string.user_info_get_failed)
                    }
                },
                // 실패
                { exception -> handleNickNameException(exception) }
            )
        compositeDisposable.add(disposable)
    }

    private fun updateUserInternal(user: User) {
        val disposable = updateUserUseCase(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _profileResult.value = ProfileState.Success },
                { _profileResult.value = ProfileState.Failure(R.string.profile_update_failed) }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleNickNameException(exception: Throwable) {
        when (exception) {
            is DuplicateNicknameException -> _profileResult.value =
                ProfileState.Failure(R.string.nick_unique_invalid)
            else -> _profileResult.value = ProfileState.Failure(R.string.nick_connect_failed)
        }
    }

    fun nickNameChanged(nickName: String) {
        val disposable = validateNickNameUseCase(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 유효성 통과
                {
                    this.nickName = nickName
                    _nickNameFormState.value = TextFormState(true)
                },
                // 유효성 불합격
                { _nickNameFormState.value = TextFormState(R.string.nick_invalid) }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}