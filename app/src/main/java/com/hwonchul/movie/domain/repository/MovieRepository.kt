package com.hwonchul.movie.domain.repository

import androidx.paging.PagingData
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.MovieListType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // 영화 상세 가져오기
    fun getMovieDetailById(movieId: Int): Flow<MovieDetail>

    // 영화 리스트 가져오기
    fun getAllMoviesByListType(listType: MovieListType): Flow<List<Movie>>

    // 영화 리스트 가져오기
    fun getAllMoviesByListTypeAsPaged(listType: MovieListType): Flow<PagingData<Movie>>

    // 영화 리스트를 최신 데이터로
    suspend fun refreshForMovieList(listType: MovieListType)

    // 영화 정보를 최신 데이터로
    suspend fun refreshForMovie(movieId: Int)

     fun searchMovieByKeyword(keyword : String) : Flow<PagingData<Movie>>
}