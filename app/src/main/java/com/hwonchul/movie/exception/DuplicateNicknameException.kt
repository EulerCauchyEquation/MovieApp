package com.hwonchul.movie.exception

data class DuplicateNicknameException(
    val errorMsg: String,
) : Exception()
