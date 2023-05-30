package com.hwonchul.movie.domain.usecase.auth

import com.hwonchul.movie.exception.InvalidCategoryException
import com.hwonchul.movie.exception.InvalidFormatException
import timber.log.Timber
import javax.inject.Inject

class ValidatePhoneNumberUseCaseImpl @Inject constructor() : ValidatePhoneNumberUseCase {

    override operator fun invoke(phoneNumber: String, countryCode: String): Result<Unit> {
        return if (countryCode == "+82") {
            if (isValidPhoneNumber(phoneNumber)) {
                Result.success(Unit)
            } else {
                Result.failure(InvalidFormatException("해당 전화번호는 올바르지 않는 양식입니다. : $phoneNumber"))
            }
        } else {
            val errorMessage = "해당 국가코드는 없는 국코드입니다. : $countryCode"
            Timber.d(errorMessage)
            Result.failure(InvalidCategoryException(errorMessage))
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String) = KR_REGEX.matches(phoneNumber)

    companion object {
        // 전화번호 기준 ex. 01012345678
        val KR_REGEX = "^010\\d{8}$".toRegex()
    }
}