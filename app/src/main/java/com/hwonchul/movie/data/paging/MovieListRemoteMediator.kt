package com.hwonchul.movie.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieListRemoteKeyEntity
import com.hwonchul.movie.data.remote.api.tmdb.TMDBService
import com.hwonchul.movie.data.remote.model.toEntity
import com.hwonchul.movie.domain.model.MovieListType

@OptIn(ExperimentalPagingApi::class)
class MovieListRemoteMediator(
    private val api: TMDBService,
    private val db: MovieDatabase,
    private val listType: MovieListType,
) : RemoteMediator<Int, MovieEntity>() {
    private val movieDao = db.movieDao()
    private val remoteKeyDao = db.movieListRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> TMDBService.INITIAL_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val remoteKey = remoteKeyDao.getRemoteKeyByListType(listType)
                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(true)
                    }
                    remoteKey.nextPageKey
                }
            }
            val response = when (listType) {
                MovieListType.NowPlaying -> api.getNowPlayingList(page = loadKey)
                MovieListType.UpComing -> api.getUpComingList(page = loadKey)
            }

            if (loadType == LoadType.REFRESH) {
                remoteKeyDao.deleteByListType(listType)

                when (listType) {
                    MovieListType.NowPlaying -> movieDao.deleteByReleased()
                    MovieListType.UpComing -> movieDao.deleteByUnreleased()
                }
            }

            val nextKey = if (response.results.isEmpty()) null else response.page + 1
            val remoteKeyEntity = MovieListRemoteKeyEntity(listType, nextKey)
            remoteKeyDao.upsert(remoteKeyEntity)

            val entities = response.results.map { it.toEntity() }
            movieDao.upsert(entities)

            MediatorResult.Success(response.results.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}