package com.hwonchul.movie.domain.usecase.details

/**
 * 영화상세 새로고침 usecase
 */
interface RefreshMovieUseCase {
    suspend operator fun invoke(movieId: Int): Result<Unit>
}