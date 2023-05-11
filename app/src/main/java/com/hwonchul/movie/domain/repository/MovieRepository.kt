package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.MovieListType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface MovieRepository {
    // 영화 상세 가져오기
    fun getMovieDetailById(movieId: Int): Flowable<MovieDetail>

    // 영화 리스트 가져오기
    fun getAllMoviesByListType(listType: MovieListType): Flowable<List<Movie>>

    // 영화 리스트를 최신 데이터로
    fun refreshForMovieList(listType: MovieListType): Completable

    // 영화 정보를 최신 데이터로
    fun refreshForMovie(movieId: Int): Completable
}