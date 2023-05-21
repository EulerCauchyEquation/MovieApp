package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.source.UserLocalDataSource
import com.hwonchul.movie.data.remote.api.firebase.FirebaseAuth
import com.hwonchul.movie.data.remote.source.UserRemoteDataSource
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import com.hwonchul.movie.exception.UserNotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {

    override suspend fun getUserInfo(): Flow<User> {
        return localDataSource.getUser()
    }

    override suspend fun refreshUserInfo(phoneNumber: String?) {
        if (phoneNumber == null) {
            // param 이 없을 때
            // 1. local 에서 사용자 정보 찾기
            // 2. 가져온 정보로 remote 정보 불러오기
            // 3. local 에 업데이트
            val user = localDataSource.getUser().first()
            val refreshedUser = remoteDataSource.getUserByUid(user.uid)
            localDataSource.insertOrUpdate(refreshedUser)
        } else {
            // param 있을 때
            // 1. 전화번호로 사용자 정보 가져오기
            // 2. 가져온 사용자 정보 local 에 업데이트
            remoteDataSource.getUserByPhoneNumber(phoneNumber)
                .also { user -> localDataSource.insertOrUpdate(user) }
        }
    }

    override suspend fun insertOrUpdate(user: User) {
        remoteDataSource.insertOrUpdateUser(user)
            .also { insertedUser -> localDataSource.insertOrUpdate(insertedUser) }
    }

    override suspend fun deleteUser() {
        deleteDBUserData()
        firebaseAuth.deletePhoneAuthAccount()
        localDataSource.deleteUser()
    }

    private suspend fun deleteDBUserData() {
        localDataSource.getUser().first()
            .also { user -> remoteDataSource.deleteUser(user) }
    }

    override suspend fun hasUserAlreadyRegisteredWithPhone(phoneNumber: String): Boolean {
        return try {
            remoteDataSource.getUserByPhoneNumber(phoneNumber)
            true
        } catch (e: UserNotFoundException) {
            false
        } catch (e: Exception) {
            // 다른 예외 처리
            throw e
        }
    }

    override suspend fun doesNicknameExist(nickname: String): Boolean {
        return try {
            remoteDataSource.getUserByNickname(nickname)
            true
        } catch (e: UserNotFoundException) {
            false
        } catch (e: Exception) {
            throw e
        }
    }
}