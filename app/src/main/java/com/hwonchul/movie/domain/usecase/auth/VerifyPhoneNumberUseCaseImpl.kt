package com.hwonchul.movie.domain.usecase.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.hwonchul.movie.domain.model.PhoneAuthResult
import com.hwonchul.movie.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VerifyPhoneNumberUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : VerifyPhoneNumberUseCase {

    override suspend operator fun invoke(
        phoneNumber: String,
        activity: Activity,
        timeOutMillis: Long,
        resendingToken: PhoneAuthProvider.ForceResendingToken?
    ): Flow<Result<PhoneAuthResult>> =
        authRepository.verifyPhoneNumber(
            phoneNumber,
            activity,
            timeOutMillis,
            resendingToken
        ).map { result -> Result.success(result) }
            .catch { e -> emit(Result.failure(e)) }
}