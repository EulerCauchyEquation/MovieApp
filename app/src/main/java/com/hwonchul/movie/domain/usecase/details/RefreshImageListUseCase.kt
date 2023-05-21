package com.hwonchul.movie.domain.usecase.details

/**
 * 영화 사진들 새로고침 usecase
 */
interface RefreshImageListUseCase {

    suspend operator fun invoke(movieId: Int): Result<Unit>
}