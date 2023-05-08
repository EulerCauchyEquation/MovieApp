package com.hwonchul.movie.data.local.source

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.exception.UserNotFoundException
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.*
import timber.log.Timber
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : UserLocalDataSource {
    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun getUser(): Flowable<User> {
        return Flowable.create({ emitter ->
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == USER_KEY) {
                    getUserInternal(emitter)
                }
            }

            // 최초 한 번은 실행
            getUserInternal(emitter)

            // 실시간 데이터 감지 리스너 등록
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

            emitter.setCancellable {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }, BackpressureStrategy.LATEST)
    }

    private fun getUserInternal(emitter: FlowableEmitter<User>) {
        sharedPreferences.getString(USER_KEY, null)?.let { jsonString ->
            val user = gson.fromJson(jsonString, User::class.java)
            Timber.d("사용자 정보를 가져왔습니다. : [user=${user.uid}]")
            emitter.onNext(user)
        } ?: run {
            val errorMsg = "local 에 사용자 정보가 없습니다. "
            Timber.d(errorMsg)
            if (!emitter.isCancelled) {
                emitter.onError(UserNotFoundException(errorMsg))
            }
        }
    }

    override fun insertOrUpdate(user: User): Single<User> {
        return Single.create { emitter ->
            val jsonString = gson.toJson(user)
            sharedPreferences.edit {
                putString(USER_KEY, jsonString)
                apply()
            }
            Timber.d("local 에 사용자 정보를 업데이트 하였습니다. ")
            emitter.onSuccess(user)
        }
    }

    override fun deleteUser(): Completable {
        return Completable.create { emitter ->
            sharedPreferences.edit {
                remove(USER_KEY)
                apply()
            }
            Timber.d("local 에 사용자 정보를 초기화 하였습니다. ")
            emitter.onComplete()
        }
    }

    companion object {
        private const val NAME = "user_info"
        private const val USER_KEY = "user"
    }
}