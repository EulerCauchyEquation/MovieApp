package com.hwonchul.movie.data.remote.source

import com.hwonchul.movie.domain.model.User

interface UserRemoteDataSource {

    suspend fun getUserByUid(uid: String): User

    suspend fun getUserByPhoneNumber(phoneNumber: String): User

    suspend fun getUserByNickname(nickname: String): User

    suspend fun insertOrUpdateUser(user: User): User

    suspend fun deleteUser(user: User)
}