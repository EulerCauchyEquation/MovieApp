package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.MovieDao
import com.hwonchul.movie.data.mapper.MovieMapper
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.MovieDto
import com.hwonchul.movie.data.remote.model.MovieProjectionDto
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val movieDao: MovieDao,
    private val mapper: MovieMapper
) : MovieRepository {

    override fun getMovieDetailById(movieId: Int): Flowable<MovieDetail> {
        return movieDao.findMovieDetailById(movieId).map { mapper.toMovieDetail(it) }
    }

    override fun getAllMoviesByListType(listType: MovieListType): Flowable<List<Movie>> {
        return movieDao.findAllProjectionOrderByPopularity().map { mapper.toMovies(it) }
    }

    override fun refreshForMovieList(listType: MovieListType): Completable {
        return api.getNowPlayingMovieList().flatMapCompletable { saveToLocal(it) }
    }

    override fun refreshForMovie(movieId: Int): Completable {
        return api.getMovie(movieId).flatMapCompletable { saveToLocal(it) }
    }

    private fun saveToLocal(dtos: List<MovieProjectionDto>): Completable {
        return Single.just(dtos)
            .map { mapper.toMovieProjections(it) }
            .flatMapCompletable { movieDao.upsertMovieProjections(it) }
    }

    private fun saveToLocal(dto: MovieDto): Completable {
        return Single.just(dto)
            .map { mapper.toEntity(it) }
            .flatMapCompletable { movieDao.upsertMovie(it) }
    }
}