package com.hwonchul.movie.data.local.source

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.hwonchul.movie.data.local.model.UserEntity
import com.hwonchul.movie.exception.UserNotFoundException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : UserLocalDataSource {
    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun getUser(): Flow<UserEntity> = callbackFlow {
        // 일반 비동기 API를 Flow 스타일로 변환 시 callbackFlow가 유용
        // 콜백 해제할 때도 유용
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == USER_KEY) {
                try {
                    trySend(getUserInternal())
                } catch (e: NullPointerException) {
                    val errorMsg = "local 에 사용자 정보가 없습니다. "
                    Timber.d(errorMsg)
                    close(UserNotFoundException(errorMsg))
                }
            }
        }

        // 실시간 데이터 감지 리스너 등록
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // 최초 한 번은 실행
        try {
            trySend(getUserInternal())
        } catch (e: NullPointerException) {
            val errorMsg = "local 에 사용자 정보가 없습니다. "
            Timber.d(errorMsg)
            close(UserNotFoundException(errorMsg))
        }

        // Flow 중단 시 리스너 해제
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    private fun getUserInternal(): UserEntity {
        val jsonString = sharedPreferences.getString(USER_KEY, null)
        return gson.fromJson(jsonString, UserEntity::class.java)
            .also { Timber.d("사용자 정보를 가져왔습니다. : [user=${it.uid}]") }
    }

    override suspend fun insertOrUpdate(user: UserEntity) {
        val jsonString = gson.toJson(user)
        sharedPreferences.edit {
            putString(USER_KEY, jsonString)
            apply()
        }
        Timber.d("local 에 사용자 정보를 업데이트 하였습니다. ")
    }

    override suspend fun deleteUser() {
        sharedPreferences.edit {
            remove(USER_KEY)
            apply()
        }
        Timber.d("local 에 사용자 정보를 초기화 하였습니다. ")
    }

    companion object {
        private const val NAME = "user_info"
        private const val USER_KEY = "user"
    }
}