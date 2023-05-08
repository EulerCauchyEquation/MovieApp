package com.hwonchul.movie.util

/**
 * 비동기 결과 관찰자용
 */
sealed class Result<out T> {
    // Success sub-class
    data class Success<T>(val data: T) : Result<T>()

    // Error sub-class
    data class Error(val error: Int) : Result<Nothing>()

    // Loading sub-class
    class Loading : Result<Nothing>()
}