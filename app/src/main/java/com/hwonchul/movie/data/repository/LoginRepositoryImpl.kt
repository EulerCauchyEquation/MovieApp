package com.hwonchul.movie.data.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.hwonchul.movie.data.local.source.UserLocalDataSource
import com.hwonchul.movie.data.remote.api.firebase.FirebaseAuth
import com.hwonchul.movie.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val localDataSource: UserLocalDataSource,
) : LoginRepository {

    override fun loginWithCredential(credential: PhoneAuthCredential): Completable {
        return firebaseAuth.signInWithPhoneAuth(credential)
    }

    override fun logout(): Completable {
        return Completable.merge(
            listOf(
                firebaseAuth.signOutWithPhoneAuth(),
                localDataSource.deleteUser(),
            )
        )
    }
}