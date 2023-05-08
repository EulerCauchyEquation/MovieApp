package com.hwonchul.movie.data.remote.source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.exception.UserNotFoundException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor() : UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    override fun getUserByUid(uid: String): Single<User> {
        return Single.create { emitter ->
            db.collection(COLLECTION_USER)
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        val errorMsg = "DB 에서 사용자 정보를 찾을 수 없습니다 : [uid = $uid]"
                        Timber.d(errorMsg)
                        emitter.onError(UserNotFoundException(errorMsg))
                        return@addOnSuccessListener
                    }

                    Timber.d("DB 에서 사용자 정보 검색 성공 : [uid = $document.id]")
                    emitter.onSuccess(
                        User(
                            uid = document.id,
                            phone = document.getString(User.FIELD_PHONE_NUMBER)!!,
                            nickname = document.getString(User.FIELD_NICK_NAME),
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 uid 로 사용자 정보 검색에 실패하였습니다. : $exception")
                    emitter.onError(exception)
                }
        }
    }

    override fun getUserByPhoneNumber(phoneNumber: String): Single<User> {
        return Single.create { emitter ->
            db.collection(COLLECTION_USER)
                .whereEqualTo(User.FIELD_PHONE_NUMBER, phoneNumber)
                .get()
                .addOnSuccessListener { query ->
                    if (query.isEmpty) {
                        val errorMsg = "DB 에서 사용자 정보를 찾을 수 없습니다."
                        Timber.d(errorMsg)
                        emitter.onError(UserNotFoundException(errorMsg))
                        return@addOnSuccessListener
                    }

                    val document = query.first()
                    Timber.d("DB 에서 사용자 정보 검색 성공 : [uid = $document.id]")
                    emitter.onSuccess(
                        User(
                            uid = document.id,
                            phone = document.getString(User.FIELD_PHONE_NUMBER)!!,
                            nickname = document.getString(User.FIELD_NICK_NAME),
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 전화번호로 사용자 정보 검색에 실패하였습니다. : $exception")
                    emitter.onError(exception)
                }
        }
    }

    override fun getUserByNickname(nickname: String): Single<User> {
        return Single.create { emitter ->
            db.collection(COLLECTION_USER)
                .whereEqualTo(User.FIELD_NICK_NAME, nickname)
                .get()
                .addOnSuccessListener { query ->
                    if (query.isEmpty) {
                        val errorMsg = "DB 에서 사용자 정보를 찾을 수 없습니다."
                        Timber.d(errorMsg)
                        emitter.onError(UserNotFoundException(errorMsg))
                        return@addOnSuccessListener
                    }

                    val document = query.first()
                    Timber.d("DB 에서 사용자 정보 검색 성공 : [uid = $document.id]")
                    emitter.onSuccess(
                        User(
                            uid = document.id,
                            phone = document.getString(User.FIELD_PHONE_NUMBER)!!,
                            nickname = document.getString(User.FIELD_NICK_NAME),
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 닉네임으로 검색에 실패하였습니다. : $exception")
                    emitter.onError(exception)
                }
        }
    }

    override fun insertOrUpdateUser(user: User): Single<User> {
        return Single.create { emitter ->
            val newUser = user.toMap()
            val document = getDocumentForUser(user)

            document.set(newUser)
                .addOnSuccessListener {
                    Timber.d("DB 에 신규 등록하였습니다. (또는 업데이트)")
                    emitter.onSuccess(user.copy(uid = document.id))
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 데이터 업데이트에 실패하였습니다. : $exception")
                    emitter.onError(exception)
                }
        }
    }

    private fun getDocumentForUser(user: User): DocumentReference {
        return if (user.isTemp()) {
            db.collection(COLLECTION_USER).document()
        } else {
            db.collection(COLLECTION_USER).document(user.uid)
        }
    }

    override fun deleteUser(user: User): Completable {
        return Completable.create { emitter ->
            db.collection(COLLECTION_USER)
                .document(user.uid)
                .delete()
                .addOnSuccessListener {
                    Timber.d("DB 데이터를 삭제하였습니다. : [uid = $user.uid]")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 데이터 삭제에 실패하였습니다. : $exception")
                    emitter.onError(exception)
                }
        }
    }

    companion object {
        private const val COLLECTION_USER = "users"
    }
}