package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.MovieListType

/**
 * 영화 리스트 새로고침 usecase
 */
interface RefreshMovieListUseCase {

    suspend operator fun invoke(listType: MovieListType): Result<Unit>
}