package com.hwonchul.movie.data.remote.source

import com.hwonchul.movie.data.remote.model.UserDto

interface UserRemoteDataSource {

    suspend fun getUserByUid(uid: String): UserDto

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserDto

    suspend fun getUserByNickname(nickname: String): UserDto

    suspend fun insertOrUpdateUser(user: UserDto): UserDto

    suspend fun deleteUser(user: UserDto)
}