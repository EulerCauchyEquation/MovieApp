package com.hwonchul.movie.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import io.reactivex.rxjava3.core.Completable

interface LoginRepository {

    fun loginWithCredential(credential: PhoneAuthCredential): Completable

    fun logout(): Completable
}