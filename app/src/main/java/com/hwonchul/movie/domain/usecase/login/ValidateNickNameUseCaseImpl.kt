package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.exception.InvalidFormatException
import javax.inject.Inject

class ValidateNickNameUseCaseImpl @Inject constructor() : ValidateNickNameUseCase {

    override operator fun invoke(nickName: String): Result<Unit> {
        return if (nickName.matches(REGEX)) {
            Result.success(Unit)
        } else {
            Result.failure(InvalidFormatException("닉네임 형식이 올바르지 않습니다."))
        }
    }

    companion object {
        // 3 ~ 14 글자 이상인 한글, 영문, 숫자만 가능한 문자열
        val REGEX = "^[가-힣a-zA-Z\\d]{3,14}\$".toRegex()
    }
}