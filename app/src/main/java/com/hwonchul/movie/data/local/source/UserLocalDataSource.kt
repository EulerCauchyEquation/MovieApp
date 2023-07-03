package com.hwonchul.movie.data.local.source

import com.hwonchul.movie.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    fun getUser(): Flow<UserEntity>

    // SharedPreferences는 Edit 작업은 main thread에서 블로킹되는 작업
    // 을 수행하지는 않지만, 일관성을 위해 suspend를 붙임
    suspend fun insertOrUpdate(user: UserEntity)

    suspend fun deleteUser()
}