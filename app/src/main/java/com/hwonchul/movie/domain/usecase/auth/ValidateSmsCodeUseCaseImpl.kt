package com.hwonchul.movie.domain.usecase.auth

import com.hwonchul.movie.exception.InvalidFormatException
import javax.inject.Inject

class ValidateSmsCodeUseCaseImpl @Inject constructor() : ValidateSmsCodeUseCase {

    override operator fun invoke(smsCode: String): Result<Unit> {
        return if (isValidSmsCode(smsCode)) {
            Result.success(Unit)
        } else {
            Result.failure(InvalidFormatException("올바르지 않는 smsCode 입니다. : $smsCode"))
        }
    }

    private fun isValidSmsCode(smsCode: String) = REGEX.matches(smsCode)

    companion object {
        // 숫자 6자리  ex. 012345
        val REGEX = "^\\d{6}\$".toRegex()
    }
}