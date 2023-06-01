package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.MovieDao
import com.hwonchul.movie.data.local.dao.MovieDetailDao
import com.hwonchul.movie.data.local.model.toDomain
import com.hwonchul.movie.data.local.model.toDomains
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.toEntity
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao,
) : MovieRepository {

    override fun getMovieDetailById(movieId: Int): Flow<MovieDetail> {
        return movieDetailDao.findMovieDetailById(movieId).map { it.toDomain() }
    }

    override fun getAllMoviesByListType(listType: MovieListType): Flow<List<Movie>> {
        return movieDao.findAllMovieOrderByPopularity(listType).map { it.toDomains() }
    }

    override suspend fun refreshForMovieList(listType: MovieListType) {
        val entities = api.getMovieList(listType).map { it.toEntity(listType) }
        movieDao.upsert(entities)
    }

    override suspend fun refreshForMovie(movieId: Int) {
        val entity = api.getMovie(movieId).toEntity()
        movieDetailDao.upsert(entity)
    }
}