package com.hwonchul.movie.data.remote.source

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserRemoteDataSource {

    fun getUserByUid(uid: String): Single<User>

    fun getUserByPhoneNumber(phoneNumber: String): Single<User>

    fun getUserByNickname(nickname: String): Single<User>

    fun insertOrUpdateUser(user: User): Single<User>

    fun deleteUser(user: User): Completable
}