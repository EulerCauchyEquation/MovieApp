package com.hwonchul.movie.exception

data class InvalidCategoryException(
    val errorMsg: String,
) : Exception()

