package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfo(): Flow<User>

    suspend fun refreshUserInfo(phoneNumber: String? = null)

    suspend fun insertOrUpdate(user: User)

    suspend fun deleteUser()

    suspend fun hasUserAlreadyRegisteredWithPhone(phoneNumber: String) : Boolean

    suspend fun doesNicknameExist(nickname: String) : Boolean
}