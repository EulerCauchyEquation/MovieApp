package com.hwonchul.movie.exception

data class UserNotFoundException(
    val errorMsg: String,
) : Exception()
