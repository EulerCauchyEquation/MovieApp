package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import io.reactivex.rxjava3.core.Flowable

/**
 * 영화 목록 가져오기 Usecase
 */
interface GetMovieListUseCase {

    operator fun invoke(listType: MovieListType): Flowable<List<Movie>>
}