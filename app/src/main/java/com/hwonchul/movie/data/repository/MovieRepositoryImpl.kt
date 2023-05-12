package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.MovieDao
import com.hwonchul.movie.data.local.model.toDomain
import com.hwonchul.movie.data.local.model.toDomains
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.MovieDto
import com.hwonchul.movie.data.remote.model.MovieProjectionDto
import com.hwonchul.movie.data.remote.model.toEntities
import com.hwonchul.movie.data.remote.model.toEntity
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
) : MovieRepository {

    override fun getMovieDetailById(movieId: Int): Flowable<MovieDetail> {
        return movieDao.findMovieDetailById(movieId).map { it.toDomain() }
    }

    override fun getAllMoviesByListType(listType: MovieListType): Flowable<List<Movie>> {
        return movieDao.findAllProjectionOrderByPopularity().map { it.toDomains() }
    }

    override fun refreshForMovieList(listType: MovieListType): Completable {
        return api.getNowPlayingMovieList().flatMapCompletable { saveToLocal(it) }
    }

    override fun refreshForMovie(movieId: Int): Completable {
        return api.getMovie(movieId).flatMapCompletable { saveToLocal(it) }
    }

    private fun saveToLocal(dtos: List<MovieProjectionDto>): Completable {
        return Single.just(dtos)
            .map { it.toEntities() }
            .flatMapCompletable { movieDao.upsertMovieProjections(it) }
    }

    private fun saveToLocal(dto: MovieDto): Completable {
        return Single.just(dto)
            .map { it.toEntity() }
            .flatMapCompletable { movieDao.upsertMovie(it) }
    }
}