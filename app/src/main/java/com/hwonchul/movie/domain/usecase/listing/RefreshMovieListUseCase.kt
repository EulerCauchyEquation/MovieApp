package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.MovieListType
import io.reactivex.rxjava3.core.Completable

/**
 * 영화 리스트 새로고침 usecase
 */
interface RefreshMovieListUseCase {

    operator fun invoke(listType: MovieListType): Completable
}