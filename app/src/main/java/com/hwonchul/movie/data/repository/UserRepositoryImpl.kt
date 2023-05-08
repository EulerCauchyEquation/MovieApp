package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.source.UserLocalDataSource
import com.hwonchul.movie.data.remote.api.firebase.FirebaseAuth
import com.hwonchul.movie.data.remote.source.UserRemoteDataSource
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import com.hwonchul.movie.exception.DuplicateNicknameException
import com.hwonchul.movie.exception.UserNotFoundException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {

    override fun getUserInfo(): Flowable<User> {
        return localDataSource.getUser()
    }

    override fun refreshUserInfo(phoneNumber: String?): Completable {
        return if (phoneNumber == null) {
            // param 이 없을 때
            // 1. local 에서 사용자 정보 찾기
            // 2. 가져온 정보로 remote 정보 불러오기
            // 3. local 에 업데이트
            localDataSource.getUser().firstOrError()
                .flatMap { user -> remoteDataSource.getUserByUid(user.uid) }
                .flatMap { refreshUserData -> localDataSource.insertOrUpdate(refreshUserData) }
                .ignoreElement()  /* Single 데이터 상관없이 성공적으로 완료됐는지만 파악 */
        } else {
            // param 있을 때
            // 1. 전화번호로 사용자 정보 가져오기
            // 2. 가져온 사용자 정보 local 에 업데이트
            remoteDataSource.getUserByPhoneNumber(phoneNumber)
                .flatMap { user -> localDataSource.insertOrUpdate(user) }
                .ignoreElement()
        }
    }

    override fun insertOrUpdate(user: User): Completable {
        return remoteDataSource.insertOrUpdateUser(user)
            .flatMap { insertedUser -> localDataSource.insertOrUpdate(insertedUser) }
            .ignoreElement()
    }

    override fun deleteUser(): Completable {
        return Completable.concat(
            listOf(
                deleteDBUserData(),
                firebaseAuth.deletePhoneAuthAccount(),
                localDataSource.deleteUser(),
            )
        )
    }

    private fun deleteDBUserData(): Completable {
        return localDataSource.getUser().firstOrError()
            .flatMapCompletable { user -> remoteDataSource.deleteUser(user) }
    }

    override fun hasUserAlreadyRegisteredWithPhone(phoneNumber: String): Completable {
        return remoteDataSource.getUserByPhoneNumber(phoneNumber)
            .flatMapCompletable { Completable.complete() }
    }

    override fun doesNicknameExist(nickname: String): Completable {
        return remoteDataSource.getUserByNickname(nickname)
            .flatMapCompletable { Completable.error(DuplicateNicknameException("중복된 닉네임 입니다.")) }
            .onErrorResumeNext { error ->
                if (error is UserNotFoundException) {
                    Completable.complete()
                } else {
                    Completable.error(error)
                }
            }
    }
}