package com.hwonchul.movie.data.remote.source

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import com.hwonchul.movie.data.remote.model.UserDto
import com.hwonchul.movie.exception.UserNotFoundException
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRemoteDataSourceImpl @Inject constructor() : UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference

    override suspend fun getUserByUid(uid: String): UserDto =
        suspendCoroutine { continuation ->
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
                    val newUser = createUserFromDocument(document)
                    continuation.resume(newUser)
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 uid 로 사용자 정보 검색에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserDto =
        suspendCoroutine { continuation ->
            db.collection(COLLECTION_USER)
                .whereEqualTo(UserDto.FIELD_PHONE_NUMBER, phoneNumber)
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
                    val newUser = createUserFromDocument(document)
                    continuation.resume(newUser)
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 전화번호로 사용자 정보 검색에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }


    override suspend fun getUserByNickname(nickname: String): UserDto =
        suspendCoroutine { continuation ->
            db.collection(COLLECTION_USER)
                .whereEqualTo(UserDto.FIELD_NICK_NAME, nickname)
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
                    val newUser = createUserFromDocument(document)
                    continuation.resume(newUser)
                }
                .addOnFailureListener { exception ->
                    Timber.d("DB 에서 닉네임으로 검색에 실패하였습니다. : $exception")
                    continuation.resumeWithException(exception)
                }
        }

    private fun createUserFromDocument(document: DocumentSnapshot) = UserDto(
        uid = document.id,
        phone = document.getString(UserDto.FIELD_PHONE_NUMBER) ?: "",
        nickname = document.getString(UserDto.FIELD_NICK_NAME),
        userImage = document.getString(UserDto.FIELD_USER_IMAGE),
        updatedAt = document.getString(UserDto.FIELD_UPDATED_AT),
    )

    override suspend fun insertOrUpdateUser(user: UserDto): UserDto =
        suspendCoroutine { continuation ->
            if (user.userImage != null) {
                // 사용자 프로필 사진이 있다면
                // 사진을 먼저 업로드
                // 업로드한 위치를 포함한 사용자 정보로 업데이트
                val imageRef = storageRef.child("user/${user.uid}/user_profile.jpg")
                val imageUri = Uri.parse(user.userImage)

                if (imageUri.scheme == "file" || imageUri.scheme == "content") {
                    // 단, 앱 내부 주소인 경우만 이미지 업로드를 선행할 것
                    imageRef.putFile(imageUri).continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }
                        imageRef.downloadUrl
                    }.addOnSuccessListener { downloadUrl ->
                        val updatedUserWithImage = user.copy(userImage = downloadUrl.toString())
                        updateUserInDB(updatedUserWithImage, continuation)
                    }.addOnFailureListener { exception ->
                        Timber.d("Storage 에 이미지 업로드를 실패하였습니다. : $exception")
                        continuation.resumeWithException(exception)
                    }
                } else {
                    // Uri 주소가 앱 내부 경로가 아닌 경우는 서버에 저장된 URL 이므로, 업로드할 필요 없다
                    // UI에서 사용자가 이미지는 그대로 두고 나머지 정보만 변경 요청한 것
                    updateUserInDB(user, continuation)
                }
            } else {
                // 사용자 프로필 사진이 없다면,
                // 사용자 기본 정보만 업데이트
                updateUserInDB(user, continuation)
            }
        }

    private fun updateUserInDB(
        user: UserDto,
        continuation: Continuation<UserDto>
    ) {
        val document = if (user.isTemp()) {
            db.collection(COLLECTION_USER).document()
        } else {
            db.collection(COLLECTION_USER).document(user.uid)
        }

        document.set(user.toMap())
            .addOnSuccessListener {
                Timber.d("DB 에 신규 등록하였습니다. (또는 업데이트)")
                continuation.resume(user.copy(uid = document.id))
            }
            .addOnFailureListener { exception ->
                Timber.d("DB 에 데이터 업데이트를 실패하였습니다. : $exception")
                continuation.resumeWithException(exception)
            }
    }

    override suspend fun deleteUser(user: UserDto) = suspendCoroutine { continuation ->
        // 이미지 삭제 완료 후 사용자 정보 삭제
        storageRef.child("user/${user.uid}/user_profile.jpg")
            .delete()
            .addOnSuccessListener { deleteUserInDB(user, continuation) }
            .addOnFailureListener { exception ->
                Timber.d("storage 에 사용자 프로필 이미지 삭제를 실패하였습니다. : $exception")
                if (exception is StorageException && exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    // 서버 Storage 에 파일이 존재하지 않아 발생한 경우는 나머지 사용자 정보만 삭제
                    deleteUserInDB(user, continuation)
                } else {
                    continuation.resumeWithException(exception)
                }
            }
    }

    private fun deleteUserInDB(user: UserDto, continuation: Continuation<Unit>) {
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