package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface UserRepository {

    fun getUserInfo(): Flowable<User>

    fun refreshUserInfo(phoneNumber: String? = null): Completable

    fun insertOrUpdate(user: User): Completable

    fun deleteUser(): Completable

    fun hasUserAlreadyRegisteredWithPhone(phoneNumber: String): Completable

    fun doesNicknameExist(nickname: String): Completable
}