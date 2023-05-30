package com.hwonchul.movie.domain.repository

import com.google.firebase.auth.PhoneAuthCredential

interface LoginRepository {

    suspend fun loginWithCredential(credential: PhoneAuthCredential)

    suspend fun logout()
}