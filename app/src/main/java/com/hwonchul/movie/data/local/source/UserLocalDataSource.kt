package com.hwonchul.movie.data.local.source

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface UserLocalDataSource {

    fun getUser(): Flowable<User>

    fun insertOrUpdate(user: User): Single<User>

    fun deleteUser(): Completable
}