package com.hwonchul.movie.domain.usecase.details

/**
 * 영화 동영상(예고편) 새로고침 usecase
 */
interface RefreshVideoListUseCase {

    suspend operator fun invoke(movieId: Int): Result<Unit>
}