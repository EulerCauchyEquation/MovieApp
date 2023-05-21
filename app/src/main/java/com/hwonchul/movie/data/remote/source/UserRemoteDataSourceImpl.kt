package com.hwonchul.movie.data.remote.source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.exception.UserNotFoundException
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRemoteDataSourceImpl @Inject constructor() : UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUserByUid(uid: String): User =
        suspendCancellableCoroutine { continuation ->
            // CancellableContinuation은 코루틴이 취소될 수 있는 상태
            // suspendCancellableCoroutine 은 코루틴이 일시 중단되고 나중에 resume 할 수 있도록 도와주는 함수로,
            // 일반 콜백 기반의 비동기 API 를 코루틴 스타일로 변환하는데 유용
            db.collection(COLLECTION_USER)
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        val errorMsg = "DB 에서 사용자 정보를 찾을 수 없습니다 : [uid = $uid]"
                        Timber.d(errorMsg)
                        continuation.resumeWithException(UserNotFoundException(errorMsg))
                        return@addOnSuccessListener
                    }

                    Timber.d("DB 에서 사용자 정보 검색 성공 : [uid = $document.id]")
                    val newUser = User(
                        uid = document.id,
                        phone = document.getString(User.FIELD_PHONE_NUMBER)!!,
                        nickname = document.getString(User.FIELD_NICK_NAME),
                    )
                    continuation.resume(newUser)
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 uid 로 사용자 정보 검색에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): User =
        suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_USER)
                .whereEqualTo(User.FIELD_PHONE_NUMBER, phoneNumber)
                .get()
                .addOnSuccessListener { query ->
                    if (query.isEmpty) {
                        val errorMsg = "DB 에서 사용자 정보를 찾을 수 없습니다."
                        Timber.d(errorMsg)
                        continuation.resumeWithException(UserNotFoundException(errorMsg))
                        return@addOnSuccessListener
                    }

                    val document = query.first()
                    Timber.d("DB 에서 사용자 정보 검색 성공 : [uid = $document.id]")
                    continuation.resume(
                        User(
                            uid = document.id,
                            phone = document.getString(User.FIELD_PHONE_NUMBER)!!,
                            nickname = document.getString(User.FIELD_NICK_NAME),
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 전화번호로 사용자 정보 검색에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun getUserByNickname(nickname: String): User =
        suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_USER)
                .whereEqualTo(User.FIELD_NICK_NAME, nickname)
                .get()
                .addOnSuccessListener { query ->
                    if (query.isEmpty) {
                        val errorMsg = "DB 에서 사용자 정보를 찾을 수 없습니다."
                        Timber.d(errorMsg)
                        continuation.resumeWithException(UserNotFoundException(errorMsg))
                        return@addOnSuccessListener
                    }

                    val document = query.first()
                    Timber.d("DB 에서 사용자 정보 검색 성공 : [uid = $document.id]")
                    continuation.resume(
                        User(
                            uid = document.id,
                            phone = document.getString(User.FIELD_PHONE_NUMBER)!!,
                            nickname = document.getString(User.FIELD_NICK_NAME),
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 닉네임으로 검색에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun insertOrUpdateUser(user: User): User =
        suspendCancellableCoroutine { continuation ->
            val newUser = user.toMap()
            val document = getDocumentForUser(user)

            document.set(newUser)
                .addOnSuccessListener {
                    Timber.d("DB 에 신규 등록하였습니다. (또는 업데이트)")
                    continuation.resume(user.copy(uid = document.id))
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 데이터 업데이트에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    private fun getDocumentForUser(user: User): DocumentReference {
        return if (user.isTemp()) {
            db.collection(COLLECTION_USER).document()
        } else {
            db.collection(COLLECTION_USER).document(user.uid)
        }
    }

    override suspend fun deleteUser(user: User) =
        suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_USER)
                .document(user.uid)
                .delete()
                .addOnSuccessListener {
                    Timber.d("DB 데이터를 삭제하였습니다. : [uid = $user.uid]")
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 데이터 삭제에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    companion object {
        private const val COLLECTION_USER = "users"
    }
}