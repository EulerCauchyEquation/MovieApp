package com.hwonchul.movie.data.remote.api.firebase

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hwonchul.movie.domain.model.PhoneAuthResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuth @Inject constructor() {
    private val auth = Firebase.auth

    // 이미 검증 됐다면 로그인 과정 호출 방지
    private var hasVerifiedPhoneNumber: Boolean = false

    suspend fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flow<PhoneAuthResult> =
        callbackFlow {
            hasVerifiedPhoneNumber = false
            val builder = PhoneAuthOptions.newBuilder(auth)

            // 재요청 토큰이 있다면, setting 을 해준다.
            resendingToken?.let { builder.setForceResendingToken(resendingToken) }

            val options = builder.setPhoneNumber(phoneNumber)
                .setTimeout(timeOutMillis / 1000, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // 번호인증 완료
                        //  case1. 디바이스가 자동 인증코드 입력해서 인증통과
                        //  case2. 이미 인증된 상태에서 재인증 요청
                        Timber.d("Phone Auth onVerificationCompleted (번호인증 완료) : [smsCode : ${credential.smsCode}]")
                        if (!hasVerifiedPhoneNumber) {
                            hasVerifiedPhoneNumber = true
                            trySend(PhoneAuthResult.VerificationCompleted(credential))
                            close()  /* Flow 종료 */
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // 인증 실패  (case : 네트워크 연결 이슈, 잘못된 전화번호, 전화번호 누락, 인증번호 락걸림 등등)
                        Timber.d("Phone Auth onVerificationFailed : $e")
                        close(e)
                    }

                    override fun onCodeSent(
                        verificationId: String, token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // 인증코드가 전송됐을 때 핸들링
                        Timber.d("Phone Auth onCodeSent (인증코드 전송 완료) : [verification id : $verificationId], [token : $token]")
                        trySend(PhoneAuthResult.CodeSent(verificationId, token))
                    }

                    override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                        // 인증번호 시간 초과
                        Timber.d("Phone Auth onCodeAutoRetrievalTimeOut (인증번호 시간 초과)")
                        trySend(PhoneAuthResult.CodeAutoRetrievalTimeOut(verificationId))
                        close()
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            awaitClose { }
        }

    suspend fun signInWithPhoneAuth(credential: PhoneAuthCredential) =
        suspendCancellableCoroutine { continuation ->
            auth.signInWithCredential(credential)  /* 로그인 처리 */
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("로그인 성공 : ${task.result.user?.phoneNumber}")
                        continuation.resume(Unit)
                    } else {
                        // Sign in failed, display a message and update the UI
                        // case. 인증 과요청, 인증 토큰 만료, 인증 코드 오류
                        Timber.d("로그인 실패 : ${task.exception}")
                        continuation.resumeWithException(task.exception!!)
                    }
                }
        }

    fun signOutWithPhoneAuth() {
        auth.signOut()
        Timber.d("로그아웃 하였습니다.")
    }

    suspend fun deletePhoneAuthAccount() =
        suspendCancellableCoroutine { continuation ->
            auth.currentUser?.let {
                it.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("계정 삭제 상공하였습니다.")
                        continuation.resume(Unit)
                    } else {
                        Timber.d("계정 삭제 실패하였습니다. : ${task.exception}")
                        continuation.resumeWithException(task.exception!!)
                    }
                }
            } ?: run {
                val errorMsg = "로그인 계정이 없어서 삭제할 수 없습니다."
                Timber.d(errorMsg)
                continuation.resumeWithException(Exception(errorMsg))
            }
        }
}