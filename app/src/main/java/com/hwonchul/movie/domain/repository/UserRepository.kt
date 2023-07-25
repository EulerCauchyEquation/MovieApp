package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.Favorites
import com.hwonchul.movie.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfo(): Flow<User>

    suspend fun refreshUserInfo(phoneNumber: String? = null)

    suspend fun insertOrUpdate(user: User)

    suspend fun deleteUser()

    suspend fun hasUserAlreadyRegisteredWithPhone(phoneNumber: String): Boolean

    suspend fun doesNicknameExist(nickname: String): Boolean

    fun getUserAllFavorites(): Flow<List<Favorites>>

    suspend fun refreshFavorites(user : User)

    suspend fun insertFavorites(favorites: Favorites)

    suspend fun deleteFavorites(favorites: Favorites)
}