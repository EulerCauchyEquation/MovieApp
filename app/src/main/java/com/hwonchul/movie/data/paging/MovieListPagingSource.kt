package com.hwonchul.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.remote.api.tmdb.TMDBService
import com.hwonchul.movie.data.remote.model.toEntity
import com.hwonchul.movie.domain.model.MovieListType

class MovieListPagingSource(
    private val service: TMDBService,
    private val listType: MovieListType,
) : PagingSource<Int, MovieEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        return try {
            val nextKey = params.key ?: TMDBService.INITIAL_PAGE_INDEX
            val response = when (listType) {
                MovieListType.NowPlaying -> service.getNowPlayingList(page = nextKey)
                MovieListType.UpComing -> service.getUpComingList(page = nextKey)
            }
            val entities = response.results.map { it.toEntity() }
            LoadResult.Page(
                data = entities,
                prevKey = if (nextKey == 1) null else nextKey - 1,
                nextKey = if (response.results.isEmpty()) null else nextKey + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}