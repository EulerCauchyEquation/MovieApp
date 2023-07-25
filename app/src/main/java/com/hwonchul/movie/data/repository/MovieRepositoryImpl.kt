package com.hwonchul.movie.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.dao.MovieDao
import com.hwonchul.movie.data.local.dao.MovieDetailDao
import com.hwonchul.movie.data.local.model.toDomain
import com.hwonchul.movie.data.paging.MovieListRemoteMediator
import com.hwonchul.movie.data.paging.MovieSearchRemoteMediator
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.api.tmdb.TMDBService
import com.hwonchul.movie.data.remote.model.toEntity
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: TMDBService,
    private val api: TMDBApi,
    private val db: MovieDatabase,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao,
) : MovieRepository {

    override fun getMovieDetailById(movieId: Int): Flow<MovieDetail> {
        return movieDetailDao.findMovieDetailById(movieId).map { it.toDomain() }
    }

    override fun getAllMoviesByListType(listType: MovieListType): Flow<List<Movie>> {
        return when (listType) {
            MovieListType.NowPlaying ->
                movieDao.findAllReleasedMoviesOrderByPopularity().map { it.toDomain() }

            MovieListType.UpComing ->
                movieDao.findAllUnreleasedMoviesOrderByPopularity().map { it.toDomain() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllMoviesByListTypeAsPaged(listType: MovieListType): Flow<PagingData<Movie>> {
        val flow = when (listType) {
            MovieListType.NowPlaying -> {
                Pager(
                    config = PagingConfig(pageSize = PAGE_SIZE),
                    remoteMediator = MovieListRemoteMediator(service, db, listType)
                ) { movieDao.findAllPagedReleasedMoviesOrderByPopularity() }.flow
            }

            MovieListType.UpComing -> {
                Pager(
                    config = PagingConfig(pageSize = PAGE_SIZE),
                    remoteMediator = MovieListRemoteMediator(service, db, listType)
                ) { movieDao.findAllPagedUnreleasedMoviesOrderByPopularity() }.flow
            }
        }

        return flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun refreshForMovieList(listType: MovieListType) {
        when (listType) {
            MovieListType.NowPlaying -> movieDao.deleteByReleased()
            MovieListType.UpComing -> movieDao.deleteByUnreleased()
        }
        val entities = api.getMovieList(listType).map { it.toEntity() }
        movieDao.upsert(entities)
    }

    override suspend fun refreshForMovie(movieId: Int) {
        val entity = api.getMovie(movieId).toEntity()
        movieDetailDao.upsert(entity)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchMovieByKeyword(keyword: String): Flow<PagingData<Movie>> {
        val flow = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = MovieSearchRemoteMediator(db, service, keyword)
        ) { movieDao.findAllPagedMoviesByKeyword(keyword) }.flow

        return flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    companion object {
        // 서버의 페이지 단위로 세팅
        private const val PAGE_SIZE = 20
    }
}