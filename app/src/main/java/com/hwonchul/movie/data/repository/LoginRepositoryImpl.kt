package com.hwonchul.movie.data.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.hwonchul.movie.data.local.source.UserLocalDataSource
import com.hwonchul.movie.data.remote.api.firebase.FirebaseAuth
import com.hwonchul.movie.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val localDataSource: UserLocalDataSource,
) : LoginRepository {

    override suspend fun loginWithCredential(credential: PhoneAuthCredential) {
        return firebaseAuth.signInWithPhoneAuth(credential)
    }

    override suspend fun logout() {
        firebaseAuth.signOutWithPhoneAuth()
        localDataSource.deleteUser()
    }
}