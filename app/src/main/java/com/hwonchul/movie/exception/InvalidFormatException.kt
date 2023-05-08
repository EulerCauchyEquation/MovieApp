package com.hwonchul.movie.exception

data class InvalidFormatException(
    val errorMsg: String,
) : Exception()
