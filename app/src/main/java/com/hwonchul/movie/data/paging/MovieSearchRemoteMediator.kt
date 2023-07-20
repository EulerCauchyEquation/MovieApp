package com.hwonchul.movie.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieSearchRemoteKeyEntity
import com.hwonchul.movie.data.remote.api.tmdb.TMDBService
import com.hwonchul.movie.data.remote.model.toEntity

@OptIn(ExperimentalPagingApi::class)
class MovieSearchRemoteMediator(
    private val db: MovieDatabase,
    private val api: TMDBService,
    private val keyword: String,
) : RemoteMediator<Int, MovieEntity>() {
    private val movieDao = db.movieDao()
    private val remoteKeyDao = db.movieSearchRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> TMDBService.INITIAL_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val remoteKey = remoteKeyDao.getRemoteKeyByKeyword(keyword = keyword)
                    if (remoteKey.nextPageKey == null) {
                        // nextKey가 없으면 더이상 추가 데이터가 없는 것이므로, 끝에 도달했음을 알림
                        return MediatorResult.Success(true)
                    }
                    remoteKey.nextPageKey
                }
            }

            val response = api.searchMoviesByKeyword(query = keyword, page = loadKey)
            if (loadType == LoadType.REFRESH) {
                // 새로고침 케이스는 오래된 데이터 삭제를 선행 작업
                remoteKeyDao.deleteByKeyword(keyword)
                movieDao.deleteByKeyword(keyword)
            }

            val nextKey = if (response.results.isEmpty()) null else response.page + 1
            val searchRemoteKeyEntity = MovieSearchRemoteKeyEntity(keyword, nextKey)
            remoteKeyDao.upsert(searchRemoteKeyEntity)

            val movieEntities = response.results.map { it.toEntity().copy(keyword = keyword) }
            movieDao.upsert(movieEntities)

            MediatorResult.Success(response.results.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}